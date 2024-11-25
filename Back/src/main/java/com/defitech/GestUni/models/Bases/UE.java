package com.defitech.GestUni.models.Bases;

import com.defitech.GestUni.enums.Etat;
import com.defitech.GestUni.enums.NiveauEtude;
import com.defitech.GestUni.enums.TypeSemestre;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class UE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ueId;

    private String libelle;

    private String code;

    private String descriptUe;

    private int credit;

    private String typeUe;

    private int quotaSemaine;

    private Integer heureEffectuer;

    @Enumerated(EnumType.STRING)
    private NiveauEtude niveauEtude;

    @Enumerated(EnumType.STRING)
    private TypeSemestre typeSemestre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professeur_id", nullable = false)
    private Professeur professeur;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ue_filiere",
            joinColumns = @JoinColumn(name = "ue_id"),
            inverseJoinColumns = @JoinColumn(name = "filiere_id")
    )
    @JsonBackReference // Pour rompre la boucle entre UE et Filier
    private List<Filiere> filieres;

    @ManyToMany(mappedBy = "ue", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Etudiant> etudiants = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Etat etat;
}
