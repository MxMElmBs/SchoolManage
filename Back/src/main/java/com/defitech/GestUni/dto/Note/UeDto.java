package com.defitech.GestUni.dto.Note;

import com.defitech.GestUni.dto.FiliereDto;
import com.defitech.GestUni.dto.ProfesseurDto;
import lombok.Data;

import java.util.List;
@Data
public class UeDto {
    private String libelle;
    private String code;
    private int credit;
    private int quotaSemaine;
    private String descriptUe;
    private ProfesseurDto professeur;
    private List<FiliereDto> filieres;
    private String niveauEtude;
    private String typeSemestre;
    private String etat;
    private int heureEffectuer;
}
