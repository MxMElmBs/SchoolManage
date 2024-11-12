package com.defitech.GestUni.dto.Note;

import com.defitech.GestUni.enums.TypeNote;
import lombok.Data;

import java.util.List;

@Data
public class EnregistrementNoteDto {

    private Long ueId;
    private TypeNote typeNote;
    private List<EtudiantNoteDto> etudiants;
}
