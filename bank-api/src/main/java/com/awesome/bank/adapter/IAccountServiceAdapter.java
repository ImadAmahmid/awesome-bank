package com.awesome.bank.adapter;

import com.awesome.bank.dto.generated.AccountDto;

import java.util.List;

public interface IAccountServiceAdapter {

    AccountDto saveAccount(AccountDto accountDto);

    AccountDto findByAccountId(Long accountId);

    List<AccountDto> findAll();

    void deleteAccountById(Long accountId);

}