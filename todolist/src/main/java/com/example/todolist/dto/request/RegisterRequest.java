package com.example.todolist.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "is required")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "is required")
    @Size(min = 6, max = 100)
    @JsonAlias("passwordHash")
    private String password;

    @NotBlank(message = "is required")
    @Size(max = 100)
    @Email
    private String email;
}
