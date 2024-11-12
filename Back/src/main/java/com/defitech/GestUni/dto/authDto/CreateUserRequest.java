package com.defitech.GestUni.dto.authDto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String nom;
    private String prenom;
    private String tel;
    private String email;
}
