package com.defitech.GestUni.dto.BEDJRA;

import lombok.Data;

@Data
public class DtoFiliere {
    private Long id;
    private String nomFiliere;

    public String getNomFiliere() {
        return nomFiliere;
    }

    public void setNomFiliere(String nomFiliere) {
        this.nomFiliere = nomFiliere;
    }
}
