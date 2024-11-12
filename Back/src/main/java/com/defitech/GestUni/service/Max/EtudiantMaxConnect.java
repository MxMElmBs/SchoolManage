package com.defitech.GestUni.service.Max;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtudiantMaxConnect {
    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String lieuNaiss;
    private String sexe;
    private String dob;
    private String parcours;
    private String niveau;
}
