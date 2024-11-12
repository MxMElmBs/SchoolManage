package com.defitech.GestUni.dto.Azhar;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class HistoriquePresence {
    private LocalDate seanceDate;
    private String nomCours;
    private List<PresenceDTO> presences;
}
