package com.defitech.GestUni.repository.Azhar;

import com.defitech.GestUni.dto.Azhar.PermissionDTO;
import com.defitech.GestUni.enums.PermissionStatus;
import com.defitech.GestUni.models.Azhar.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByStatus(PermissionStatus status);


    @Query("SELECT p FROM Permission p WHERE p.etudiant.parcours.parcoursId = :parcoursId")
    List<Permission> findAllByParcoursId(Long parcoursId);

    List<Permission> findByEtudiant_EtudiantId(Long etudiantId);

    @Query("SELECT new com.defitech.GestUni.dto.Azhar.PermissionDTO(p.permissionId, e.nom, e.prenom, e.matricule, p.dateDebutAbsence, " +
            "p.dateDemande, p.dateFinAbsence, p.description, p.remarque, p.status) " +
            "FROM Permission p JOIN p.etudiant e WHERE e.filiere.id = :filiereId")
    List<PermissionDTO> findByFiliereId(@Param("filiereId") Long filiereId);

    @Query("SELECT new com.defitech.GestUni.dto.Azhar.PermissionDTO(p.permissionId, e.nom, e.prenom, e.matricule, p.dateDebutAbsence, " +
            "p.dateDemande, p.dateFinAbsence, p.description, p.remarque, p.status) " +
            "FROM Permission p JOIN p.etudiant e JOIN e.filiere f JOIN f.parcours parc WHERE parc.id = :parcoursId")
    List<PermissionDTO> findByParcoursId(@Param("parcoursId") Long parcoursId);

    @Query("SELECT new com.defitech.GestUni.dto.Azhar.PermissionDTO(p.permissionId, e.nom, e.prenom, e.matricule, p.dateDebutAbsence, " +
            "p.dateDemande, p.dateFinAbsence, p.description, p.remarque, p.status) " +
            "FROM Permission p JOIN p.etudiant e JOIN e.filiere f JOIN f.parcours parc WHERE parc.id = :parcoursId AND p.status = :status")
    List<PermissionDTO> findByParcoursIdAndStatus(@Param("parcoursId") Long parcoursId, @Param("status") PermissionStatus status);

    @Query("SELECT new com.defitech.GestUni.dto.Azhar.PermissionDTO(p.permissionId, e.nom, e.prenom, e.matricule, p.dateDebutAbsence, " +
            "p.dateDemande, p.dateFinAbsence, p.description, p.remarque, p.status) " +
            "FROM Permission p JOIN p.etudiant e WHERE e.filiere.id = :filiereId AND p.status = :status")
    List<PermissionDTO> findByFiliereIdAndStatus(@Param("filiereId") Long filiereId, @Param("status") PermissionStatus status);

    @Query("SELECT new com.defitech.GestUni.dto.Azhar.PermissionDTO(p.permissionId, e.nom, e.prenom, e.matricule, p.dateDebutAbsence, " +
            "p.dateDemande, p.dateFinAbsence, p.description, p.remarque, p.status) " +
            "FROM Permission p JOIN p.etudiant e WHERE e.filiere.id = :filiereId AND p.status = :status AND p.dateDebutAbsence BETWEEN :startDate AND :endDate")
    List<PermissionDTO> findByFiliereIdAndStatusAndPeriod(@Param("filiereId") Long filiereId, @Param("status") PermissionStatus status, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

//    @Query("SELECT new com.defitech.GestUni.dto.Azhar.PermissionDTO(p.permissionId, e.nom, e.prenom, e.matricule, p.dateDebutAbsence, " +
//            "p.dateDemande, p.dateFinAbsence, p.description, p.remarks, p.status) " +
//            "FROM Permission p JOIN p.etudiant e JOIN e.filiere f JOIN f.parcours parc WHERE parc.id = :parcoursId AND p.status = :status AND p.dateDebutAbsence BETWEEN :startDate AND :endDate")
//    List<PermissionDTO> findByParcoursIdAndStatusAndPeriod(@Param("parcoursId") Long parcoursId, @Param("status") PermissionStatus status, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
//
    @Query("SELECT p FROM Permission p WHERE p.status = :status AND p.dateDebutAbsence = :dateDebutAbsence")
    List<Permission> findPermissionsByStatusAndDateDebutAbsence(@Param("status") PermissionStatus status, @Param("dateDebutAbsence") LocalDate dateDebutAbsence);

    Permission findByEtudiant_EtudiantIdAndDateDebutAbsenceLessThanEqualAndDateFinAbsenceGreaterThanEqual(
            Long etudiantId, LocalDate dateDebut, LocalDate dateFin);

//  List<Permission> findByEtudiant_EtudiantId(Long etudiantId);


    List<Permission> findPermissionsByStatusAndDateDebutAbsenceBefore(PermissionStatus status, LocalDate date);
    List<Permission> findPermissionsByStatusAndDateFinAbsenceBefore(PermissionStatus status, LocalDate date);

}
