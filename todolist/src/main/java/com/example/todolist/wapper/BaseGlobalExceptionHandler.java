package com.example.todolist.wapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
@Slf4j
public class
BaseGlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(
                ApiResponse.error(
                        status.value(),
                        toErrorCodeName(ex.getCode()),
                        ex.getCode().getDefaultMessage(),
                        req.getRequestURI()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex,
                                                                         WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .orElse("Validation error");

        ApiResponse<Object> body = ApiResponse.builder()
                .statusCode(status.value())
                .timestamp(Instant.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .code("VALIDATION_ERROR")
                .message(message)
                .build();

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                              WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        log.error("Illegal argument exception: {}", ex.getMessage(), ex);

        ApiResponse<Object> body = ApiResponse.builder()
                .statusCode(status.value())
                .timestamp(Instant.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .code("ILLEGAL_ARGUMENT")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleUnexpectedException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        log.error("Unexpected exception occurred: {}", ex.getClass().getName(), ex);
        log.error("Exception message: {}", ex.getMessage());
        if (ex.getCause() != null) {
            log.error("Caused by: {} - {}", ex.getCause().getClass().getName(), ex.getCause().getMessage());
        }

        ApiResponse<Object> body = ApiResponse.builder()
                .statusCode(status.value())
                .timestamp(Instant.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .code("INTERNAL_ERROR")
                .message("An unexpected error occurred")
                .build();

        return new ResponseEntity<>(body, status);
    }

    private String toErrorCodeName(ErrorCode code) {
        if (code instanceof Enum<?> enumCode) {
            return enumCode.name();
        }
        return code.getClass().getSimpleName();
    }
}