package com.defitech.GestUni.dto.Note;

import com.defitech.GestUni.enums.TypeNote;
import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.UE;
import lombok.Data;

@Data
public class NoteDto {
    private long id;
    private long etudiantId;
    private String etudiantNom;
    private String etudiantPrenom;
    private Long ueId;
    private String ueCode;
    private String ueLibelle;
    private int ueCredit;
    private float valeur;
    private TypeNote typeNote;
    private TypeSemestre semestre;

}
