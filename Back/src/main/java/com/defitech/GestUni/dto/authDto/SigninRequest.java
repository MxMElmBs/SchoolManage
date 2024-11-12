package com.defitech.GestUni.dto.authDto;

import lombok.Data;

@Data
public class SigninRequest {
    private String username;
    private String password;
    private String role;
}
