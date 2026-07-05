package com.example.todolist.config;


import com.example.todolist.cache.CacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;


import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    private static final String REVOKE_PREFIX = "revoked:";
    private final CacheService cacheService;

    private final String[] PUBLIC_URLS = {
            "/auth",
            "/auth/login",
            "/auth/register",
            "/auth/introspect",
            "/users",
            "/users/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtDecoder jwtDecoder ) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->authorizeRequests
                .requestMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().authenticated());
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(sessionManagement
                -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.oauth2ResourceServer(oauth2
                -> oauth2
                .bearerTokenResolver(bearerTokenResolver())
                .jwt(jwtConfigurer
                        -> jwtConfigurer.decoder(jwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(jwtAuthenticationEntryPoint));
        return http.build();
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver() {
        return request -> {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }

            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("access_token".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
            return null;
        };
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtEncoder jwtEncoder(RSAPublicKey jwtPublicKey,
                                 RSAPrivateKey jwtPrivateKey) {
        RSAKey rsaKey = new RSAKey.Builder(jwtPublicKey)
                .privateKey(jwtPrivateKey)
                .build();

        ImmutableJWKSet<SecurityContext> jwkSource =
                new ImmutableJWKSet<>(new JWKSet(rsaKey));

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey publicKey, JwtProperties props) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(props.issuer()));
        return token -> {
            Jwt jwt = decoder.decode(token);
            Object type = jwt.getClaims().get("type");
            String jti = jwt.getClaimAsString("jti");
            if (jti == null || jti.isBlank() || cacheService.isCacheExist(REVOKE_PREFIX + jti)) {
                throw new JwtValidationException("Token Revoked",
                        List.of(new OAuth2Error("invalid_token", "Token Revoked", null)));
            }

            if (!props.access().equals(type)) {
                throw new JwtValidationException(
                        "Token type not allowed",
                        List.of(new OAuth2Error("invalid_token", "Token type not allowed", null))
                );
            }

            return jwt;
        };
    }

    @Bean
    public JwtDecoder refreshJwtDecoder(RSAPublicKey publicKey, JwtProperties props) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(props.issuer()));

        return token -> {
            Jwt jwt = decoder.decode(token);

            String jti = jwt.getClaimAsString("jti");
            Object type = jwt.getClaims().get("type");

            if (jti == null || jti.isBlank() || cacheService.isCacheExist(REVOKE_PREFIX + jti)) {
                throw new JwtValidationException("Token Revoked",
                        List.of(new OAuth2Error("invalid_token", "Token Revoked", null)));
            }

            if (!props.refresh().equals(type)) {
                throw new JwtValidationException("Token type not allowed",
                        List.of(new OAuth2Error("invalid_token", "Token type not allowed", null)));
            }

            return jwt;
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}