package com.defitech.GestUni.models.BAKA;

import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.Bases.AnneeScolaire;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.enums.TypeNote;
import com.defitech.GestUni.models.Bases.UE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;
    private float valeur;
    @Enumerated(EnumType.STRING)
    private TypeNote typeNote;
    @Enumerated(EnumType.STRING)
    private TypeSemestre typeSemestre;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    @JsonIgnore
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ue_id", nullable = false)
    @JsonIgnore
    private UE ue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annee_id")
    @JsonIgnore
    private AnneeScolaire anneescolaire;


}
