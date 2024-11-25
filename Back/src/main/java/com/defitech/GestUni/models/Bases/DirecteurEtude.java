package com.defitech.GestUni.models.Bases;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DirecteurEtude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long directeurEtudeId;

    private String directeurEtudeNom;
    private String directeurEtudePrenom;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcours_Id")
    private Parcours parcours;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_Id")
    private Utilisateur utilisateur;
}
