package com.defitech.GestUni.dto.Note;

import lombok.Data;

@Data
public class NoteEtudiantDto {
    private long etudiantId;
    private String matricule;
    private String nom;
    private String prenom;
    private Float valeur;
}
