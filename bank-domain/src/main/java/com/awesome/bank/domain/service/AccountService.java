package com.awesome.bank.domain.service;

import com.awesome.bank.domain.model.Account;

import java.util.List;

public interface AccountService {

    Account saveAccount(Account account);

    Account findAccountById(Long accountId);

    void deleteAccountById(Long accountId);

    Account findByAccountIdAndLock(Long accountId);

    List<Account> findAllAccounts();
}
