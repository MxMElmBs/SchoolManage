package com.defitech.GestUni.models.Bases;

import com.defitech.GestUni.enums.Etat;
import com.defitech.GestUni.enums.NiveauEtude;
import com.defitech.GestUni.enums.TypeSemestre;
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

    @ManyToOne
    @JoinColumn(name = "professeur_id", nullable = false)
    private Professeur professeur;

    @ManyToMany
    @JoinTable(
            name = "ue_filiere",
            joinColumns = @JoinColumn(name = "ue_id"),
            inverseJoinColumns = @JoinColumn(name = "filiere_id")
    )
    private List<Filiere> filieres;

    @ManyToMany(mappedBy = "ue")
    private List<Etudiant> etudiants = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Etat etat;
}
