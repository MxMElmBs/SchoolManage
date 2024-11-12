package com.defitech.GestUni.dto.Azhar;

import com.defitech.GestUni.enums.PermissionStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PermissionDTO {
    private Long permissionId;

    // Informations de la permission
    private Long etudiantId;
    private String nom;
    private String prenom;
    private String email;
    private String matricule;

    // Informations de la permission
    private LocalDate dateDebutAbsence;
    private LocalDate dateFinAbsence;
    private LocalDate dateDemande;
    private String remarque;
    private PermissionStatus status;
    private String raison;
    private String raisonR;
    private Long duree;
    private String description;
    private String fileUrl;

    public PermissionDTO(Long permissionId, String nom, String prenom, String matricule, LocalDate dateDebutAbsence,
                         LocalDate dateDemande, LocalDate dateFinAbsence, String description, String remarque, PermissionStatus status) {
        this.permissionId = permissionId;
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.dateDebutAbsence = dateDebutAbsence;
        this.dateFinAbsence = dateFinAbsence;
        this.dateDemande = dateDemande;
        this.description = description;
        this.remarque = remarque;
        this.status = status;
    }

// Constructeur avec tous les paramètres
public PermissionDTO() {
    this.permissionId = permissionId;
    this.etudiantId = etudiantId;
    this.nom = nom;
    this.prenom = prenom;
    this.email = email;
    this.matricule = matricule;
    this.dateDebutAbsence = dateDebutAbsence;
    this.dateFinAbsence = dateFinAbsence;
    this.dateDemande = dateDemande;
    this.remarque = remarque;
    this.status = status;
    this.raison = raison;
    this.duree = duree;
    this.description = description;
    this.fileUrl = fileUrl;
    this.raisonR = raisonR;
}
}
