package com.awesome.bank.security.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("application.security.jwt")
public class TokenProperties {

    private String secretKey;
    private Long expiration;

}
