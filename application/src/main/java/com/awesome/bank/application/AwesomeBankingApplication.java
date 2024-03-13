package com.awesome.bank.application;

import com.awesome.bank.config.ApplicationAuditAware;
import com.awesome.bank.config.ModulesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@Import(ModulesConfiguration.class)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AwesomeBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwesomeBankingApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new ApplicationAuditAware();
    }

}
