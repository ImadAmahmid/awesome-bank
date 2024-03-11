package com.awesome.bank.domain.service;

import com.awesome.bank.domain.dao.OperationDao;
import com.awesome.bank.domain.handler.AccountOperationHandler;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.domain.model.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@Transactional
@RequiredArgsConstructor
@Service
public class DefaultOperationService implements OperationService {

	private final AccountService accountService;
	private final OperationDao operationDao;
	private final List<AccountOperationHandler> operationHandlers;

	@Override
	@Transactional(isolation = SERIALIZABLE)
	public Operation updateBalance(Long accountId, OperationType operationType, BigDecimal amount) {
		Account account = accountService.findByAccountIdAndLock(accountId);
		AccountOperationHandler handler = operationHandlers.stream().filter(accountOperationHandler -> accountOperationHandler.getOperationType().equals(operationType))
				.findAny().orElseThrow(() -> new IllegalArgumentException("Not supported operation"));
		return handler.makeTransaction(account, amount);
	}

	@Override
	public Account findAllOperationsForAccountLastMonth(Long accountId) {
		OffsetDateTime aMonthAgoDate = OffsetDateTime.now().minus(30, ChronoUnit.DAYS);
		Account account = accountService.findAccountById(accountId);
		List<Operation> allOperationsForAccountSinceDate = operationDao.findAllOperationsForAccountSinceDate(accountId, aMonthAgoDate);
		account.setOperations(allOperationsForAccountSinceDate);

		return account;
	}

}
