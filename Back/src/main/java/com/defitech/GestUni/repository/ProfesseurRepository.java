package com.defitech.GestUni.repository;

import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
    @Query("SELECT prof FROM Professeur prof WHERE prof.utilisateur IS NULL")
    List<Professeur> findProfSansUtilisateur();
}
