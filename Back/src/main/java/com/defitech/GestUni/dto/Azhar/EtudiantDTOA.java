package com.defitech.GestUni.dto.Azhar;

import lombok.Data;

@Data
public class EtudiantDTOA {
    private Long etudiantId;
    private String nom;
    private String prenom;
    private String matricule;

    public EtudiantDTOA(Long etudiantId, String nom, String prenom, String matricule) {
        this.etudiantId = etudiantId;
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
    }
}
