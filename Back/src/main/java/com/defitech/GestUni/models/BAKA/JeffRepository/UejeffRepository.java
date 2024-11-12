package com.defitech.GestUni.models.BAKA.JeffRepository;

import com.defitech.GestUni.enums.NiveauEtude;
import com.defitech.GestUni.enums.TypeNote;
import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.Bases.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UejeffRepository extends JpaRepository<UE, Long> {


    @Query("SELECT ue FROM UE ue WHERE ue.typeSemestre = :typeSemestre")
    List<UE> findByTypeSemestre(@Param("typeSemestre") TypeSemestre typeSemestre);

//    @Query("SELECT ue FROM UE ue JOIN Note n ON n.ue = ue WHERE ue.typeSemestre = :typeSemestre GROUP BY ue")
//    List<UE> findUesWithNotesBySemestre(@Param("typeSemestre") TypeSemestre typeSemestre);

    @Query("SELECT DISTINCT ue FROM UE ue JOIN Note n ON n.ue = ue WHERE n.typeSemestre = :typeSemestre AND n.typeNote = :typeNote")
    List<UE> findUesWithNotesBySemestreAndTypeNote(@Param("typeSemestre") TypeSemestre typeSemestre, @Param("typeNote") TypeNote typeNote);

    @Query("SELECT DISTINCT ue FROM UE ue JOIN Note n ON ue.ueId = n.ue.ueId WHERE ue.typeSemestre = :typeSemestre")
    List<UE> findUesWithNotesBySemestre(@Param("typeSemestre") TypeSemestre typeSemestre);

    // Compter le nombre total d'UE
    @Query("SELECT COUNT(ue) FROM UE ue")
    long countTotalUE();

    // Compter le nombre d'UE par semestre
    @Query("SELECT COUNT(u) FROM UE u WHERE u.typeSemestre = :typeSemestre")
    long countUEParSemestre(@Param("typeSemestre") TypeSemestre typeSemestre);


}
