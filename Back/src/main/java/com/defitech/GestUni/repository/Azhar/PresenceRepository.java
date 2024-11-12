package com.defitech.GestUni.repository.Azhar;

import com.defitech.GestUni.models.Azhar.Presence;
import com.defitech.GestUni.models.Azhar.Seance;
import com.defitech.GestUni.models.Bases.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresenceRepository extends JpaRepository<Presence, Long> {

    /**
     * Trouve la présence d'un étudiant pour une séance spécifique.
     *
     * @param etudiantId L'ID de l'étudiant.
     * @param seanceId L'ID de la séance.
     * @return Un Optional contenant la présence si elle existe, sinon un Optional vide.
     */
    Optional<Presence> findByEtudiant_EtudiantIdAndSeance_SeanceId(Long etudiantId, Long seanceId);

    /**
     * Trouve l'étudiant avec le plus d'absences pour une unité d'enseignement (UE) donnée.
     *
     * @param ueId L'ID de l'UE.
     * @return Une liste d'objets contenant l'étudiant et le nombre d'absences, triée par nombre d'absences décroissant.
     */
    @Query("SELECT p.etudiant, COUNT(p.present) AS nbAbsences " +
            "FROM Presence p " +
            "JOIN p.seance s JOIN s.ue ue " +
            "WHERE ue.ueId = :ueId AND p.present = FALSE " +
            "GROUP BY p.etudiant " +
            "ORDER BY nbAbsences DESC")
    List<Object[]> findMostAbsentStudentByUE(Long ueId);

    /**
     * Calcule le taux de présence de chaque étudiant pour une unité d'enseignement (UE) donnée.
     *
     * @param ueId L'ID de l'UE.
     * @return Une liste d'objets contenant l'étudiant et son taux de présence (en pourcentage).
     */
    @Query("SELECT p.etudiant, " +
            "(SUM(CASE WHEN p.present = TRUE THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) AS tauxPresence " +
            "FROM Presence p " +
            "JOIN p.seance s JOIN s.ue ue " +
            "WHERE ue.ueId = :ueId " +
            "GROUP BY p.etudiant")
    List<Object[]> findAttendanceRateForStudentsByUE(Long ueId);

    /**
     * Calcule le taux de présence pour chaque étudiant d'un parcours donné, et optionnellement pour une UE spécifique.
     *
     * @param parcoursId L'ID du parcours.
     * @param ueId Optionnel : l'ID de l'UE si l'on souhaite restreindre les résultats à une UE particulière.
     * @return Une liste d'objets contenant l'étudiant et son taux de présence (en pourcentage).
     */
    @Query("SELECT e, " +
            "(SUM(CASE WHEN p.present = TRUE THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) AS tauxPresence " +
            "FROM Presence p " +
            "JOIN p.seance s JOIN s.ue ue " +
            "JOIN p.etudiant e JOIN e.parcours parcours " +
            "WHERE (:ueId IS NULL OR ue.ueId = :ueId) AND parcours.parcoursId = :parcoursId " +
            "GROUP BY e")
    List<Object[]> findPresenceRateForStudentsByParcoursAndOptionallyUE(Long parcoursId, Long ueId);

    /**
     * Calcule le taux d'absence pour une unité d'enseignement (UE) donnée.
     *
     * @param ueId L'ID de l'UE.
     * @return Le taux d'absence (en pourcentage) pour l'UE.
     */
    @Query("SELECT (SUM(CASE WHEN p.present = FALSE THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) AS tauxAbsence " +
            "FROM Presence p " +
            "JOIN p.seance s JOIN s.ue ue " +
            "WHERE ue.ueId = :ueId")
    Double findAbsenceRateForUE(Long ueId);

    List<Presence> findByEtudiantAndPresentFalse(Etudiant etudiant);

    @Query("SELECT p FROM Presence p WHERE p.seance.ue.ueId = :ueId")
    List<Presence> findBySeanceIn(@Param("ueId") Long ueId);
    @Query("SELECT p.etudiant.etudiantId, p.etudiant.nom, p.etudiant.prenom, p.etudiant.matricule, " +
            "COUNT(p) AS totalSeances, " +
            "SUM(CASE WHEN p.present = true OR p.enPermission = true THEN 1 ELSE 0 END) AS presencesEffectives " +
            "FROM Presence p WHERE p.seance.ue.ueId = :ueId GROUP BY p.etudiant")
    List<Object[]> calculerTauxPresenceParUE(@Param("ueId") Long ueId);
    int countByEtudiantAndPresentFalseAndEnPermissionFalse(Etudiant etudiant);
}
