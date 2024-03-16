package com.awesome.bank.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * Scan the beans from com.awesome.bank.
 * <p>
 * Some of these beans should be annotated with @ConditionalMissingBean in order to allow the application
 * layer to override them without having to use Qualifiers.
 */
@Configuration
@ComponentScan({"com.awesome.bank.*"})
public class ModulesConfiguration {

}
