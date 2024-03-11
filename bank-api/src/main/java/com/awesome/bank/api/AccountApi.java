package com.awesome.bank.api;

import com.awesome.bank.adapter.IAccountServiceAdapter;
import com.awesome.bank.dto.generated.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController()
public class AccountApi implements com.awesome.bank.api.generated.AccountApi {

    private final IAccountServiceAdapter accountServiceAdapter;

    @Override
    public ResponseEntity<AccountDto> createNewAccount(AccountDto accountDto) {
        LOG.info("[Account API] create new account | balance=[{}] accountType=[{}]", accountDto.getBalance(), accountDto.getType());
        return ResponseEntity.ok(accountServiceAdapter.saveAccount(accountDto));
    }

    @Override
    public ResponseEntity<AccountDto> getAccountById(Long accountId) {
        LOG.info("[Account API] Find account | accountId=[{}]",accountId);
        return ResponseEntity.ok(accountServiceAdapter.findByAccountId(accountId));

    }

    @Override
    public ResponseEntity<List<AccountDto>> getAllBankAccounts() {
        LOG.info("[Account API] Get all bank accounts");
        return ResponseEntity.ok(accountServiceAdapter.findAll());
    }

    @Override
    public ResponseEntity<Void> deleteAccount(Long accountId) {
        LOG.info("[Account API] Delete account | accountId=[{}]", accountId);
        accountServiceAdapter.deleteAccountById(accountId);
        return ResponseEntity.noContent().build();
    }
}
