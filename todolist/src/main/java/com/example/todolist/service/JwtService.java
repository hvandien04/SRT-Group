package com.example.todolist.service;

import com.example.todolist.config.JwtProperties;
import com.example.todolist.dto.response.DecodeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties properties;
    private final JwtDecoder refreshJwtDecoder;


    public String createAccessToken(Map<String, Object> claims) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(properties.accessTokenTtlSeconds());
        claims.put("type", properties.access());
        return encode(claims, issuedAt, expiresAt);
    }

    public String createRefreshToken(Map<String, Object> claims) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(properties.refreshTokenTtlSeconds());
        claims.put("type", properties.refresh());
        return encode(claims, issuedAt, expiresAt);
    }

    public DecodeToken decodeToken(String token) {
        Jwt jwt = refreshJwtDecoder.decode(token);
        assert jwt.getExpiresAt() != null;
        return DecodeToken.builder()
                .sub(jwt.getClaimAsString("sub"))
                .jti(jwt.getClaimAsString("jti"))
                .exp(jwt.getExpiresAt().getEpochSecond())
                .build();

    }

    private String encode(Map<String, Object> claims, Instant issuedAt, Instant expiresAt) {
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .id(java.util.UUID.randomUUID().toString())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .issuer(properties.issuer());

        claims.forEach(builder::claim);

        return jwtEncoder.encode(JwtEncoderParameters.from(builder.build())).getTokenValue();
    }
}
