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
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "etudiantId")
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcours_id", nullable = false)
    @JsonManagedReference
    private Parcours parcours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filiere_id", nullable = false)
    @JsonBackReference
    private Filiere filiere;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "etudiant_filiere",
            joinColumns = @JoinColumn(name = "etudiant_id"),
            inverseJoinColumns = @JoinColumn(name = "filiere_id")
    )
    @JsonIgnore
    private Set<Filiere> filieres = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tuteur_id")
    @JsonManagedReference
    private Tuteur tuteur;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "etudiant_ue",
            joinColumns = @JoinColumn(name = "etudiant_id"),
            inverseJoinColumns = @JoinColumn(name = "ue_id")
    )
    @JsonIgnore // Ignorer complètement les UEs dans la sérialisation
    private List<UE> ue;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Note> notes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_Id")
    @JsonIgnore
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reduction_id")
    @JsonIgnore
    private Reduction reduction ;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Sérialiser les paiements dans Etudiant
    private List<Paiement> paiements;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "echeance_id")
    @JsonIgnore
    private Echeance echeance;

    // Relation avec les échéances
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Echeance> echeances;
}
