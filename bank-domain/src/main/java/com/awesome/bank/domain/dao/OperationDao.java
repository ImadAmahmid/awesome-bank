package com.awesome.bank.domain.dao;

import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.domain.model.OperationType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

public interface OperationDao {

    /**
     * Saves a new operation
     */
    Operation saveOperation(Account accountId, BigDecimal amount, OperationType type);

    /**
     * Returns all the operations related to an account Id since a given date. The result is sorted in descending order by date
     */
    List<Operation> findAllOperationsForAccountSinceDate(Long accountId, OffsetDateTime date);

}
