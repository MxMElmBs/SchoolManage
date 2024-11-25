package com.defitech.GestUni.models.Bases;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcours_id", nullable = false)
    private Parcours parcours;

    @ManyToMany(mappedBy = "filieres", fetch = FetchType.LAZY)
    private List<UE> ue;

    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Permet la sérialisation dans Etudiant
    private List<Etudiant> etudiants;


}
