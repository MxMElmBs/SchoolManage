package com.defitech.GestUni.models.Azhar;

import com.defitech.GestUni.models.Bases.Etudiant;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "presence")
public class Presence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long presenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seance_id")
    private Seance seance;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    private Boolean present;
    private Boolean enPermission;
}
