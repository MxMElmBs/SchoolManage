package com.defitech.GestUni.models.Bases;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Professeur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long professeurId;

    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    private String prenom;

    @OneToOne
    @JoinColumn(name = "utilisateur_Id")
    @JsonIgnore
    private Utilisateur utilisateur;


//    @OneToMany
//    @JoinColumn(referencedColumnName = "cours_id", insertable = false)
//    private List<UE> ues;


}
