package com.awesome.bank.domain.validator;

import com.awesome.bank.domain.annotation.Validator;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.AccountType;

import java.math.BigDecimal;

@Validator
public class DefaultAccountValidator implements AccountValidator {

    @Override
    public void validate(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account is null!");
        }

        if (account.getOverdraftAllowed() == null) {
            account.setOverdraftAllowed(BigDecimal.ZERO);
        }
        if (AccountType.LIVRET_A.equals(account.getType()) && BigDecimal.ZERO.compareTo(account.getOverdraftAllowed()) < 0) {
            throw new IllegalArgumentException("You cannot have overdraft on a Livret A account!");
        }

        if (account.getHasLimit()) {
            if (account.getLimitAllowed() == null) {
                throw new IllegalArgumentException("You should define the account limit");
            }
            if (account.getBalance().compareTo(account.getLimitAllowed()) > 1) {
                throw new IllegalArgumentException("Cannot have a balance above your limit");
            }
        }

        if (account.getOverdraftAllowed().compareTo(BigDecimal.ZERO) == -1) {
            throw new IllegalArgumentException("Account overdraft cannot be negative!");
        }

        if (account.getBalance().compareTo(account.getOverdraftAllowed().negate()) == -1) {
            throw new IllegalArgumentException("Cannot have a balance bellow your overdraft allowed");
        }

        if (account.getHasLimit() && account.getLimitAllowed().compareTo(BigDecimal.ZERO) == -1) {
            throw new IllegalArgumentException("Account Limit cannot be negative!");
        }
    }

}
