package com.defitech.GestUni.models.Bases;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Parcours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parcoursId;

    private long montantScolarite;

    @Column(nullable = false, unique = true)
    private String nomParcours;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "de_Id")
    private DirecteurEtude directeurEtude;
}
