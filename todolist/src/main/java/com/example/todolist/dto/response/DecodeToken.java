package com.example.todolist.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DecodeToken {
    String jti;
    String sub;
    String type;
    Long exp;
}
