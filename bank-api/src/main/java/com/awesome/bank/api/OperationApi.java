package com.awesome.bank.api;

import com.awesome.bank.adapter.IOperationServiceAdapter;
import com.awesome.bank.domain.model.OperationType;
import com.awesome.bank.dto.generated.AccountOperationsDto;
import com.awesome.bank.dto.generated.OperationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OperationApi implements com.awesome.bank.api.generated.OperationApi {

    private final IOperationServiceAdapter operationServiceAdapter;

    @Override
    public ResponseEntity<OperationDto> deposit(Long accountId, BigDecimal amount) {
        LOG.info("[Account API] Deposit in account | accountId=[{}] amount=[{}]", accountId, amount);
        return ResponseEntity.ok(operationServiceAdapter.updateBalance(accountId, OperationType.DEPOSIT, amount));
    }

    @Override
    public ResponseEntity<OperationDto> withdraw(Long accountId, BigDecimal amount) {
        LOG.info("[Account API] Withdraw from account | accountId=[{}] amount=[{}]", accountId, amount);
        return ResponseEntity.ok(operationServiceAdapter.updateBalance(accountId, OperationType.WITHDRAWAL, amount));
    }

    @Override
    public ResponseEntity<AccountOperationsDto> getAccountLatestOperations(Long accountId) {
        LOG.info("[Account API] Get all account transactions | accountId=[{}]", accountId);
        return ResponseEntity.ok(operationServiceAdapter.findAllLatestAccountOperations(accountId));
    }
}
