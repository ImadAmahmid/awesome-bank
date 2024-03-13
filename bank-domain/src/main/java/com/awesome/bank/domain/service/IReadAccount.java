package com.awesome.bank.domain.service;

import com.awesome.bank.domain.model.Account;

import java.util.List;

public interface IReadAccount {

    Account findAccountById(Long accountId);

    Account findByAccountIdAndLock(Long accountId);

    List<Account> findAllAccounts();
}
