package com.defitech.GestUni.dto.Azhar;

import lombok.Data;

@Data
public class PresenceDTO {
    private Long etudiantId;
    private String nomEtudiant;
    private String prenomEtudiant;
    private String matriculeEtudiant;
    private Boolean present;
    private Boolean enPermission;
}

