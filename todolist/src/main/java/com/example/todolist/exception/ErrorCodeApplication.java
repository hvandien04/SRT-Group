package com.example.todolist.exception;

import com.example.todolist.wapper.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ErrorCodeApplication implements ErrorCode {
    AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, 401, "Invalid username or password"),
    LOGIN_BLOCKED(HttpStatus.TOO_MANY_REQUESTS, 429, "Account is temporarily locked"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "User not found"),
    USERNAME_TAKEN(HttpStatus.CONFLICT, 409, "Username already exists"),
    EMAIL_TAKEN(HttpStatus.CONFLICT, 409, "Email already exists"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, 400, "Password does not meet policy"),
    USER_INACTIVE(HttpStatus.FORBIDDEN, 403, "User is inactive"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, 403, "Access denied"),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Role not found"),
    ROLE_ALREADY_EXISTS(HttpStatus.CONFLICT, 409, "Role already exists"),
    PERMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Permission not found"),
    PERMISSION_ALREADY_EXISTS(HttpStatus.CONFLICT, 409, "Permission already exists"),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Task not found"),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, 400, "Invalid task status. Status must be PENDING or COMPLETED");

    private final HttpStatus status;
    private final int code;
    private final String defaultMessage;

    ErrorCodeApplication(HttpStatus status, int code, String defaultMessage) {
        this.status = status;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
