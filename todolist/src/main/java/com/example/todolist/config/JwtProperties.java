package com.example.todolist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("security.jwt")
public record JwtProperties(
        String privateKeyPem,
        String publicKeyPem,
        @DefaultValue("3600") long accessTokenTtlSeconds,
        @DefaultValue("604800") long refreshTokenTtlSeconds,
        @DefaultValue("identity-service") String issuer,
        @DefaultValue("access") String access,
        @DefaultValue("refresh") String refresh,
        String audience
) {
}