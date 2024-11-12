package com.defitech.GestUni.service.Chahib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesseurDocsDto {
    private Long professeurId;
    private String nom;
    private String prenom;
    private String email;
}
