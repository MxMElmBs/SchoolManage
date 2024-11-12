package com.defitech.GestUni.models.Bases;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Tuteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String profession;
    private String organismeEmployeur;
    private String adresse;
    private String telBureau;
    private String telDom;
    private String cel;
    private String email;

}
