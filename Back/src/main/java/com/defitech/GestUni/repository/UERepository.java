package com.defitech.GestUni.repository;

import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.Filiere;
import com.defitech.GestUni.models.Bases.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UERepository extends JpaRepository<UE, Long> {

    List<UE> findByFilieres_filiereId(Long filiereId);
    List<UE> findByProfesseur_ProfesseurId(Long professeurId);


    List<UE> findByFilieres_Parcours_ParcoursId(Long parcoursId);
    @Query("SELECT u FROM UE u JOIN u.filieres f WHERE f.filiereId = :filiereId AND u.typeSemestre = :semestre")
    List<UE> findByFiliereAndSemestre(@Param("filiereId") Filiere filiereId, @Param("semestre") TypeSemestre semestre);

    @Query("SELECT u FROM UE u WHERE :filiere member of u.filieres")
    List<UE> findByFilieres(@Param("filiere") Filiere filiere);

    @Query("SELECT u FROM UE u JOIN u.filieres f JOIN u.etudiants e WHERE e.etudiantId = :etudiantId AND f.filiereId = :filiereId ORDER BY u.typeSemestre, u.typeUe")
    List<UE> findByEtudiantAndFiliere(@Param("etudiantId") Long etudiantId, @Param("filiereId") Long filiereId);

    @Query("SELECT u FROM UE u JOIN u.filieres f WHERE f.filiereId = :filiereId AND u.typeSemestre = :typeSemestre ORDER BY u.typeUe")
    List<UE> findByEtudiantAndFiliereAndSemestre(@Param("filiereId") Long filiereId, @Param("typeSemestre") TypeSemestre typeSemestre);

    @Query("SELECT u FROM UE u JOIN u.filieres f WHERE f.filiereId = :filiereId ORDER BY u.typeSemestre, u.typeUe")
    List<UE> findByFiliere(@Param("filiereId") Long filiereId);


//    @Query("SELECT e FROM Etudiant e JOIN e.ue u WHERE u.ueId = :ueId AND u.typeSemestre = :semestre")
//    List<Etudiant> findByUe_ueIdAndSemestre(@Param("ueId") Long ueId, @Param("semestre") TypeSemestre semestre);
}
