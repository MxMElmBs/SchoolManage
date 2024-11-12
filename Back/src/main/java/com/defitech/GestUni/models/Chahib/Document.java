package com.defitech.GestUni.models.Chahib;

import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.Filiere;
import com.defitech.GestUni.models.Bases.Parcours;
import com.defitech.GestUni.models.Bases.Professeur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String theme;
    private LocalDateTime createdAt;

    @Lob  // Annotation pour supporter de grandes quantités de texte
    @Column(columnDefinition = "LONGTEXT")  // Spécifie explicitement le type LONGTEXT dans MySQL
    private String introduction;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String problematique;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String etude_critique_existant;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String resumeDoc;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String conclusion;

    private String documentUrl;

    @Enumerated(EnumType.STRING)
    private TypeDocument typeDocument;

    @Enumerated(EnumType.STRING)
    private StatutDocument statut;

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    @JsonIgnore  // Éviter la sérialisation du professeur pour éviter le cycle
    private Professeur professeur;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", referencedColumnName = "etudiantId")
    @JsonIgnore  // Éviter la sérialisation de l'étudiant pour éviter le cycle
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "filiere_id")
    @JsonIgnore  // Éviter la sérialisation de la filière
    private Filiere filiere;

    @ManyToOne
    @JoinColumn(name = "parcours_id")
    @JsonIgnore  // Éviter la sérialisation du parcours
    private Parcours parcours;


}
