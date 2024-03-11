package com.awesome.bank.domain.handler;

import com.awesome.bank.domain.exception.MaximumBalanceExceededException;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.domain.model.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.awesome.bank.domain.model.OperationType.DEPOSIT;

/**
 * Handles the logic to deposit a certain amount from account
 */
@Service
@Slf4j
public class DepositOperationHandler extends AccountOperationHandler {

	public Operation makeTransaction(Account account, BigDecimal amount) {
		LOG.info("Make a deposit for account | amount=[{}] accId=[{}] accVersion=[{}]", amount, account.getId(), account.getVersion());
		return handleDepositOperation(account, amount);
	}

	public OperationType getOperationType() {
		return OperationType.DEPOSIT;
	}


	/**
	 * In case the amount added does not exceed the limit of the account, add a new operation. Otherwise, throw MaximumBalanceExceededException error.
	 *
	 * @param account
	 * @param amount
	 */
	private Operation handleDepositOperation(Account account, BigDecimal amount) {
		BigDecimal currentBalance = account.getBalance();
		// Check if the account has a limit, if so make sure that the limit is not exceeded after the deposit
		if (wouldAccountSurpassLimit(account, amount, currentBalance)) {
			throw new MaximumBalanceExceededException();
		}

		account.setBalance(currentBalance.add(amount));
		Operation operation = operationDao.saveOperation(account, amount, DEPOSIT);

		accountDao.saveAccount(account);
		return operation;
	}

	/**
	 * For certain types of account with the boolean hasLimit true, the limit should not be exceeded.
	 *
	 * @param account
	 * @param amount
	 * @param currentBalance
	 * @return
	 */
	private boolean wouldAccountSurpassLimit(Account account, BigDecimal amount, BigDecimal currentBalance) {
		return Boolean.TRUE.equals(account.getHasLimit()) && currentBalance.add(amount).compareTo(account.getLimitAllowed()) == 1;
	}
}
