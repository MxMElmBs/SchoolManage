package com.defitech.GestUni.dto.authDto;

import lombok.Data;

@Data
public class AuthResponse {
    private Long userId;
    private String jwt;
    private String refreshToken;
    private long expiresAt;
    private String username;
    private String role;
    private String message;
}
