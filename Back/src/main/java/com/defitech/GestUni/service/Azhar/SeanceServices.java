package com.defitech.GestUni.service.Azhar;

import com.defitech.GestUni.config.Azhar.ResourceNotFoundException;
import com.defitech.GestUni.dto.Azhar.CreateSeanceRequest;
import com.defitech.GestUni.dto.Azhar.SeanceDto;
import com.defitech.GestUni.models.Azhar.Seance;
import com.defitech.GestUni.models.Bases.UE;
import com.defitech.GestUni.repository.Azhar.SeanceRepository;
import com.defitech.GestUni.repository.UERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class SeanceServices {
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private UERepository ueRepository;

    // Créer une nouvelle séance
    public Seance createSeance(Long ueId) {
        Optional<UE> ueOptional = ueRepository.findById(ueId);
        if (!ueOptional.isPresent()) {
            throw new IllegalArgumentException("UE with id " + ueId + " not found");
        }

        UE ue = ueOptional.get();
        Optional<Seance> seanceEnCours = seanceRepository.findFirstByUeAndEndTimeIsNull(ue);
        if (seanceEnCours.isPresent()) {
            throw new IllegalStateException("Une séance est déjà en cours pour ce cours. Veuillez la fermer avant d'en créer une nouvelle.");
        }

        // Créer une nouvelle séance si aucune séance n'est en cours
        Seance seance = new Seance();
        seance.setUe(ue);
        seance.setDate(LocalDate.now());
        seance.setStartTime(LocalTime.now());

        return seanceRepository.save(seance);
    }

    public boolean isSeanceFermee(Long seanceId) {
        Optional<Seance> seanceOpt = seanceRepository.findById(seanceId);
        if (seanceOpt.isPresent()) {
            Seance seance = seanceOpt.get();
            LocalDateTime startTime = LocalDateTime.of(seance.getDate(), seance.getStartTime());
            LocalDateTime now = LocalDateTime.now();
            return Duration.between(startTime, now).toHours() >= 12 || seance.getEndTime() != null;
        }
        return false;
    }

    @Scheduled(fixedRate = 3600000)
    public void checkAndCloseSeances() {
        fermerSeancesAutomatiquement();
    }

    // Méthode pour fermer une séance automatiquement après 12 heures
    public void fermerSeancesAutomatiquement() {
        List<Seance> seancesOuvertes = seanceRepository.findAllByEndTimeIsNull();

        for (Seance seance : seancesOuvertes) {
            LocalDateTime startTime = LocalDateTime.of(seance.getDate(), seance.getStartTime());
            LocalDateTime now = LocalDateTime.now();

            // Calculer la durée depuis l'ouverture de la séance
            Duration duration = Duration.between(startTime, now);
            if (duration.toHours() >= 12) {
                // Fermer la séance
                seance.setEndTime(LocalTime.now());
                seance.setNombreHeureEffectuee(Duration.between(seance.getStartTime(), seance.getEndTime()).toHoursPart());
                seanceRepository.save(seance);
                System.out.println("Séance fermée automatiquement : " + seance.getSeanceId());
            }
        }
    }

    public Seance updateSeance(Long id, SeanceDto seanceDto){
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La seance avec l'ID : " +id+ " n'as pas été trouvé"));
        seance.setStartTime(seanceDto.getStartTime());
        seance.setEndTime(seanceDto.getEndTime());
        final Seance updateSeance = seanceRepository.save(seance);
        return updateSeance;
    }

    public void closeSeance(Long seanceId) {
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Seance not found"));

        seance.setEndTime(LocalTime.now());
        seance.setNombreHeureEffectuee(Duration.between(seance.getStartTime(), seance.getEndTime()).toHoursPart());

        seanceRepository.save(seance);
    }

    public List<Seance> getSeancesByUeId(Long ueId) {
        return seanceRepository.findByUe_UeId(ueId);
    }

    public boolean verifierSeanceEnCours(Long ueId) {
        Optional<Seance> seanceEnCours = seanceRepository.findFirstByUe_UeIdAndEndTimeIsNull(ueId);
        return seanceEnCours.isPresent();
    }

    public List<Seance> getOpenSeancesWithoutCahierByEtudiant(Long etudiantId) {
        return seanceRepository.findOpenSeancesWithoutCahierByEtudiant(etudiantId);
    }

    /**
     * Retourner le nombre de séances enregistrées pour une UE donnée
     */
    public Long getNombreSeancesByUE(Long ueId) {
        return seanceRepository.countSeancesByUE(ueId);
    }

    /**
     * Calculer le taux d'achèvement d'une UE donnée
     * Le taux d'achèvement est basé sur le nombre d'heures effectuées par rapport aux heures totales attendues.
     * Heures totales = crédits de l'UE * 12
     */
    public Double getTauxAchevementUE(Long ueId) {
        // Récupérer l'UE pour obtenir le crédit et les heures effectuées
        UE ue = ueRepository.findById(ueId).orElse(null);
        if (ue == null) {
            return null; // L'UE n'existe pas
        }

        // Calculer le nombre d'heures totales (crédit * 12)
        int heuresTotales = ue.getCredit() * 12;

        // Calculer le nombre d'heures effectuées
        Integer heuresEffectuees = seanceRepository.sumHeuresEffectueesByUE(ueId);
        if (heuresEffectuees == null) {
            heuresEffectuees = 0;
        }

        // Calculer le taux d'achèvement
        return (heuresEffectuees * 100.0) / heuresTotales;
    }
}
