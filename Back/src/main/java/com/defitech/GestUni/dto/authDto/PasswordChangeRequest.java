package com.defitech.GestUni.dto.authDto;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String email;
    private String confirmationCode;
    private String newPassword;
}
