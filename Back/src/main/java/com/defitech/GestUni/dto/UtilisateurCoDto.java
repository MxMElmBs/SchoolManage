package com.defitech.GestUni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurCoDto {

    private  Long id;
    private String nom;
    private  String prenom;
}
