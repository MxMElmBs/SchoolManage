package com.defitech.GestUni.repository;

import com.defitech.GestUni.models.Bases.Parcours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParcoursRepository extends JpaRepository<Parcours, Long> {
    @Query("SELECT p FROM Parcours p WHERE p.nomParcours = :nom")
    Parcours findByNomParcour(@Param("nom") String nom);

    Optional<Parcours> findByNomParcours(String nomParcours);
}
