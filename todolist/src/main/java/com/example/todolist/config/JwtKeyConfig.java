package com.example.todolist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtKeyConfig {

    @Value("${PRIVATE_KEY}")
    private String privateKey;

    @Value("${PUBLIC_KEY}")
    private String publicKey;

    @Bean
    public RSAPublicKey loadPublicKey() {
        try {
            byte[] decoded = Base64.getDecoder().decode(publicKey);
            String pem = new String(decoded, StandardCharsets.UTF_8);
            String publicKeyPem = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(publicKeyPem);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to load RSA public key from configuration", ex);
        }
    }

    @Bean
    public RSAPrivateKey loadPrivateKey() {
        try {
            byte[] decoded = Base64.getDecoder().decode(privateKey);
            String pem = new String(decoded, StandardCharsets.UTF_8);
            String privateKeyPem = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to load RSA private key from configuration", ex);
        }
    }
}