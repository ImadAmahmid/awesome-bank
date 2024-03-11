package com.awesome.bank.adapter;

import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.service.AccountService;
import com.awesome.bank.dto.generated.AccountDto;
import com.awesome.bank.mapper.AccountToDomainMapper;
import com.awesome.bank.mapper.AccountToDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountServiceAdapter implements IAccountServiceAdapter{

    private final AccountService accountService;

    @Override
    public AccountDto saveAccount(AccountDto accountDto) {
        Account account = accountService.saveAccount(AccountToDomainMapper.MAPPER.map(accountDto));
        return AccountToDtoMapper.MAPPER.map(account);
    }

    @Override
    public AccountDto findByAccountId(Long accountId) {
        Account account = accountService.findAccountById(accountId);
        return AccountToDtoMapper.MAPPER.map(account);
    }

    @Override
    public List<AccountDto> findAll() {
        List<Account> accounts = accountService.findAllAccounts();
        return AccountToDtoMapper.MAPPER.map(accounts);
    }

    @Override
    public void deleteAccountById(Long accountId) {
        accountService.deleteAccountById(accountId);
    }
}
