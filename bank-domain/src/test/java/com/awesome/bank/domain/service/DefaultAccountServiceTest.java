package com.awesome.bank.domain.service;

import com.awesome.bank.domain.dao.AccountDao;
import com.awesome.bank.domain.exception.AccountNotFoundException;
import com.awesome.bank.domain.exception.CannotConvertAccountTypeException;
import com.awesome.bank.domain.handler.AccountOperationHandler;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.AccountType;
import com.awesome.bank.domain.validator.AccountSanitizer;
import com.awesome.bank.domain.validator.AccountValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Unit tests
 */
@ExtendWith(MockitoExtension.class)
class DefaultAccountServiceTest {

//    private static final Date NOW = Date.from(Instant.now());
    private static final Instant NOW = Instant.now();
    // Pretty poor guy
    private static final BigDecimal BALANCE = BigDecimal.ONE;
    private static final Long ACCOUNT_ID = 1l;
    private static final BigDecimal MAXIMUM_ACCONT_BALANCE = BigDecimal.TEN;
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal OVERDRAFT_ALLOWED = BigDecimal.valueOf(200);

    @Mock
    AccountDao accountAdapter;
    @Mock
    AccountOperationHandler handler;
    @Mock
    AccountValidator accountValidator;
    @Mock
    AccountSanitizer accountSanitizer;

    @InjectMocks
    private DefaultAccountService accountService;

    @BeforeAll
    static void setup() {

    }

    @Test
    void findAccountById_givenAccountExists_ok() {
        Account acc = Account.builder().id(ACCOUNT_ID).type(AccountType.NORMAL).balance(BALANCE)
                .overdraftAllowed(OVERDRAFT_ALLOWED).createdAt(NOW).hasLimit(false).build();

        when(accountAdapter.findByAccountId(eq(ACCOUNT_ID))).thenReturn(Optional.of(acc));

        Account result = accountService.findAccountById(ACCOUNT_ID);

        assertThat(acc).isEqualTo(result);
    }

    @Test
    void findAccountById_givenAccountNotExists_errorThrown() {
        when(accountAdapter.findByAccountId(eq(ACCOUNT_ID))).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.findAccountById(ACCOUNT_ID));
    }

    @Test
    void findByAccountIdAndLock() {
        Account acc = Account.builder().id(ACCOUNT_ID).type(AccountType.NORMAL).balance(BALANCE)
                .overdraftAllowed(OVERDRAFT_ALLOWED).createdAt(NOW).hasLimit(false).build();

        when(accountAdapter.findByAccountIdAndLock(eq(ACCOUNT_ID))).thenReturn(Optional.of(acc));

        Account result = accountService.findByAccountIdAndLock(ACCOUNT_ID);

        assertThat(acc).isEqualTo(result);
    }

    @Test
    void findAllAccounts() {
        Account acc = Account.builder().id(ACCOUNT_ID).type(AccountType.NORMAL).balance(BALANCE)
                .overdraftAllowed(OVERDRAFT_ALLOWED).createdAt(NOW).hasLimit(false).build();

        when(accountAdapter.findAllAccounts()).thenReturn(List.of(acc));

        List<Account> result = accountService.findAllAccounts();

        assertThat(result).contains(acc);
    }

    @Nested
    class SaveAccount {

        @Test
        void saveNewLivretA_setsOverdraftToZeroAndHasLimit_ok() {
            Account acc = Account.builder().type(AccountType.LIVRET_A).limitAllowed(MAXIMUM_ACCONT_BALANCE).balance(BALANCE).createdAt(NOW).build();
            Account sanitizedAcc = acc.toBuilder().overdraftAllowed(ZERO).hasLimit(true).build();

            when(accountSanitizer.sanitize(eq(acc))).thenReturn(sanitizedAcc);
            doNothing().when(accountValidator).validate(eq(sanitizedAcc));
            when(accountAdapter.saveAccount(eq(sanitizedAcc))).thenReturn(sanitizedAcc);

            Account res = accountService.saveAccount(acc);

            assertThat(res).isEqualTo(sanitizedAcc);
        }

        @Test
        void saveNewNormalAccount_setLimitAllowedToZeroAndHasLimitToFalse_ok() {
            Account acc = Account.builder().type(AccountType.NORMAL).balance(BALANCE)
                    .overdraftAllowed(OVERDRAFT_ALLOWED).createdAt(NOW).build();
            Account sanitizedAcc = acc.toBuilder().hasLimit(false).build();

            when(accountSanitizer.sanitize(eq(acc))).thenReturn(sanitizedAcc);
            doNothing().when(accountValidator).validate(eq(sanitizedAcc));
            when(accountAdapter.saveAccount(eq(sanitizedAcc))).thenReturn(sanitizedAcc);

            Account res = accountService.saveAccount(acc);

            assertThat(res).isEqualTo(sanitizedAcc);
        }


        @Test
        void updateAccount_withDifferentType() {
            Account newAcc = Account.builder().id(ACCOUNT_ID).type(AccountType.NORMAL).balance(BALANCE)
                    .overdraftAllowed(OVERDRAFT_ALLOWED).createdAt(NOW).hasLimit(false).build();
            Account oldAccount = Account.builder().id(ACCOUNT_ID).type(AccountType.LIVRET_A).balance(BALANCE)
                    .overdraftAllowed(OVERDRAFT_ALLOWED).createdAt(NOW).hasLimit(false).build();

            when(accountSanitizer.sanitize(eq(newAcc))).thenReturn(newAcc);
            doNothing().when(accountValidator).validate(eq(newAcc));
            when(accountAdapter.findByAccountId(eq(ACCOUNT_ID))).thenReturn(Optional.of(oldAccount));

            assertThrows(CannotConvertAccountTypeException.class, () -> accountService.saveAccount(newAcc));
        }

    }
}