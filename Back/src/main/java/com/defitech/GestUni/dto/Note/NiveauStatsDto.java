package com.defitech.GestUni.dto.Note;

import com.defitech.GestUni.enums.NiveauEtude;
import lombok.Data;

@Data
public class NiveauStatsDto {
    private NiveauEtude niveau;
    private Long nbEtudiants;
    private Double moyenneGenerale;
    private Double tauxReussite;

    public NiveauStatsDto(NiveauEtude niveau, Long nbEtudiants, Double moyenneGenerale, Double tauxReussite) {
        this.niveau = niveau;
        this.nbEtudiants = nbEtudiants;
        this.moyenneGenerale = moyenneGenerale;
        this.tauxReussite = tauxReussite;
    }
}
