package com.defitech.GestUni.dto;

import lombok.Data;

@Data
public class EtudiantDetailsDto {
    private String nom;
    private String prenom;
    private String parcours;
    private String filiere;
    private String theme;
    private String typeDocument;
    private String annee;
    private String documentUrl;
    private String professeurNom;
    private String professeurPrenom;
}
