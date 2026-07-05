package com.example.todolist.service;

import com.example.todolist.dto.request.RegisterRequest;
import com.example.todolist.dto.response.AuthTokenResponse;
import com.example.todolist.entity.User;
import com.example.todolist.exception.ErrorCodeApplication;
import com.example.todolist.repository.UserRepository;
import com.example.todolist.wapper.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService  authenticationService;
    private final JwtService jwtService;

    public AuthTokenResponse registerUser(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new AppException(ErrorCodeApplication.USERNAME_TAKEN);
        }
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new AppException(ErrorCodeApplication.EMAIL_TAKEN);
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .passwordHash(passwordEncoder.encode(registerRequest.getPasswordHash()))
                .build();

        userRepository.save(user);

        return AuthTokenResponse.builder()
                .accessToken(jwtService.createAccessToken(authenticationService.createClaim(user)))
                .refreshToken(jwtService.createRefreshToken(authenticationService.createClaim(user)))
                .build();

    }
}
