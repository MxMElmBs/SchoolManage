package com.defitech.GestUni.dto.Azhar;

import lombok.Data;

@Data
public class TauxPresence {
    private Long etudiantId;
    private String matricule;
    private String nom;
    private String prenom;
    private Double attendanceRate;
}

