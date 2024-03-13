package com.awesome.bank.domain.service;

import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.domain.model.OperationType;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

public interface IPersistOperation {

    /**
     * @param accountId
     * @param operationType
     * @param amount
     * @return
     */
    @Transactional(isolation = SERIALIZABLE)
    Operation updateBalance(Long accountId, OperationType operationType, BigDecimal amount);
}
