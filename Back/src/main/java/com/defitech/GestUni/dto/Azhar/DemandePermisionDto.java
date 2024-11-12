package com.defitech.GestUni.dto.Azhar;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DemandePermisionDto {
    private Long etudiantId;
    private LocalDate dateDebutAbsence;
    private LocalDate dateFinAbsence;
    private LocalDate dateDemande;
    private String raison;
    private String description;
    private String fileUrl;
}

