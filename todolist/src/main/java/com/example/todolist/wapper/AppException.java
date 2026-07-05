package com.example.todolist.wapper;

import lombok.Getter;

import java.util.List;

@Getter
public class AppException extends RuntimeException {

    private final ErrorCode code;
    private final List<String> details;

    public AppException(ErrorCode code) {
        super(code.getDefaultMessage());
        this.code = code;
        this.details = List.of();
    }

    public AppException(ErrorCode code, String message) {
        super(message);
        this.code = code;
        this.details = List.of();
    }

    public AppException(ErrorCode code, String message, List<String> details) {
        super(message);
        this.code = code;
        this.details = details == null ? List.of() : details;
    }
}