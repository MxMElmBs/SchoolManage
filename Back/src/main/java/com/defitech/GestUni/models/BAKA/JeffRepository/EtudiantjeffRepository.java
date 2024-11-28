package com.defitech.GestUni.models.BAKA.JeffRepository;

import com.defitech.GestUni.enums.NiveauEtude;
import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.Bases.Etudiant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantjeffRepository extends JpaRepository<Etudiant, Long> {

//    @Query("SELECT e FROM Etudiant e JOIN e.ue ue WHERE ue.typeSemestre = :typeSemestre")
//    List<Etudiant> findEtudiantsByTypeSemestre(@Param("typeSemestre") TypeSemestre typeSemestre);

      @Query("SELECT e FROM Etudiant e " +
              "LEFT JOIN FETCH e.parcours " +
              "LEFT JOIN FETCH e.filieres " +
              "LEFT JOIN FETCH e.ue " +
              "LEFT JOIN FETCH e.notes " +
              "LEFT JOIN FETCH e.paiements " +
              "WHERE e.etudiantId = :id")
      Etudiant findEtudiantWithRelations(@Param("id") Long id);

      List<Etudiant> findEtudiantsByTypeSemestre(TypeSemestre typeSemestre);
      @Query("SELECT DISTINCT n.etudiant FROM Note n")
      List<Etudiant> findEtudiantsWithNotes();
      List<Etudiant> findByNiveauEtude(NiveauEtude niveauEtude);

      @Query("SELECT COUNT(e) FROM Etudiant e")
      long countTotalEtudiants();

      @Query("SELECT COUNT(e) FROM Etudiant e WHERE e.niveauEtude = :niveauEtude")
      long countEtudiantsParNiveau(@Param("niveauEtude") NiveauEtude niveauEtude);

      @Query("SELECT e, AVG(n.valeur) as moyenne " +
              "FROM Etudiant e " +
              "JOIN Note n ON n.etudiant.etudiantId = e.etudiantId " +
              "GROUP BY e.etudiantId " +
              "ORDER BY moyenne DESC")
      List<Object[]> findTop5EtudiantsByMoyenne(Pageable pageable);

      @Query("SELECT e.niveauEtude, COUNT(e), AVG((SELECT AVG(n.valeur) FROM Note n WHERE n.etudiant = e))," +
              " SUM(CASE WHEN (SELECT AVG(n.valeur) FROM Note n WHERE n.etudiant = e) >= 10 THEN 1 ELSE 0 END) * 1.0 / COUNT(e) " +
              "FROM Etudiant e WHERE e.niveauEtude IN :niveaux GROUP BY e.niveauEtude")
      List<Object[]> findStatsParNiveau(@Param("niveaux") List<NiveauEtude> niveaux);
}
