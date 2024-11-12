package com.defitech.GestUni.service.Azhar;

import com.defitech.GestUni.config.Azhar.ResourceNotFoundException;
import com.defitech.GestUni.dto.Azhar.CahierDto;
import com.defitech.GestUni.dto.UEDto;
import com.defitech.GestUni.enums.Etat;
import com.defitech.GestUni.enums.StatutCahier;
import com.defitech.GestUni.models.Azhar.CahierTexte;
import com.defitech.GestUni.models.Azhar.Seance;
import com.defitech.GestUni.models.Bases.Filiere;
import com.defitech.GestUni.models.Bases.UE;
import com.defitech.GestUni.repository.Azhar.CahierTexteRepository;
import com.defitech.GestUni.repository.Azhar.SeanceRepository;
import com.defitech.GestUni.repository.UERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CahierTexteService {

    @Autowired
    private CahierTexteRepository cahierTexteRepo;
    @Autowired
    private NotificationsServices notificationsServices;
    @Autowired
    private UERepository ueRepository;
    @Autowired
    private SeanceRepository seanceRepo;
    @Autowired
    private azharEmailService emailService;
    @Autowired
    private SeanceRepository seanceRepository;

    public CahierTexte createCahierTexte(Long seanceId, CahierTexte dto) {
        Optional<CahierTexte> existingCahierTexte = cahierTexteRepo.findBySeance_seanceId(seanceId);
        if (existingCahierTexte.isPresent()) {
            throw new IllegalStateException("Un CahierTexte existe déjà pour cette séance.");
        }

        Seance seance = seanceRepo.findById(seanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Seance not found for this ID :: " + seanceId));

        CahierTexte cahierTexte = new CahierTexte();
        cahierTexte.setSeance(seance);
        cahierTexte.setContenuSeance(dto.getContenuSeance());
        cahierTexte.setHeureDebut(dto.getHeureDebut());
        cahierTexte.setHeureFin(dto.getHeureFin());
        cahierTexte.setStatus(StatutCahier.ENREGISTRER);

        long heuresEffectuees = Duration.between(dto.getHeureDebut(), dto.getHeureFin()).toHours();

        UE ue = seance.getUe();
        if (ue.getHeureEffectuer() == null) {
            ue.setHeureEffectuer((int) heuresEffectuees);
        } else {
            ue.setHeureEffectuer(ue.getHeureEffectuer() + (int) heuresEffectuees);
        }

        ueRepository.save(ue);

        return cahierTexteRepo.save(cahierTexte);
    }

    public CahierTexte updateOrModifyCahierTexte(Long seanceId, CahierTexte dto) {
        // Récupérer la séance par seanceId
        Seance seance = seanceRepo.findById(seanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Seance not found for this ID :: " + seanceId));

        // Récupérer le cahier de texte associé à la séance
        CahierTexte existingCahierTexte = cahierTexteRepo.findBySeance_SeanceId(seanceId)
                .orElseThrow(() -> new ResourceNotFoundException("CahierTexte not found for this Seance ID :: " + seanceId));

        // Vérifier si la séance est déjà fermée
        if (seance.getEndTime() != null) {
            throw new IllegalStateException("La séance est déjà fermée.");
        }

        // Mettre à jour les informations du cahier de texte si elles sont fournies
        if (dto.getContenuSeance() != null && !dto.getContenuSeance().isEmpty()) {
            existingCahierTexte.setContenuSeance(dto.getContenuSeance());
        }
        if (dto.getHeureDebut() != null) {
            existingCahierTexte.setHeureDebut(dto.getHeureDebut());
        }
        if (dto.getHeureFin() != null) {
            existingCahierTexte.setHeureFin(dto.getHeureFin());
        }

        // Vérifier si le cahier est prêt à être confirmé (si heure début et fin sont présentes)
        if (existingCahierTexte.getHeureDebut() != null && existingCahierTexte.getHeureFin() != null) {
            existingCahierTexte.setStatus(StatutCahier.CONFIRMER);

            UE ue = seance.getUe();
            long heuresEffectuees = Duration.between(existingCahierTexte.getHeureDebut(), existingCahierTexte.getHeureFin()).toHours();

            if (ue.getHeureEffectuer() == null) {
                ue.setHeureEffectuer((int) heuresEffectuees);
            } else {
                ue.setHeureEffectuer(ue.getHeureEffectuer() + (int) heuresEffectuees);
            }
            ueRepository.save(ue);

            notificationsServices.sendNotification(
                    seance.getUe().getProfesseur().getUtilisateur(),
                    "Le cahier de texte a été confirmé."
            );
        } else {
            existingCahierTexte.setStatus(StatutCahier.ENREGISTRER);
        }

        // Sauvegarder les modifications du cahier
        CahierTexte updatedCahierTexte = cahierTexteRepo.save(existingCahierTexte);
        return updatedCahierTexte;
    }

    public CahierTexte confirmerCahierTexte(Long cahierTexteId) {
        CahierTexte cahierTexte = cahierTexteRepo.findById(cahierTexteId)
                .orElseThrow(() -> new ResourceNotFoundException("CahierTexte not found for this ID :: " + cahierTexteId));

        if (cahierTexte.getStatus() != StatutCahier.ENREGISTRER) {
            throw new IllegalStateException("Le CahierTexte ne peut être confirmé car il n'est pas dans l'état ENREGISTRER.");
        }

        if (cahierTexte.getSeance().getEndTime() != null) {
            throw new IllegalStateException("La séance est fermée");
        }

        cahierTexte.setStatus(StatutCahier.CONFIRMER);
        CahierTexte confirmedCahierTexte = cahierTexteRepo.save(cahierTexte);

        // Envoyer une notification au directeur d'études
        notificationsServices.sendNotification(cahierTexte.getSeance().getUe().getProfesseur().getUtilisateur(), "Un cahier de texte a été confirmé.");

        return confirmedCahierTexte;
    }

    public CahierTexte confirmerCahierTexteClose(Long cahierTexteId) {
        CahierTexte cahierTexte = cahierTexteRepo.findById(cahierTexteId)
                .orElseThrow(() -> new ResourceNotFoundException("CahierTexte not found for this ID :: " + cahierTexteId));

        if (cahierTexte.getStatus() != StatutCahier.ENREGISTRER) {
            throw new IllegalStateException("Le CahierTexte ne peut être confirmé car il n'est pas dans l'état ENREGISTRER.");
        }

        Seance seance = cahierTexte.getSeance();

        // Vérifier si la séance est déjà fermée
        if (seance.getEndTime() != null) {
            throw new IllegalStateException("La séance est déjà fermée");
        }

        // Mettre à jour le statut du cahier de texte
        cahierTexte.setStatus(StatutCahier.CONFIRMER);
        CahierTexte confirmedCahierTexte = cahierTexteRepo.save(cahierTexte);

        seance.setEndTime(LocalTime.now());
        seanceRepo.save(seance);

        notificationsServices.sendNotification(seance.getUe().getProfesseur().getUtilisateur(),
                "Les présences ont été mises à jour et la séance a été fermée.");
        notificationsServices.sendNotification(seance.getUe().getProfesseur().getUtilisateur(),
                "Un cahier de texte a été confirmé.");

        return confirmedCahierTexte;
    }

    public CahierTexte createCahierTexteByProf(CahierTexte dto) {
        Seance seance = seanceRepo.findById(dto.getSeance().getSeanceId())
                .orElseThrow(() -> new ResourceNotFoundException("Seance not found for this ID :: " + dto.getSeance().getSeanceId()));

        // Vérifier si la séance est déjà fermée
        if (seance.getEndTime() != null) {
            throw new IllegalStateException("La séance est déjà fermée");
        }

        // Créer le cahier de texte
        CahierTexte cahierTexte = new CahierTexte();
        cahierTexte.setSeance(seance);
        cahierTexte.setContenuSeance(dto.getContenuSeance());
        cahierTexte.setHeureDebut(dto.getHeureDebut());
        cahierTexte.setHeureFin(dto.getHeureFin());
        cahierTexte.setStatus(StatutCahier.CONFIRMER);

        // Sauvegarder le cahier de texte
        CahierTexte savedCahierTexte = cahierTexteRepo.save(cahierTexte);

        // Fermer la séance après la création du cahier de texte
        seance.setEndTime(LocalTime.now());
        seanceRepo.save(seance);

        // Envoyer une notification au professeur
        notificationsServices.sendNotification(seance.getUe().getProfesseur().getUtilisateur(),
                "Un cahier de texte a été confirmé et la séance a été fermée.");

        return savedCahierTexte;
    }

    public List<CahierDto> findCahiersByUeId(Long ueId) {
        List<CahierTexte> cahiers = cahierTexteRepo.findByUeId(ueId);
        return cahiers.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // Trouver les cahiers de texte par filière
    public List<CahierDto> findCahiersByEtudiantId(Long etudiantId) {
        List<CahierTexte> cahiers = cahierTexteRepo.findCahiersByEtudiantId(etudiantId);
        return cahiers.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // Convertir CahierTexte en CahierDto
    private CahierDto mapToDto(CahierTexte cahier) {
        CahierDto dto = new CahierDto();
        dto.setId(cahier.getId());
        dto.setNomCours(cahier.getSeance().getUe().getLibelle());
        dto.setContenuSeance(cahier.getContenuSeance());
        dto.setDate(cahier.getSeance().getDate());
        dto.setHeureDebut(cahier.getHeureDebut());
        dto.setDuree(cahier.getDuree());
        dto.setHeureFin(cahier.getHeureFin());
        dto.setCredit(cahier.getSeance().getUe().getCredit());
        return dto;
    }

    public boolean hasCahierEnregistrerForSeance(Long seanceId) {
        CahierTexte cahier = cahierTexteRepo.findBySeance_SeanceIdAndStatus(seanceId, StatutCahier.ENREGISTRER);
        return cahier != null;
    }

    public Optional<CahierTexte> findCahierTexteBySeanceId(Long seanceId) {
        return cahierTexteRepo.findBySeance_SeanceId(seanceId);
    }

    public void markUeAsRetard(Long ueId) {
        UE ue = ueRepository.findById(ueId)
                .orElseThrow(() -> new IllegalArgumentException("UE with id " + ueId + " not found"));
//        ue.setEtat(Etat.EN_RETARD);
        ueRepository.save(ue);
        emailService.sendRetardNotification(ue);
    }

    public UEDto convertToDTO(UE ue) {
        UEDto ueDto = new UEDto();
        ueDto.setUeId(ue.getUeId());
        ueDto.setLibelle(ue.getLibelle());
        ueDto.setCode(ue.getCode());
        ueDto.setCredit(ue.getCredit());
        ueDto.setTypeUe(ue.getTypeUe());
        ueDto.setDescriptUe(ue.getDescriptUe());
        ueDto.setProfesseurNom(ue.getProfesseur().getNom() + " " + ue.getProfesseur().getPrenom());

        if (ue.getNiveauEtude() != null) {
            ueDto.setNiveauEtude(ue.getNiveauEtude());
        } else {
            ueDto.setNiveauEtude(null);
        }

        if (ue.getTypeSemestre() != null) {
            ueDto.setTypeSemestre(ue.getTypeSemestre());
        } else {
            ueDto.setTypeSemestre(null);
        }

        ueDto.setFiliereNom(ue.getFilieres() != null ?
                ue.getFilieres().stream().map(Filiere::getNomFiliere).collect(Collectors.toList()) :
                Collections.emptyList());

        // Calcul du taux d'achèvement
        int heuresEffectuees = ue.getHeureEffectuer() != null ? ue.getHeureEffectuer() : 0;
        int totalHeuresPrevues = ue.getCredit() * 12;
        double tauxAchevement = (double) heuresEffectuees / totalHeuresPrevues * 100;
        ueDto.setTauxAchevement(Math.round(tauxAchevement * 100.0) / 100.0); // arrondi à 2 chiffres après la virgule

        ueDto.setParcoursNom(ueDto.getParcoursNom());

        return ueDto;
    }


    @Scheduled(cron = "0 0 23 * * SAT") // Exécuter chaque samedi à 23h
    public void checkAllUEsForWeeklyRetard() {
        LocalDate now = LocalDate.now();
        // Vérifie que c'est bien la fin de la semaine (samedi)
        if (now.getDayOfWeek() == java.time.DayOfWeek.SATURDAY) {
            List<UE> ueList = ueRepository.findAll();
            for (UE ue : ueList) {
                checkWeeklyUEForRetard(ue);
            }
        }
    }

    @Scheduled(cron = "0 0 23 L * ?") // Exécuter le dernier jour de chaque mois à 23h
    public void checkAllUEsForMonthlyRetard() {
        LocalDate now = LocalDate.now();
        // Vérifie que c'est bien la fin du mois
        if (now.equals(now.with(TemporalAdjusters.lastDayOfMonth()))) {
            List<UE> ueList = ueRepository.findAll();
            for (UE ue : ueList) {
                checkMonthlyUEForRetard(ue);
            }
        }
    }

    public void checkWeeklyUEForRetard(UE ue) {
        int totalHeuresPrevues = ue.getCredit() * 12;
        int heuresEffectuees = ue.getHeureEffectuer();

        // Vérification si le cours est terminé
        if (heuresEffectuees >= totalHeuresPrevues) {
            ue.setEtat(Etat.TERMINEE);
            ueRepository.save(ue);
            return;
        }

        int quotaSemaine = ue.getQuotaSemaine();

        // Calculer les heures effectuées cette semaine
        int heuresEffectueesSemaine = seanceRepository.findHeuresEffectueesSemaine(ue.getUeId(), LocalDate.now().minusWeeks(1), LocalDate.now());

        // Vérification du quota hebdomadaire
        if (heuresEffectueesSemaine < quotaSemaine && ue.getEtat() != Etat.TERMINEE) {
            ue.setEtat(Etat.EN_RETARD);
            ueRepository.save(ue);
            emailService.sendRetardNotification(ue);
        }
    }

    public void checkMonthlyUEForRetard(UE ue) {
        int totalHeuresPrevues = ue.getCredit() * 12;
        int heuresEffectuees = ue.getHeureEffectuer();

        // Vérification si le cours est terminé
        if (heuresEffectuees >= totalHeuresPrevues) {
            ue.setEtat(Etat.TERMINEE);
            ueRepository.save(ue);
            return;
        }

        int quotaMois = ue.getQuotaSemaine() * 4;

        // Calculer les heures effectuées ce mois-ci
        int heuresEffectueesMois = seanceRepository.findHeuresEffectueesMois(ue.getUeId(), LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));

        // Vérification du quota mensuel
        if (heuresEffectueesMois < quotaMois && ue.getEtat() != Etat.TERMINEE) {
            ue.setEtat(Etat.EN_RETARD);
            ueRepository.save(ue);
            emailService.sendRetardNotification(ue);
        }
    }

    public void checkSpecificUEForRetard(Long ueId) {
        UE ue = ueRepository.findById(ueId)
                .orElseThrow(() -> new IllegalArgumentException("UE with id " + ueId + " not found"));
        checkWeeklyUEForRetard(ue);
        checkMonthlyUEForRetard(ue);
    }
}

