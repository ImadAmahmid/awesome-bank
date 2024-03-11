package com.awesome.bank.dal.repository;

import com.awesome.bank.dal.entity.AccountEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository {

    List<AccountEntity> findAll();

    /**
     * Takes an account
     *
     * @param account
     * @return the updated account if already exists or a new account
     */
    AccountEntity save(AccountEntity account);

    /**
     * Returns the account if exists
     */
    Optional<AccountEntity> findById(Long accountId);

    /**
     * Returns the account if exists
     */
    Optional<AccountEntity> findByAccountIdAndLock(Long accountId);

    /**
     * Deletes the account if exists
     */
    void deleteAccountById(Long accountId);

}
