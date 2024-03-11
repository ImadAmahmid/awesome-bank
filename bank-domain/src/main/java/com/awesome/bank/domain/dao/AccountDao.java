package com.awesome.bank.domain.dao;

import com.awesome.bank.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {


    List<Account> findAllAccounts();
    /**
     * Takes an account
     *
     * @param account
     * @return the updated account if already exists or a new account
     */
    Account saveAccount(Account account);

    /**
     * Returns the account if exists
     */
    Optional<Account> findByAccountId(Long accountId);

    /**
     * Returns the account if exists
     */
    Optional<Account> findByAccountIdAndLock(Long accountId);

    /**
     * Deletes the account if exists
     */
    void deleteAccountById(Long accountId);

}
