package com.awesome.bank.domain.handler;

import com.awesome.bank.domain.dao.AccountDao;
import com.awesome.bank.domain.dao.OperationDao;
import com.awesome.bank.domain.exception.InsufficientBalanceException;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.domain.model.OperationType;
import com.awesome.bank.domain.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.awesome.bank.domain.model.OperationType.WITHDRAWAL;


/**
 * Handles the logic to withdraw a certain amount from account
 */
@Service
public class WithdrawalOperationHandler extends AccountOperationHandler {

	public Operation makeTransaction(Account account, BigDecimal amount) {
		return handleWithdrawalOperation(account, amount);
	}

	public OperationType getOperationType() {
		return OperationType.WITHDRAWAL;
	}


	/**
	 * In case the balance is sufficient, adds a new withdrawal operation if withdrawal is allowed and the balance is sufficient. Otherwise, throw an error.
	 *
	 * @param account
	 * @param amount
	 */
	private Operation handleWithdrawalOperation(Account account, BigDecimal amount) {
		BigDecimal currentBalance = account.getBalance();
		if (currentBalance.add(account.getOverdraftAllowed()).compareTo(amount) > -1) {
			account.setBalance(currentBalance.subtract(amount));
			Operation operation = operationDao.saveOperation(account, amount, WITHDRAWAL);
			accountDao.saveAccount(account);
			return operation;
		}

		throw new InsufficientBalanceException();
	}

}
