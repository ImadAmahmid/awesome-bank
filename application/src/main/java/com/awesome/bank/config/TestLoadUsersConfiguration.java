package com.awesome.bank.config;

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

@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class TestLoadUsersConfiguration {


    private final PasswordEncoder passwordEncoder;

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

        };
    }

}
