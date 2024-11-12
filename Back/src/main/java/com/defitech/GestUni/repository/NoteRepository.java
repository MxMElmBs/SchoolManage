package com.defitech.GestUni.repository;

import com.defitech.GestUni.enums.TypeNote;
import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.BAKA.Note;
import com.defitech.GestUni.models.Bases.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n WHERE n.etudiant.etudiantId = :etudiantId AND n.ue.ueId = :ueId")
    Optional<Note> findNoteByEtudiantAndUe(@Param("etudiantId") Long etudiantId, @Param("ueId") Long ueId);

    @Query("SELECT n FROM Note n WHERE n.etudiant.etudiantId = :etudiantId")
    List<Note> findByEtudiantId(@Param("etudiantId") Long etudiantId);

    boolean existsByUeUeIdAndTypeNote(Long ueId, TypeNote typeNote);

    @Query("SELECT n FROM Note n WHERE n.ue.ueId = :ueId AND n.typeNote = :typeNote")
    List<Note> findByUeAndTypeNote(@Param("ueId") Long ueId, @Param("typeNote") TypeNote typeNote);

    @Query("SELECT n FROM Note n WHERE n.etudiant.etudiantId = :etudiantId AND n.ue.ueId = :ueId AND n.typeNote = :typeNote")
    Note findByEtudiantAndUeAndTypeNote(@Param("etudiantId") Long etudiantId, @Param("ueId") Long ueId, @Param("typeNote") TypeNote typeNote);

    List<Note> findByUe_UeIdAndTypeNote(Long ueId, TypeNote typeNote);

    // Vérifier si une UE a des notes d'un certain type (DEVOIR ou EXAMEN)
    boolean existsByUe_UeIdAndTypeNote(Long ueId, TypeNote typeNote);

    // Récupérer toutes les notes d'un semestre et type de note
    List<Note> findByUe_TypeSemestreAndTypeNote(TypeSemestre typeSemestre, TypeNote typeNote);

    List<Note> findByEtudiantAndTypeSemestre(Etudiant etudiant, TypeSemestre typeSemestre);

    List<Note> findByUe_UeId(Long ueId);



    @Query("SELECT n1 FROM Note n1 " +
            "JOIN n1.etudiant e " +
            "JOIN n1.ue ue " +
            "JOIN e.filiere f " +
            "WHERE e.etudiantId = :etudiantId " +
            "AND f.filiereId = :filiereId " +
            "ORDER BY n1.ue.typeSemestre, n1.ue.libelle")
    List<Note> findNotesByEtudiantIdAndFiliere(@Param("etudiantId") Long etudiantId,
                                               @Param("filiereId") Long filiereId);





//    List<Note> findByEtudiant_EtudiantId(Long etudiantId);  // Pour récupérer les notes d'un étudiant
//    List<Note> findByUe_UeId(Long ueId);  // Pour récupérer les notes d'une UE

}
//    @Query("SELECT DISTINCT n.ue FROM Note n WHERE n.ue.typeSemestre = :semestre")
//    List<UE> findUeNotedBySemestre(@Param("semestre") TypeSemestre semestre);
//
//    @Query("SELECT n.ue, n.ue.typeSemestre, n.valeur FROM Note n WHERE n.etudiant.etudiantId = :etudiantId")
//    List<Object[]> findUeAndSemestreAndNoteByEtudiant(@Param("etudiantId") Long etudiantId);
//
//    List<Note> findByEtudiant_EtudiantId(Long etudiantId);
//
//    @Query("SELECT DISTINCT n.ue FROM Note n WHERE n.ue IS NOT NULL")
//    List<UE> findDistinctUeWithNotes();
