package com.example.todolist.wapper;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    int statusCode;
    String code;
    String message;
    Instant timestamp;
    T data;
    String path;

    public static ApiResponse<Void> error(int statusCode,String code, String message, String path) {
        return ApiResponse.<Void>builder()
                .statusCode(statusCode)
                .code(code)
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static ApiResponse<String> error(String message) {
        return ApiResponse.<String>builder()
                .statusCode(500)
                .code("INTERNAL_ERROR")
                .message(message)
                .timestamp(Instant.now())
                .build();
    }


}