package com.defitech.GestUni.dto.Note;
import com.defitech.GestUni.enums.TypeNote;
import com.defitech.GestUni.enums.TypeSemestre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtudiantNoteMaxDto {
    private String ueCode;
    private String ueLibelle;
    private float devoir;
    private float examen;
    private String typeUe;
    private TypeSemestre semestre;

}
