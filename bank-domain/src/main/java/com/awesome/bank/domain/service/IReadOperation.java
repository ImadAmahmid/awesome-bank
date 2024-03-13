package com.awesome.bank.domain.service;

import com.awesome.bank.domain.model.Account;

public interface IReadOperation {

    /**
     * Lists all operations of an account since 30 days ago
     *
     * @param accountId
     * @return an account with the transaction
     */
    Account findAllOperationsForAccountLastMonth(Long accountId);
}
