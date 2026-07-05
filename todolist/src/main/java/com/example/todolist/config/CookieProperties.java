package com.example.todolist.config;

public class CookieProperties {
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final long DEFAULT_ACCESS_TOKEN_TTL_SECONDS = 3600;
    public static final long DEFAULT_REFRESH_TOKEN_TTL_SECONDS = 604800;
    private CookieProperties(){}
}