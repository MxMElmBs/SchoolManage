package com.defitech.GestUni.dto.Note;

import com.defitech.GestUni.enums.NiveauEtude;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EtudiantjeffDto {
    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String filiereNom;
    private NiveauEtude niveauEtude;
    private LocalDate dateNaiss;
}
