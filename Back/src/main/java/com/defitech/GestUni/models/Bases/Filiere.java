package com.defitech.GestUni.models.Bases;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "filieres")
public class Filiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filiereId;

    @Column(nullable = false, unique = true)
    private String nomFiliere;
    private String description;

    @ManyToOne
    @JoinColumn(name = "parcours_id", nullable = false)
    private Parcours parcours;

    @ManyToMany(mappedBy = "filieres")
    private List<UE> ue;

    @ManyToMany(mappedBy = "filieres")
    private Set<Etudiant> etudiants = new HashSet<>();


}
