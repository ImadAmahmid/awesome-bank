package com.awesome.bank.dal.dao;

import com.awesome.bank.dal.entity.AccountEntity;
import com.awesome.bank.dal.repository.AccountRepository;
import com.awesome.bank.domain.dao.AccountDao;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.dal.mapper.AccountToDomainMapper;
import com.awesome.bank.dal.mapper.AccountToEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AccountDaoImpl implements AccountDao {

    private final AccountRepository accountRepository;

    @Override
    public List<Account> findAllAccounts() {
        List<AccountEntity> all = accountRepository.findAll();
        return AccountToDomainMapper.MAPPER.map(all);
    }

    @Override
    public Account saveAccount(Account account) {
        LOG.info("Saving account | accountId=[{}]", account.getId());

        AccountEntity accountEntity = AccountToEntityMapper.MAPPER.map(account);
        AccountEntity saved = accountRepository.save(accountEntity);
        return AccountToDomainMapper.MAPPER.map(saved);
    }

    @Override
    public Optional<Account> findByAccountId(Long accountId) {
        return accountRepository.findById(accountId)
                .map(acc -> AccountToDomainMapper.MAPPER.map(acc))
                .stream().findAny();
    }

    @Override
    public Optional<Account> findByAccountIdAndLock(Long accountId) {
        return accountRepository.findByAccountIdAndLock(accountId)
                .map(acc -> AccountToDomainMapper.MAPPER.map(acc))
                .stream().findAny();
    }

    @Override
    public void deleteAccountById(Long accountId) {
        accountRepository.deleteAccountById(accountId);
    }
}
