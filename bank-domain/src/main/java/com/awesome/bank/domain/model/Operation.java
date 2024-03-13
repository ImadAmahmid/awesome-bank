package com.awesome.bank.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
public class Operation {

    private Long id;

    private BigDecimal amount;

    private OperationType type;

    private Account account;

    private OffsetDateTime createdAt;

    private String createdBy;
}
