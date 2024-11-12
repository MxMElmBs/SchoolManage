package com.defitech.GestUni.dto.BEDJRA;

import lombok.Data;

import java.util.List;

@Data
public class RappelEcheanceDto {
    private Long etudiantId;
    private String etudiantNom;
    private String etudiantPrenom;
    private List<EcheanceDto> echeances;
    private String tuteurMail;
}
