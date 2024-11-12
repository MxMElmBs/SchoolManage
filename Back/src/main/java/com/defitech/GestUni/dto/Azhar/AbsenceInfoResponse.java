package com.defitech.GestUni.dto.Azhar;

import lombok.Data;

@Data
public class AbsenceInfoResponse {
    private Double tauxAbsence;
    private TauxPresence mostAbsentStudent;
    private Long nombreSeances;

    // Constructeur attendu
    public AbsenceInfoResponse(Double tauxAbsence, TauxPresence mostAbsentStudent, Long nombreSeances) {
        this.tauxAbsence = tauxAbsence;
        this.mostAbsentStudent = mostAbsentStudent;
        this.nombreSeances = nombreSeances;
    }
}
