package com.example.todolist.service;

import com.example.todolist.cache.CacheService;
import com.example.todolist.dto.request.LoginRequest;
import com.example.todolist.dto.request.TokenRequest;
import com.example.todolist.dto.response.AuthTokenResponse;
import com.example.todolist.dto.response.DecodeToken;
import com.example.todolist.entity.User;
import com.example.todolist.exception.ErrorCodeApplication;
import com.example.todolist.repository.UserRepository;
import com.example.todolist.wapper.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final String REVOKE_PREFIX = "revoked:";
    private static final String LOGIN_FAIL_PREFIX = "login_fail:";
    private static final String LOCK_LOGIN_PREFIX = "lock_login:";
    private static final String OTP_PREFIX = "auth:forget_password:" ;
    private static final String OTP_COOL_DOWN_PREFIX = "auth:otp:cooldown:";
    private static final String OTP_LIMIT_PREFIX = "auth:otp:limit:";

    private final CacheService cacheService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void handleLoginFail(String email) {
        String key = LOGIN_FAIL_PREFIX + email;
        cacheService.increment(key, 1, 10);

        int failCount = cacheService.getIntegerValue(key);
        if (failCount >= 5) {
            cacheService.createCache(LOCK_LOGIN_PREFIX + email, "locked", 10);
            cacheService.deleteCache(key);
        }
    }

    public AuthTokenResponse login(LoginRequest loginRequest) {

        if(cacheService.isCacheExist(LOCK_LOGIN_PREFIX+loginRequest.getUsername())) {
            throw new AppException(ErrorCodeApplication.LOGIN_BLOCKED, ErrorCodeApplication.LOGIN_BLOCKED.getDefaultMessage());
        }

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .filter(u -> u.getPasswordHash() != null)
                .filter(u -> passwordEncoder.matches(loginRequest.getPassword(), u.getPasswordHash()))
                .orElseThrow(() -> {
                    this.handleLoginFail(loginRequest.getUsername());
                    return new AppException(ErrorCodeApplication.AUTHENTICATION_FAIL, ErrorCodeApplication.AUTHENTICATION_FAIL.getDefaultMessage());
                });

        cacheService.deleteCache(LOGIN_FAIL_PREFIX + loginRequest.getUsername());

        return AuthTokenResponse.builder()
                .accessToken(jwtService.createAccessToken(createClaim(user)))
                .refreshToken(jwtService.createRefreshToken(createClaim(user)))
                .build();
    }


    public AuthTokenResponse refreshToken(TokenRequest token) {
        DecodeToken decodeDeToken = jwtService.decodeToken(token.getToken());
        long ttl = decodeDeToken.getExp() - Instant.now().getEpochSecond();
        if (ttl > 0) {
            cacheService.setWithSeconds(REVOKE_PREFIX + decodeDeToken.getJti(), "", ttl);
        }
        User user = userRepository.findById(Long.valueOf(decodeDeToken.getSub())).orElseThrow(
                ()-> new AppException(ErrorCodeApplication.USER_NOT_FOUND)
        );
        return AuthTokenResponse.builder()
                .accessToken(jwtService.createAccessToken(createClaim(user)))
                .refreshToken(jwtService.createRefreshToken(createClaim(user)))
                .build();
    }

    public void logout(TokenRequest token) {
        DecodeToken decodeToken = jwtService.decodeToken(token.getToken());
        long ttl = decodeToken.getExp() - Instant.now().getEpochSecond();
        if (ttl > 0) {
            cacheService.setWithSeconds(REVOKE_PREFIX + decodeToken.getJti(), "", ttl);
        }
    }

    public Map<String, Object> createClaim(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("sub", user.getId());
        claims.put("username", user.getUsername());
        return claims;
    }
}
