package com.defitech.GestUni.repository;

import com.defitech.GestUni.models.Bases.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtudiantDocRepository extends JpaRepository<Etudiant, Long> {
    // Méthode pour trouver un étudiant via l'ID utilisateur
    Optional<Etudiant> findByUtilisateurIdUser(Long idUser);
}
