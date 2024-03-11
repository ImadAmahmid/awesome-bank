package com.awesome.bank.domain.service;

import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.domain.model.OperationType;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

public interface OperationService {

    /**
     * @param accountId
     * @param operationType
     * @param amount
     * @return
     */
    @Transactional(isolation = SERIALIZABLE)
    Operation updateBalance(Long accountId, OperationType operationType, BigDecimal amount);

    /**
     * Lists all operations of an account since 30 days ago
     *
     * @param accountId
     * @return an account with the transaction
     */
    Account findAllOperationsForAccountLastMonth(Long accountId);
}
