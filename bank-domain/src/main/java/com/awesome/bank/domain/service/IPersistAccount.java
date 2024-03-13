package com.awesome.bank.domain.service;

import com.awesome.bank.domain.model.Account;

public interface IPersistAccount {

    Account saveAccount(Account account);

    void deleteAccountById(Long accountId);
}
