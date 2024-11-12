package com.defitech.GestUni.dto;

import com.defitech.GestUni.models.Bases.Professeur;
import com.defitech.GestUni.models.Chahib.TypeDocument;
import lombok.Data;

@Data
public class CreateDocumentDto {
    private TypeDocument typeDocument;
    private String theme;
    private Long professeurId; // On utilise l'ID pour identifier le professeur
    private Long etudiantId; // L'ID de l'étudiant
}

