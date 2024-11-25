package com.defitech.GestUni.models.Azhar;

import com.defitech.GestUni.enums.PermissionStatus;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Entity
@Table
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", referencedColumnName = "etudiantId")
    private Etudiant etudiant;

    private LocalDate dateDemande;
    private LocalDate dateDebutAbsence;
    private LocalDate dateFinAbsence;
    private String raison;
    private String raisonR;
    private String description;
    private Long duree;

    private String fileUrl;

    @Column(length = 500)
    private String remarque;

    @Enumerated(EnumType.STRING)
    private PermissionStatus status;

    // Getter personnalisé pour la durée
    public Integer getDuree() {
        if (dateDebutAbsence != null && dateFinAbsence != null) {
            return (int) ChronoUnit.DAYS.between(dateDebutAbsence, dateFinAbsence);
        }
        return null;
    }
}
