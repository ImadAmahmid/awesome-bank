package com.awesome.bank.dal.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = {"com.awesome.bank.dal.entity"})
@EnableJpaRepositories(basePackages = {"com.awesome.bank.dal.repository"})
@EnableTransactionManagement
public class JpaConfiguration {}
