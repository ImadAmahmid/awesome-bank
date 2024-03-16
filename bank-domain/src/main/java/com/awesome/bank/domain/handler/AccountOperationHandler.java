package com.awesome.bank.domain.handler;

import com.awesome.bank.domain.dao.AccountDao;
import com.awesome.bank.domain.dao.OperationDao;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.domain.model.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * An abstract Operation handler in order to be able to extend other types of transaction, like a FORCE_WITHDRAWAL mechanism.
 * <p>
 * We could also have a handler for each account type that implements the behaviour upon each operation depending on the account (i.e. depoising for a
 * NORMAL account would have no condition whereas it would for the LIVRET_A type).
 */
@RequiredArgsConstructor
public abstract class AccountOperationHandler {

	@Autowired
	OperationDao operationDao;
	@Autowired
	AccountDao accountDao;

	/**
	 * It literally does make a transaction within a transaction :)
	 * <p>
	 *
	 * @param account
	 * @param amount
	 * @return
	 */
	public abstract Operation makeTransaction(Account account, BigDecimal amount);

	/**
	 * returns the account type supported by the handler
	 *
	 * @return
	 */
	public abstract OperationType getOperationType();

}
