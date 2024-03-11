package com.awesome.bank.domain.validator;

import com.awesome.bank.domain.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.awesome.bank.domain.model.AccountType.LIVRET_A;
import static com.awesome.bank.domain.model.AccountType.NORMAL;

@Slf4j
@Component
public class DefaultAccountSanitizer implements AccountSanitizer {

    /**
     * Does all the sanitizing for an account and make sure its coherent
     *
     * @param account
     */
    @Override
    public Account sanitize(Account account) {
        // to get a new Object
        Account tmp = account.toBuilder().build();

        if (tmp.getType().equals(LIVRET_A)) {
            LOG.debug("[Account Service] Updating the limit/overdraft of account | accountType={}", tmp.getType());
            tmp.setHasLimit(true);
            tmp.setOverdraftAllowed(BigDecimal.ZERO);
        }

        if (tmp.getType().equals(NORMAL)) {
            LOG.debug("[Account Service] Updating the limit of account | accountType={}", tmp.getType());
            tmp.setHasLimit(false);
            tmp.setLimitAllowed(BigDecimal.valueOf(-1l));
            if (tmp.getOverdraftAllowed() == null) {
                LOG.debug("[Account Service] Setting overdraft to 0 account | accountType={}", tmp.getType());
                tmp.setOverdraftAllowed(BigDecimal.ZERO);
            }
        }

        if (tmp.getBalance() == null) {
            LOG.debug("[Account Service] Setting balance to 0 account | accountType={}", tmp.getType());
            tmp.setBalance(BigDecimal.ZERO);
        }

        return tmp;
    }
}
