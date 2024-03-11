package com.awesome.bank.application;

import com.awesome.bank.config.ModulesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ModulesConfiguration.class)
public class AwesomeBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwesomeBankingApplication.class, args);
    }

}
