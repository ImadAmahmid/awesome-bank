package com.awesome.bank.adapter;

import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.domain.model.OperationType;
import com.awesome.bank.domain.service.OperationService;
import com.awesome.bank.dto.generated.AccountOperationsDto;
import com.awesome.bank.dto.generated.OperationDto;
import com.awesome.bank.mapper.AccountOperationsMapper;
import com.awesome.bank.mapper.OperationToDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OperationServiceAdapter implements IOperationServiceAdapter {

    private final OperationService operationService;


    @Override
    public OperationDto updateBalance(Long accountId, OperationType operationType, BigDecimal amount) {
        Operation operation = operationService.updateBalance(accountId, operationType, amount);
        return OperationToDtoMapper.MAPPER.map(operation);
    }

    @Override
    public AccountOperationsDto findAllLatestAccountOperations(Long accountId) {
        Account account = operationService.findAllOperationsForAccountLastMonth(accountId);

        return AccountOperationsMapper.MAPPER.map(account);
    }
}
