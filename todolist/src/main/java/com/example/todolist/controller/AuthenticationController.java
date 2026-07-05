package com.example.todolist.controller;

import com.example.todolist.config.CookieProperties;
import com.example.todolist.config.DefaultAuthCookieWriter;
import com.example.todolist.dto.request.LoginRequest;
import com.example.todolist.dto.request.RegisterRequest;
import com.example.todolist.dto.request.TokenRequest;
import com.example.todolist.dto.response.AuthTokenResponse;
import com.example.todolist.service.AuthenticationService;
import com.example.todolist.service.RegisterService;
import com.example.todolist.wapper.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final DefaultAuthCookieWriter  defaultAuthCookieWriter;
    private final RegisterService registerService;

    @PostMapping
    ApiResponse<AuthTokenResponse> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        AuthTokenResponse authTokenResponse = authenticationService.login(loginRequest);
        defaultAuthCookieWriter.writeTokens(response, authTokenResponse);
        return ApiResponse.ok(authTokenResponse);
    }

    @PostMapping("/register")
    ApiResponse<AuthTokenResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthTokenResponse authTokenResponse = registerService.registerUser(request);
        defaultAuthCookieWriter.writeTokens(response, authTokenResponse);
        return ApiResponse.ok(authTokenResponse);
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@CookieValue(value = CookieProperties.REFRESH_TOKEN, required = false) String token, HttpServletResponse response) {
        defaultAuthCookieWriter.clearTokens(response);
        authenticationService.logout(TokenRequest.builder()
                        .token(token)
                .build());
        return ApiResponse.ok("Logout successful");
    }

    @PostMapping("/refresh")
    ApiResponse<String> refresh(@CookieValue(value = CookieProperties.REFRESH_TOKEN, required = false) String token, HttpServletResponse response) {

        AuthTokenResponse authTokenResponse = authenticationService.refreshToken(TokenRequest.builder()
                        .token(token)
                .build());
        defaultAuthCookieWriter.writeTokens(response, authTokenResponse);
        return ApiResponse.ok("Refresh token successful");
    }
}
