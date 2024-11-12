package com.defitech.GestUni.dto.Note;

import lombok.Data;

@Data
public class EtudiantMoyenneDto {
    private String nom;
    private String prenom;
    private Double moyenne;


    public EtudiantMoyenneDto(String nom, String prenom, Double moyenne) {
        this.nom = nom;
        this.prenom = prenom;
        this.moyenne = moyenne;
    }
}
