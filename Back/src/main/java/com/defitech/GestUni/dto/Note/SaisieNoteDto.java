package com.defitech.GestUni.dto.Note;

import com.defitech.GestUni.enums.TypeNote;
import lombok.Data;

@Data
public class SaisieNoteDto {
    private Long etudiantId;
    private Long ueId;
    private TypeNote typeNote;
    private float valeurNote;
}
