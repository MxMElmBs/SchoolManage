package com.defitech.GestUni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtudiantCoursDto {
    private String nomFiliere;
    private String code;
    private String libelleUE;
    private String niveau;
    private int creditUE;
    private String semestre;
    private String typeUe;
    private String description;
}
