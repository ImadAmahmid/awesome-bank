package com.awesome.bank.domain.service;

import com.awesome.bank.domain.dao.AccountDao;
import com.awesome.bank.domain.exception.AccountNotFoundException;
import com.awesome.bank.domain.exception.CannotConvertAccountTypeException;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.validator.AccountSanitizer;
import com.awesome.bank.domain.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class DefaultAccountService implements AccountService {

	private final AccountDao accountAdapter;
	private final AccountValidator accountValidator;
	private final AccountSanitizer accountSanitizer;

	public Account saveAccount(Account account) {
		LOG.info("[Account Service] Save account | accountType=[{}] accountId=[{}]", account.getType(), account.getId());
		Account sanitizedAccount = accountSanitizer.sanitize(account);
		accountValidator.validate(sanitizedAccount);

		if (account.getId() != null) {
			return updateExistingAccount(sanitizedAccount);
		}

		return accountAdapter.saveAccount(sanitizedAccount);
	}

	public Account findAccountById(Long accountId) {

		return accountAdapter.findByAccountId(accountId).orElseThrow(() -> new AccountNotFoundException());
	}

	public void deleteAccountById(Long accountId) {
		LOG.info("[Account Service] Delete account | accountId=[{}]", accountId);

		accountAdapter.deleteAccountById(accountId);
	}

	/**
	 * Only allow the execution of this method within a transaction.
	 *
	 * @param accountId
	 * @return account if found or throw
	 */
	@Transactional(propagation = MANDATORY)
	public Account findByAccountIdAndLock(Long accountId) {
		LOG.info("[Account Service] Find account and lock | accountId=[{}]", accountId);
		return accountAdapter.findByAccountIdAndLock(accountId).orElseThrow(() -> new AccountNotFoundException());
	}

	public List<Account> findAllAccounts() {
		return accountAdapter.findAllAccounts();
	}

	/**
	 * A quick attempt to prevent bypassing the deposits and getting rich too easily.
	 * You can only deposit money or withdraw money through operations.
	 * We could define also a strategy for this
	 * depending on the account type.
	 *
	 * todo: double check the premise of this method as it's really not satisfactory
	 * @param account
	 * @return
	 */
	private Account updateExistingAccount(Account account) {
		LOG.info("[Account Service] Account exists, updating ... | accountType=[{}] accountId=[{}]", account.getType(), account.getId());

		Account accountById = findAccountById(account.getId());
		if (!account.getType().equals(accountById.getType())) {
			throw new CannotConvertAccountTypeException();
		}
		accountById.setLimitAllowed(account.getLimitAllowed());
		accountById.setOverdraftAllowed(account.getOverdraftAllowed());
		return accountAdapter.saveAccount(accountById);
	}

}
