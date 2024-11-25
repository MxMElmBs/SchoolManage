package com.defitech.GestUni.models.Azhar;

import com.defitech.GestUni.models.Bases.UE;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ue_id")
    @JsonBackReference
    private UE ue;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer nombreHeureEffectuee;

    @OneToMany(mappedBy = "seance")
    @JsonIgnore
    private List<Presence> presences;

    @OneToOne(mappedBy = "seance")
    @JsonIgnore
    private CahierTexte cahierTexte;

//
//    public long getDurationInHours() {
//        return Duration.between(startTime, endTime).toHours();
//    }
}
