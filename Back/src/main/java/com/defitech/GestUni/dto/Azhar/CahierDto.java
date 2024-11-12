package com.defitech.GestUni.dto.Azhar;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CahierDto {
    private Long id;
    private String nomCours;
    private String contenuSeance;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Duration duree;
    private int credit;
}
