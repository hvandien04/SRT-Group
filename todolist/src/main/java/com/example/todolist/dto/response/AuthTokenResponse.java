package com.example.todolist.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthTokenResponse {
    private String accessToken;
    private String refreshToken;
}
