package com.defitech.GestUni.models.Azhar;

import com.defitech.GestUni.enums.StatutCahier;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalTime;

@Entity
@Data
public class CahierTexte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seance_id", unique = true)
    private Seance seance;

    private String contenuSeance;

    private LocalTime heureDebut;
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    private StatutCahier status;

    public Duration getDuree() {
        if (heureDebut != null && heureFin != null) {
            if (heureFin.isBefore(heureDebut)) {
                return Duration.between(heureDebut, heureFin.plusHours(24));
            } else {
                return Duration.between(heureDebut, heureFin);
            }
        } else {
            return null;
        }
    }

}

