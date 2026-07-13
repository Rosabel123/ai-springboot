package com.xxs.aispringbooot.config;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;
    private Long expiration;
    private Long refreshTokenExpiration;
    private String header;
    private String tokenPrefix;
    private String issuer;
}
