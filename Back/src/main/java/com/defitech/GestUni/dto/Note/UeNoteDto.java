package com.defitech.GestUni.dto.Note;

import com.defitech.GestUni.enums.TypeSemestre;
import lombok.Data;

@Data
public class UeNoteDto {
    private String codeUe;
    private String libelleUe;
    private String typeUe;
    private int creditUe;
    private float valeurNote;
    private String valide;
    private TypeSemestre semestre;
}
