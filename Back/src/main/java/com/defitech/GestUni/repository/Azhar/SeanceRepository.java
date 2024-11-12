package com.defitech.GestUni.repository.Azhar;

import com.defitech.GestUni.models.Azhar.Seance;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    @Query("SELECT e FROM Etudiant e JOIN e.filiere f JOIN f.ue u WHERE u.ueId = :ueId")
    List<Etudiant> findEtudiantsByUe_UeId(Long ueId);
    List<Seance> findByUe_UeId(Long ueId);
    Optional<Seance> findFirstByUeAndEndTimeIsNull(UE ue);
    List<Seance> findAllByEndTimeIsNull();
    Optional<Seance> findFirstByUe_UeIdAndEndTimeIsNull(Long ueId);

    @Query("SELECT s FROM Seance s " +
            "JOIN s.ue ue " +
            "JOIN ue.filieres f " +
            "JOIN Etudiant e ON e.filiere.filiereId = f.filiereId " +  // Filtre par filière de l'étudiant
            "WHERE e.etudiantId = :etudiantId " +  // Filtrer par l'étudiantId
            "AND s.endTime IS NULL " +             // Filtrer les séances ouvertes (sans endTime)
            "AND s.cahierTexte IS NULL")           // Filtrer les séances sans cahier de texte
    List<Seance> findOpenSeancesWithoutCahierByEtudiant(Long etudiantId);

    @Query("SELECT COUNT(s) FROM Seance s WHERE s.ue.ueId = :ueId")
    Long countSeancesByUE(Long ueId);

    // Calculer le nombre total d'heures effectuées pour une UE donnée
    @Query("SELECT SUM(s.nombreHeureEffectuee) FROM Seance s WHERE s.ue.ueId = :ueId")
    Integer sumHeuresEffectueesByUE(Long ueId);

    @Query("SELECT SUM(TIMESTAMPDIFF(MINUTE, c.heureDebut, c.heureFin)) / 60 FROM Seance s JOIN s.cahierTexte c WHERE s.ue.ueId = :ueId AND s.date >= :startDate AND s.date <= :endDate")
    Integer findHeuresEffectueesSemaine(@Param("ueId") Long ueId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(TIMESTAMPDIFF(MINUTE, c.heureDebut, c.heureFin)) / 60 FROM Seance s JOIN s.cahierTexte c WHERE s.ue.ueId = :ueId AND s.date >= :startDate AND s.date <= :endDate")
    Integer findHeuresEffectueesMois(@Param("ueId") Long ueId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
