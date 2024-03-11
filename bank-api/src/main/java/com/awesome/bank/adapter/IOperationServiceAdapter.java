package com.awesome.bank.adapter;

import com.awesome.bank.domain.model.OperationType;
import com.awesome.bank.dto.generated.AccountOperationsDto;
import com.awesome.bank.dto.generated.OperationDto;

import java.math.BigDecimal;

public interface IOperationServiceAdapter {

    OperationDto updateBalance(Long accountId, OperationType operationType, BigDecimal amount);

    /**
     * Lists all operations of an account since a month ago
     *
     * @param accountId
     * @return
     */
    AccountOperationsDto findAllLatestAccountOperations(Long accountId);
}
