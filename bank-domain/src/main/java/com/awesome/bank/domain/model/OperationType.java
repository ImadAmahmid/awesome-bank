package com.awesome.bank.domain.model;


/**
 * The type of operation we allow over a bank account balance.
 *
 * Can include further operations like FORCE_WITHDRAWAL etc.
 */
public enum OperationType {
    WITHDRAWAL, DEPOSIT
}
