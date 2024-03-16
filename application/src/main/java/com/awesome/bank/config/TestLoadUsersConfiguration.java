package com.awesome.bank.config;

import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.AccountType;
import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.domain.model.OperationType;
import com.awesome.bank.domain.service.AccountService;
import com.awesome.bank.domain.service.OperationService;
import com.awesome.bank.security.service.AuthenticationService;
import com.awesome.bank.security.user.Role;
import com.awesome.bank.security.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class TestLoadUsersConfiguration {


    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final OperationService operationService;

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService service) {

        return args -> {
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.USER)
                    .build();

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ADMIN)
                    .build();

            User manager = User.builder()
                    .username("manager")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.MANAGER)
                    .build();

            LOG.info("Token for user is [{}]. ", service.register(user).getAccessToken());
            LOG.info("Token for admin is [{}]. ", service.register(admin).getAccessToken());
            LOG.info("Token for manager is [{}]. ", service.register(manager).getAccessToken());
            Account account = accountService.saveAccount(Account.builder().type(AccountType.NORMAL).balance(BigDecimal.ZERO).build());
            Operation operation = operationService.updateBalance(account.getId(), OperationType.DEPOSIT, BigDecimal.valueOf(500));
            Operation operation2 = operationService.updateBalance(account.getId(), OperationType.WITHDRAWAL, BigDecimal.valueOf(200));
            Operation operation3 = operationService.updateBalance(account.getId(), OperationType.DEPOSIT, BigDecimal.valueOf(12));
            LOG.info("Created Account = [{}] and Operation = [{}]. ", account.getId(), operation.getId());
        };
    }

}
