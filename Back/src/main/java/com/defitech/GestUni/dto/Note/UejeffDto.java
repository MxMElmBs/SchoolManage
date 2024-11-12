package com.defitech.GestUni.dto.Note;

import com.defitech.GestUni.enums.NiveauEtude;
import com.defitech.GestUni.enums.TypeNote;
import com.defitech.GestUni.enums.TypeSemestre;
import lombok.Data;

import java.util.List;

@Data
public class UejeffDto {
    private Long id;
    private String code;
    private String libelle;
    private String type;
    private String description;
    private int credit;
    private NiveauEtude niveauEtude;
    private TypeSemestre typeSemestre;
    private TypeNote typeNote;
    private Long professeurId; // Id du professeur
    private String professeurName;
    private String professeurPrenom;
    private String professeurEmail;
    private List<Long> filiereIds;

}
