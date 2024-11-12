package com.defitech.GestUni.models.Bases;

import com.defitech.GestUni.enums.BEDJRA.MentionBac;
import com.defitech.GestUni.enums.BEDJRA.Statutboursier;
import com.defitech.GestUni.enums.BEDJRA.TypeModalite;
import com.defitech.GestUni.enums.NiveauEtude;
import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.BAKA.Note;
import com.defitech.GestUni.models.BEDJRA.Echeance;
import com.defitech.GestUni.models.BEDJRA.Paiement;
import com.defitech.GestUni.models.BEDJRA.Reduction;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long etudiantId;

    @Column(unique = true)
    private String matricule;

    private String nom;
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;
    private String adresse;
    private String telephone;
    private LocalDate dateNaiss;
    private String lieuNaiss;
    private String nationnalite;
    private String sexe;
    private String serieBac;
    private String anneeBac;
    private String etatProvenance;
    private LocalDate dateIns;
    private String paysBac;
    private String autreDiplome;

    @Enumerated(EnumType.STRING)
    private Statutboursier statutboursier ;

    @Enumerated(EnumType.STRING)
    private NiveauEtude niveauEtude;

    @Enumerated(EnumType.STRING)
    private TypeSemestre typeSemestre;

    @Enumerated(EnumType.STRING)
    private MentionBac mentionBac;

    @Enumerated(EnumType.STRING)
    private TypeModalite typeModalite;


    @ManyToOne
    @JoinColumn(name = "parcours_id", nullable = false)
    private Parcours parcours;

    @ManyToOne
    @JoinColumn(name = "filiere_id", nullable = false)
    private Filiere filiere;

    @ManyToMany
    @JoinTable(
            name = "etudiant_filiere",
            joinColumns = @JoinColumn(name = "etudiant_id"),
            inverseJoinColumns = @JoinColumn(name = "filiere_id")
    )
    private Set<Filiere> filieres = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "tuteur_id")
    private Tuteur tuteur;

    @ManyToMany
    @JoinTable(
            name = "etudiant_ue",
            joinColumns = @JoinColumn(name = "etudiant_id"),
            inverseJoinColumns = @JoinColumn(name = "ue_id")
    )
    private List<UE> ue;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes;

    @OneToOne
    @JoinColumn(name = "utilisateur_Id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "reduction_id")
    private Reduction reduction ;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Paiement> paiements;

    @ManyToOne
    @JoinColumn(name = "echeance_id")
    private Echeance echeance;

    // Relation avec les échéances
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    private List<Echeance> echeances;
}
