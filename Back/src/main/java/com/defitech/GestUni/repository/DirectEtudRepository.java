package com.defitech.GestUni.repository;

import com.defitech.GestUni.models.Bases.DirecteurEtude;
import com.defitech.GestUni.models.Bases.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DirectEtudRepository extends JpaRepository<DirecteurEtude, Long> {
    @Query("SELECT de FROM DirecteurEtude de WHERE de.utilisateur.idUser IS NULL")
    List<DirecteurEtude> findDirectEtudSansUtilisateur();
}
