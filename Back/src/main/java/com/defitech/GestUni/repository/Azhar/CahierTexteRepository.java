package com.defitech.GestUni.repository.Azhar;

import com.defitech.GestUni.enums.StatutCahier;
import com.defitech.GestUni.models.Azhar.CahierTexte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CahierTexteRepository extends JpaRepository<CahierTexte, Long> {
    Optional<CahierTexte> findBySeance_seanceId(Long seanceId);
    @Query("SELECT ct FROM CahierTexte ct " +
            "JOIN ct.seance s " +
            "JOIN s.ue ue " +
            "WHERE ue.ueId = :ueId")
    List<CahierTexte> findByUeId(Long ueId);

    @Query("SELECT ct FROM CahierTexte ct " +
            "JOIN ct.seance s " +
            "JOIN s.ue ue " +
            "JOIN ue.filieres f " +
            "JOIN Etudiant e ON e.filiere = f " +
            "WHERE e.etudiantId = :etudiantId")
    List<CahierTexte> findCahiersByEtudiantId(Long etudiantId);

    CahierTexte findBySeance_SeanceIdAndStatus(Long seanceId, StatutCahier status);

    Optional<CahierTexte> findBySeance_SeanceId(Long seanceId);
}
