package com.awesome.bank.application;

import com.awesome.bank.dal.entity.AccountEntity;
import com.awesome.bank.dal.entity.OperationEntity;
import com.awesome.bank.dal.repository.AccountPagingAndSortingRepository;
import com.awesome.bank.dal.repository.OperationRepository;
import com.awesome.bank.domain.model.OperationType;
import com.awesome.bank.dto.generated.AccountDto;
import com.awesome.bank.dto.generated.AccountOperationsDto;
import com.awesome.bank.dto.generated.OperationDto;
import com.awesome.bank.event.BiEventPublisher;
import com.awesome.bank.event.impl.DefaultBiEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.awesome.bank.dto.generated.AccountDto.TypeEnum.NORMAL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@ActiveProfiles({"h2","test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AwesomeBankingApplicationTest {

	private static final BigDecimal INIT_BALANCE = BigDecimal.valueOf(50l);
	private static final BigDecimal OVERDRAFT_ALLOWED = BigDecimal.valueOf(200l);
	private static final int NUMBER_OF_THREAD_FOR_CONCURRENCY_TEST = 50;

	// bind the above RANDOM_PORT
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private AccountPagingAndSortingRepository accountPagingAndSortingRepository;
	@Autowired
	private OperationRepository operationRepository;
	@SpyBean
	@Autowired
	BiEventPublisher biEventPublisher;


	@BeforeEach
	public void cleanUp() {
		accountPagingAndSortingRepository.deleteAll();
		operationRepository.deleteAll();
		clearInvocations(biEventPublisher);
	}

	@Nested
	class CrudAccountTests {

		@Test
		public void createAccount_success() throws MalformedURLException {
			AccountDto accountToBeSaved = new AccountDto().balance(INIT_BALANCE).type(NORMAL).overdraftAllowed(OVERDRAFT_ALLOWED);

			HttpEntity<AccountDto> request = new HttpEntity<>(accountToBeSaved, new HttpHeaders());

			ResponseEntity<AccountDto> saveAccountResponse =
					makeRequest(HttpMethod.POST,"http://localhost:" + port + "/api/v1/bank/account/",request, AccountDto.class);

			AccountDto savedAccount = saveAccountResponse.getBody();

			ResponseEntity<List<AccountDto>> getAllAccountsResponse = restTemplate.exchange(
					"http://localhost:" + port + "/api/v1/bank/accounts",
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<>() {});

			Assertions.assertThat(getAllAccountsResponse.getBody()).extracting(AccountDto::getId).contains(savedAccount.getId());

			ResponseEntity deleteResponse = restTemplate.exchange(
					"http://localhost:" + port + "/api/v1/bank/account/" + savedAccount.getId(),
					HttpMethod.DELETE,
					null,
					new ParameterizedTypeReference<>() {});

			Assertions.assertThat(deleteResponse.getStatusCode().is2xxSuccessful());

			// Get all account once again and check it's empty
			getAllAccountsResponse = restTemplate.exchange(
					"http://localhost:" + port + "/api/v1/bank/accounts",
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<>() {});

			Assertions.assertThat(getAllAccountsResponse.getBody()).isEmpty();

		}

		@ParameterizedTest
		@CsvSource({
				"NORMAL,false,0,-200, Cannot have a balance bellow your overdraft allowed",
				"NORMAL,false,-999,200, Account overdraft cannot be negative",
				"LIVRET_A,true,999,200, You should define the account limit",
		})
		@DisplayName("Testing out several cases where account should not be created!")
		public void createAccount_fail(AccountDto.TypeEnum type, Boolean hasLimit, BigDecimal overdraftAllowed, BigDecimal balance, String error) throws MalformedURLException {
			AccountDto accountToBeSaved = new AccountDto().balance(balance).hasLimit(hasLimit).type(type).overdraftAllowed(overdraftAllowed);

			HttpEntity<AccountDto> request = new HttpEntity<>(accountToBeSaved, new HttpHeaders());

			ResponseEntity saveAccountResponse = makeRequest(HttpMethod.POST,"http://localhost:" + port + "/api/v1/bank/account/",request, Object.class);

			Assertions.assertThat(saveAccountResponse.getStatusCode().isSameCodeAs(HttpStatus.METHOD_NOT_ALLOWED)).isTrue();
			Assertions.assertThat(saveAccountResponse.getBody().toString()).contains(error);
		}

	}


	@Nested
	class TestOperations {

		@Test
		public void makeOperations_success() throws MalformedURLException {
			AccountDto accountToBeSaved = new AccountDto().balance(INIT_BALANCE).type(NORMAL).overdraftAllowed(OVERDRAFT_ALLOWED);

			HttpEntity<AccountDto> request = new HttpEntity<>(accountToBeSaved, new HttpHeaders());

			ResponseEntity<AccountDto> saveAccountResponse =
					makeRequest(HttpMethod.POST, "http://localhost:" + port + "/api/v1/bank/account/", request, AccountDto.class);
			Long accId = saveAccountResponse.getBody().getId();


			// Before loading the recent transactions, we save a very old transaction to check if transactions are loaded
			operationRepository.save(OperationEntity.builder()
					.account(AccountEntity.builder().id(accId).version(1l).build())
					.type(OperationType.WITHDRAWAL)
					.amount(BigDecimal.ONE)
					.createdAt(OffsetDateTime.now().minus(NUMBER_OF_THREAD_FOR_CONCURRENCY_TEST, ChronoUnit.DAYS))
					.build());

			String withdrawUrl = UriComponentsBuilder.fromUriString("http://localhost:" + port + "/api/v1/bank/account/" + accId + "/withdraw")
					.queryParam("amount", 5).toUriString();

			ResponseEntity<OperationDto> withdrawOperation =
				makeRequest(HttpMethod.POST, withdrawUrl, null, OperationDto.class);

			Assertions.assertThat(withdrawOperation.getStatusCode().is2xxSuccessful());
			Assertions.assertThat(withdrawOperation.getBody().getNewAccountBalance().compareTo(BigDecimal.valueOf(45l))).isEqualTo(0);

			String depositUrl = UriComponentsBuilder.fromUriString("http://localhost:" + port + "/api/v1/bank/account/" + accId + "/deposit")
					.queryParam("amount", 15).toUriString();

			ResponseEntity<OperationDto> depositOperation =
					makeRequest(HttpMethod.POST, depositUrl, null, OperationDto.class);

			Assertions.assertThat(depositOperation.getStatusCode().is2xxSuccessful());
			Assertions.assertThat(depositOperation.getBody().getNewAccountBalance().compareTo(BigDecimal.valueOf(60l))).isEqualTo(0);

			ResponseEntity<AccountOperationsDto> accountOperations =
					makeRequest(HttpMethod.GET, "http://localhost:" + port + "/api/v1/bank/account/" + accId + "/operations", null, AccountOperationsDto.class);


			Assertions.assertThat(accountOperations.getStatusCode().is2xxSuccessful());
			Assertions.assertThat(accountOperations.getBody().getAccount().getBalance().compareTo(BigDecimal.valueOf(60l))).isEqualTo(0);
			Assertions.assertThat(accountOperations.getBody().getOperations()).hasSize(2);

			// Since the deposit operation happened last, it should be on the top of the list
			Assertions.assertThat(accountOperations.getBody().getOperations().get(0).getId()).isEqualTo(depositOperation.getBody().getId());
			Assertions.assertThat(accountOperations.getBody().getOperations().get(1).getId()).isEqualTo(withdrawOperation.getBody().getId());
			// it will be called one time because we have a buffer in the event publisher.
			verify(biEventPublisher, times(2)).publish(any());
		}

		// todo: Add test for failed operations with a parametrized test defining most scenarios with Normal And Livret Account


		@Test
		@DisplayName("Makes a test for concurrency between many threads trying to update an account at the same time. The sigma of operations " +
				"should be the last account balance.")
		public void makeConcurrentOperations_success() throws MalformedURLException, InterruptedException {
			AccountDto accountToBeSaved = new AccountDto().balance(BigDecimal.ZERO).type(NORMAL).overdraftAllowed(OVERDRAFT_ALLOWED);

			HttpEntity<AccountDto> request = new HttpEntity<>(accountToBeSaved, new HttpHeaders());

			ResponseEntity<AccountDto> saveAccountResponse =
					makeRequest(HttpMethod.POST, "http://localhost:" + port + "/api/v1/bank/account/", request, AccountDto.class);

			Long accId = saveAccountResponse.getBody().getId();

			String depositUrl = UriComponentsBuilder.fromUriString("http://localhost:" + port + "/api/v1/bank/account/" + accId + "/deposit")
					.queryParam("amount", 15).toUriString();

			List<Thread> threads = new ArrayList<>();
			for (Integer i = 0; i < NUMBER_OF_THREAD_FOR_CONCURRENCY_TEST; i++) {
				Integer threadNumber = i;
				Thread newThread = new Thread(() -> {
					LOG.info("Thread [{}] making a deposit", threadNumber);
					makeRequest(HttpMethod.POST, depositUrl, null, Object.class);;
				});
				threads.add(newThread);
				newThread.start();
			}

			// Waits for all the threads to finalize their job and retrieve the successful operations with the main thread
			for (Thread thread : threads) {
				thread.join();
			}

			ResponseEntity<AccountOperationsDto> accountOperations =
					makeRequest(HttpMethod.GET, "http://localhost:" + port + "/api/v1/bank/account/" + accId + "/operations", null, AccountOperationsDto.class);

			BigDecimal newBalance = accountOperations.getBody().getAccount().getBalance();
			// This is the sum of the effective operations that were not rolled back
			BigDecimal successfulDepositsSum = accountOperations.getBody().getOperations().stream().map(OperationDto::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

			Assertions.assertThat(accountOperations.getStatusCode().is2xxSuccessful());
			Assertions.assertThat(accountOperations.getBody().getOperations().size()).isLessThan(NUMBER_OF_THREAD_FOR_CONCURRENCY_TEST);
			Assertions.assertThat(newBalance.compareTo(successfulDepositsSum)).isEqualTo(0);
		}
	}

	public <T> ResponseEntity<T> makeRequest(HttpMethod method, String endpoint, HttpEntity<?> request, Class<T> responseType) {
		return restTemplate.exchange(
				endpoint,
				method,
				request,
				responseType);
	}

}
