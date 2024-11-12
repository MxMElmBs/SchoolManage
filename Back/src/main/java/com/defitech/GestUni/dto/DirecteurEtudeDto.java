package com.defitech.GestUni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirecteurEtudeDto {
    private Long directeurId;
    private Long parcoursId;
    private String nomDirecteurEtude;
    private String prenomDirecteurEtude;
    private String emailDirecteurEtude;
    private String parcoursDirecteurEtude;
}
