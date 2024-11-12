package com.defitech.GestUni.models.Bases;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OtherUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otherUserId;

    private String nom;

    private String prenom;
    @Column(nullable = false, unique = true)
    private String email;


    @OneToOne
    @JoinColumn(name = "utilisateur_Id")
    private Utilisateur utilisateur;

}
