package com.awesome.bank.domain.service;

import com.awesome.bank.domain.handler.AccountOperationHandler;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static com.awesome.bank.domain.model.OperationType.WITHDRAWAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


// Todo: to be finished, the implementation is very similar to DefaultAccountServiceTest
@ExtendWith(MockitoExtension.class)
class DefaultOperationServiceTest {

    private static final Long ACCOUNT_ID = 1l;
    private static final BigDecimal AMOUNT = BigDecimal.TEN;
    private static final long OPERATION_ID = 10l;

    @Mock
    AccountOperationHandler withdrawOperationHandler;
    @Mock
    List<AccountOperationHandler> operationHandlers;
    @Mock
    AccountService accountService;

    @InjectMocks
    private DefaultOperationService operationService;

    @BeforeEach
    void setup() {
        when(operationHandlers.stream()).thenReturn(Stream.of(withdrawOperationHandler));
        when(withdrawOperationHandler.getOperationType()).thenReturn(WITHDRAWAL);
    }

    @Test
    void updateBalance_ok() {
        Account acc = Account.builder().id(ACCOUNT_ID).build();
        Operation operation = Operation.builder().id(OPERATION_ID).build();

        when(accountService.findByAccountIdAndLock(eq(ACCOUNT_ID))).thenReturn(acc);
        when(withdrawOperationHandler.getOperationType()).thenReturn(WITHDRAWAL);

        when(withdrawOperationHandler.makeTransaction(eq(acc), eq(AMOUNT))).thenReturn(operation);

        Operation result = operationService.updateBalance(ACCOUNT_ID, WITHDRAWAL, AMOUNT);

        assertThat(result).isEqualTo(operation);
    }

}