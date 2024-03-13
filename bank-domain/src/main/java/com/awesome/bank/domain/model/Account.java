package com.awesome.bank.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private Long id;

    private BigDecimal balance;

    private AccountType type;

    /**
     * This decimal will be set to 0 in case the decouvert is not allowed in this account
     */
    private BigDecimal overdraftAllowed;

    /**
     * Not too necessary but in order to have a clear and simple logic for deposit it suitable to have such a boolean
     */
    private Boolean hasLimit;

    private BigDecimal limitAllowed;

    private Instant createdAt;

    private Instant latestUpdateDate;

    private String createdBy;

    private Long version;

    private List<Operation> operations;
}
