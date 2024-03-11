package com.awesome.bank.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.awesome.bank.api", "com.awesome.bank.adapter"})
public class ApiAutoConfiguration {

}
