package com.defitech.GestUni.service.Azhar;

import com.defitech.GestUni.config.Azhar.ResourceNotFoundException;
import com.defitech.GestUni.dto.Azhar.*;
import com.defitech.GestUni.enums.PermissionStatus;
import com.defitech.GestUni.models.Azhar.Permission;
import com.defitech.GestUni.models.Azhar.Presence;
import com.defitech.GestUni.models.Azhar.Seance;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.Tuteur;
import com.defitech.GestUni.repository.Azhar.PermissionRepository;
import com.defitech.GestUni.repository.Azhar.PresenceRepository;
import com.defitech.GestUni.repository.Azhar.SeanceRepository;
import com.defitech.GestUni.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class PresenceServices {

    @Autowired
    private PresenceRepository presenceRepository;
    @Autowired
    private SeanceServices seanceServices;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private azharEmailService azharEmailService;

    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private NotificationsServices notificationsServices;
    @Autowired
    private EtudiantRepository etudiantRepository;

//    public List<PresenceDTO> getPresencesBySeance(Long seanceId) {
//        Seance seance = seanceRepository.findById(seanceId)
//                .orElseThrow(() -> new ResourceNotFoundException("Seance not found"));
//
//        List<EtudiantDTOA> etudiants = getEtudiantsByUeId(seance.getUe().getUeId());
//
//        return etudiants.stream()
//                .map(etudiant -> {
//                    Permission permission = permissionRepository.findByEtudiant_EtudiantIdAndDateDebutAbsenceLessThanEqualAndDateFinAbsenceGreaterThanEqual(
//                            etudiant.getEtudiantId(), seance.getDate(), seance.getDate());
//
//                    Presence presence = presenceRepository.findByEtudiant_EtudiantIdAndSeance_SeanceId(etudiant.getEtudiantId(), seanceId)
//                            .orElseGet(() -> {
//                                Presence newPresence = new Presence();
//                                newPresence.setEtudiant(etudiantRepository.findById(etudiant.getEtudiantId())
//                                        .orElseThrow(() -> new ResourceNotFoundException("Etudiant not found")));
//                                newPresence.setSeance(seance);
//                                newPresence.setPresent(false); // Default to not present
//                                newPresence.setEnPermission(permission != null); // Set enPermission based on permission
//                                return newPresence;
//                            });
//
//                    return getPresenceDTO(etudiant, permission, presence);
//                })
//                .collect(Collectors.toList());
//    }
//    public void updatePresences(Long seanceId, List<PresenceDTO> presenceDTOs) {
//        Seance seance = seanceRepository.findById(seanceId)
//                .orElseThrow(() -> new ResourceNotFoundException("Séance non trouvée"));
//
//        // Vérifier que la séance n'est pas encore fermée
//        if (seance.getEndTime() != null) {
//            throw new IllegalStateException("La séance est déjà fermée.");
//        }
//
//        for (PresenceDTO presenceDTO : presenceDTOs) {
//            // Chercher une permission pour cet étudiant durant la séance
//            Permission permission = permissionRepository.findByEtudiant_EtudiantIdAndDateDebutAbsenceLessThanEqualAndDateFinAbsenceGreaterThanEqual(
//                    presenceDTO.getEtudiantId(), seance.getDate(), seance.getDate());
//
//            Presence presence = presenceRepository.findByEtudiant_EtudiantIdAndSeance_SeanceId(presenceDTO.getEtudiantId(), seanceId)
//                    .orElseGet(() -> {
//                        Presence newPresence = new Presence();
//                        newPresence.setEtudiant(etudiantRepository.findById(presenceDTO.getEtudiantId())
//                                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé")));
//                        newPresence.setSeance(seance);
//                        return newPresence;
//                    });
//
//            // Si l'étudiant est en permission, il n'est pas marqué absent ni présent, et le nombre d'absences ne doit pas être incrémenté
//            if (permission != null) {
//                presence.setEnPermission(true);
//                presence.setPresent(null);  // Si en permission, l'étudiant n'est ni présent ni absent
//            } else {
//                presence.setEnPermission(false);
//                presence.setPresent(presenceDTO.getPresent());  // Sinon, on enregistre s'il est présent ou absent
//            }
//
//            presenceRepository.save(presence);
//
//            // Vérification du nombre d'absences après la mise à jour des présences
//            // Ignorer les permissions dans le calcul des absences
//            if (presence.getPresent() != null && !presence.getPresent()) {
//                checkAndSendAbsenceAlert(presenceDTO.getEtudiantId());
//            }
//        }
//    }
    public List<PresenceDTO> getPresencesBySeance(Long seanceId) {
    Seance seance = seanceRepository.findById(seanceId)
            .orElseThrow(() -> new ResourceNotFoundException("Seance not found"));

    List<EtudiantDTOA> etudiants = getEtudiantsByUeId(seance.getUe().getUeId());

    return etudiants.stream()
            .map(etudiant -> {
                // Recherche de la permission avec le statut EN_COURS
                Permission permission = permissionRepository.findByEtudiant_EtudiantIdAndDateDebutAbsenceLessThanEqualAndDateFinAbsenceGreaterThanEqual(
                        etudiant.getEtudiantId(), seance.getDate(), seance.getDate());

                boolean enPermission = permission != null && permission.getStatus() == PermissionStatus.EN_COURS;

                Presence presence = presenceRepository.findByEtudiant_EtudiantIdAndSeance_SeanceId(etudiant.getEtudiantId(), seanceId)
                        .orElseGet(() -> {
                            Presence newPresence = new Presence();
                            newPresence.setEtudiant(etudiantRepository.findById(etudiant.getEtudiantId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Etudiant not found")));
                            newPresence.setSeance(seance);
                            newPresence.setPresent(false); // Par défaut, l'étudiant est absent
                            newPresence.setEnPermission(enPermission); // Mettre à jour en fonction de la permission
                            return newPresence;
                        });

                return getPresenceDTO(etudiant, permission, presence);
            })
            .collect(Collectors.toList());
}

    public void updatePresences(Long seanceId, List<PresenceDTO> presenceDTOs) {
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Séance non trouvée"));

        // Vérifier que la séance n'est pas encore fermée
        if (seance.getEndTime() != null) {
            throw new IllegalStateException("La séance est déjà fermée.");
        }

        for (PresenceDTO presenceDTO : presenceDTOs) {
            // Recherche de la permission avec le statut EN_COURS
            Permission permission = permissionRepository.findByEtudiant_EtudiantIdAndDateDebutAbsenceLessThanEqualAndDateFinAbsenceGreaterThanEqual(
                    presenceDTO.getEtudiantId(), seance.getDate(), seance.getDate());

            boolean enPermission = permission != null && permission.getStatus() == PermissionStatus.EN_COURS;

            Presence presence = presenceRepository.findByEtudiant_EtudiantIdAndSeance_SeanceId(presenceDTO.getEtudiantId(), seanceId)
                    .orElseGet(() -> {
                        Presence newPresence = new Presence();
                        newPresence.setEtudiant(etudiantRepository.findById(presenceDTO.getEtudiantId())
                                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé")));
                        newPresence.setSeance(seance);
                        return newPresence;
                    });

            // Si l'étudiant est en permission (statut EN_COURS), il n'est ni absent ni présent
            if (enPermission) {
                presence.setEnPermission(true);
                presence.setPresent(null);  // Si en permission, pas de statut de présence
            } else {
                presence.setEnPermission(false);
                presence.setPresent(presenceDTO.getPresent());  // Sinon, on enregistre sa présence ou son absence
            }

            presenceRepository.save(presence);

            // Vérification du nombre d'absences après la mise à jour des présences
            if (presence.getPresent() != null && !presence.getPresent()) {
                checkAndSendAbsenceAlert(presenceDTO.getEtudiantId());
            }
        }
    }

    private void checkAndSendAbsenceAlert(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        int nombreAbsences = presenceRepository.countByEtudiantAndPresentFalseAndEnPermissionFalse(etudiant);
        if (nombreAbsences > 10) {
            signalerAbsence(etudiantId);
        }
    }
    private PresenceDTO getPresenceDTO(EtudiantDTOA etudiant, Permission permission, Presence presence) {
        PresenceDTO presenceDTO = new PresenceDTO();
        presenceDTO.setEtudiantId(etudiant.getEtudiantId());
        presenceDTO.setNomEtudiant(etudiant.getNom());
        presenceDTO.setPrenomEtudiant(etudiant.getPrenom());
        presenceDTO.setMatriculeEtudiant(etudiant.getMatricule());

        if (permission != null) {
            presenceDTO.setPresent(false);
            presenceDTO.setEnPermission(true);
        } else {
            presenceDTO.setPresent(presence.getPresent());
            presenceDTO.setEnPermission(false);
        }
        return presenceDTO;
    }

    public List<EtudiantDTOA> getEtudiantsByUeId(Long ueId) {
        List<Etudiant> etudiants = seanceRepository.findEtudiantsByUe_UeId(ueId);
        return etudiants.stream()
                .map(e -> new EtudiantDTOA(e.getEtudiantId(), e.getNom(), e.getPrenom(), e.getMatricule()))
                .collect(Collectors.toList());
    }

    public void updatePresencesAfterClose(Long seanceId, List<PresenceDTO> presenceDTOs) {
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Seance not found"));

        // Vérifier que la séance n'est pas encore fermée
        if (seance.getEndTime() != null) {
            throw new IllegalStateException("Impossible de mettre à jour les présences après la fermeture de la séance.");
        }

        for (PresenceDTO presenceDTO : presenceDTOs) {
            // Chercher une permission pour cet étudiant durant la séance
            Permission permission = permissionRepository.findByEtudiant_EtudiantIdAndDateDebutAbsenceLessThanEqualAndDateFinAbsenceGreaterThanEqual(
                    presenceDTO.getEtudiantId(), seance.getDate(), seance.getDate());

            boolean enPermission = permission != null && permission.getStatus() == PermissionStatus.EN_COURS;

            Presence presence = presenceRepository.findByEtudiant_EtudiantIdAndSeance_SeanceId(presenceDTO.getEtudiantId(), seanceId)
                    .orElseGet(() -> {
                        Presence newPresence = new Presence();
                        newPresence.setEtudiant(etudiantRepository.findById(presenceDTO.getEtudiantId())
                                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé")));
                        newPresence.setSeance(seance);
                        return newPresence;
                    });

            if (enPermission) {
                presence.setEnPermission(true);
                presence.setPresent(null);  // Si en permission, pas de statut de présence
            } else {
                presence.setEnPermission(false);
                presence.setPresent(presenceDTO.getPresent());  // Sinon, on enregistre sa présence ou son absence
            }

            presenceRepository.save(presence);
        }

        // Fermer la séance en définissant l'heure de fin
        seance.setEndTime(LocalTime.now());
        seanceRepository.save(seance);

        // Envoyer une notification au professeur
        notificationsServices.sendNotification(seance.getUe().getProfesseur().getUtilisateur(),
                "Les présences ont été mises à jour et la séance a été fermée.");
    }

    public List<HistoriquePresence> getHistoriqueByUeId(Long ueId) {
        List<Seance> seances = seanceRepository.findByUe_UeId(ueId);
        return seances.stream().map(this::convertSeanceToDTO).collect(Collectors.toList());
    }
    // Méthode de conversion d'une Seance en DTO
    private HistoriquePresence convertSeanceToDTO(Seance seance) {
        HistoriquePresence dto = new HistoriquePresence();
        dto.setSeanceDate(seance.getDate());
        dto.setNomCours(seance.getUe().getLibelle());
        // Conversion des présences associées à la séance
        List<PresenceDTO> presences = seance.getPresences().stream().map(presence -> {
            PresenceDTO presenceDTO = new PresenceDTO();
            presenceDTO.setEtudiantId(presence.getEtudiant().getEtudiantId());
            presenceDTO.setMatriculeEtudiant(presence.getEtudiant().getMatricule());
            presenceDTO.setNomEtudiant(presence.getEtudiant().getNom());
            presenceDTO.setPrenomEtudiant(presence.getEtudiant().getPrenom());
            presenceDTO.setPresent(presence.getPresent());
            presenceDTO.setEnPermission(presence.getEnPermission());
            return presenceDTO;
        }).collect(Collectors.toList());

        dto.setPresences(presences);
        return dto;
    }

    public AbsenceInfoResponse getAbsenceInfoForUE(Long ueId) {
        Double absenceRate = findAbsenceRateForUE(ueId);
        TauxPresence mostAbsentStudent = findMostAbsentStudentForUE(ueId);
        Long nombreSeances = seanceServices.getNombreSeancesByUE(ueId);

        return new AbsenceInfoResponse(absenceRate, mostAbsentStudent, nombreSeances);
    }

    /**
     * Trouver l'étudiant le plus absent pour une UE donnée.
     */
    public String findMostAbsentStudentNameForUE(Long ueId) {
        List<Object[]> result = presenceRepository.findMostAbsentStudentByUE(ueId);
        if (!result.isEmpty()) {
            Etudiant etudiant = (Etudiant) result.get(0)[0];  // Récupérer l'étudiant
            return etudiant.getNom() + " " + etudiant.getPrenom();  // Retourner le nom complet
        }
        return null;  // Aucun étudiant trouvé
    }
    public TauxPresence findMostAbsentStudentForUE(Long ueId) {
        List<Object[]> result = presenceRepository.findMostAbsentStudentByUE(ueId);
        return result.isEmpty() ? null : createTauxPresenceForMostAbsentStudent(result.get(0), ueId);
    }

    /**
     * Trouver le taux de présence de chaque étudiant pour une UE donnée.
     */
    public List<TauxPresence> findAttendanceRateForStudentsInUE(Long ueId) {
        List<Object[]> result = presenceRepository.findAttendanceRateForStudentsByUE(ueId);
        return mapToTauxPresence(result, ueId);
    }

    /**
     * Trouver le taux d'absence pour une UE donnée.
     */
    public Double findAbsenceRateForUE(Long ueId) {
        return presenceRepository.findAbsenceRateForUE(ueId);
    }

    /**
     * Trouver le taux de présence de tous les étudiants d'un parcours donné.
     */
    public List<TauxPresence> findAttendanceRateForStudentsInParcours(Long parcoursId) {
        List<Object[]> result = presenceRepository.findPresenceRateForStudentsByParcoursAndOptionallyUE(parcoursId, null);
        return mapToTauxPresence(result, null);
    }

    /**
     * Trouver le taux de présence de chaque étudiant dans un parcours donné pour une UE spécifique.
     */
    public List<TauxPresence> findPresenceRateForEachStudentByUEAndParcours(Long ueId, Long parcoursId) {
        List<Object[]> result = presenceRepository.findPresenceRateForStudentsByParcoursAndOptionallyUE(parcoursId, ueId);
        return mapToTauxPresence(result, ueId);
    }

    /**
     * Mapper les résultats Object[] vers TauxPresence et calculer le taux de présence basé sur le nombre total de séances
     */
    private List<TauxPresence> mapToTauxPresence(List<Object[]> result, Long ueId) {
        List<TauxPresence> tauxPresenceList = new ArrayList<>();

        // Récupérer le nombre total de séances pour l'UE donnée
        Long totalSeances = seanceServices.getNombreSeancesByUE(ueId);
        if (totalSeances == null || totalSeances == 0) {
            throw new IllegalStateException("Le nombre total de séances ne peut pas être nul ou zéro.");
        }

        // Parcourir les résultats de la requête
        for (Object[] row : result) {
            Etudiant etudiant = (Etudiant) row[0];
            Long nbPresences = (row[1] != null) ? ((Number) row[1]).longValue() : 0L; // Nombre de présences

            // Calculer le nombre d'absences
            Long nbAbsences = totalSeances - nbPresences;

            // Calcul du taux d'absence
            Double tauxAbsence = (nbAbsences / (double) totalSeances) * 100.0;

            // Calcul du taux de présence en soustrayant le taux d'absence de 100
            Double attendanceRate = 100.0 - tauxAbsence;

            // Limiter le taux de présence à la plage de 0 à 100
            if (attendanceRate < 0.0) {
                attendanceRate = 0.0;
            } else if (attendanceRate > 100.0) {
                attendanceRate = 100.0;
            }

            // Créer l'objet TauxPresence
            TauxPresence tauxPresence = new TauxPresence();
            tauxPresence.setEtudiantId(etudiant.getEtudiantId());
            tauxPresence.setMatricule(etudiant.getMatricule());
            tauxPresence.setNom(etudiant.getNom());
            tauxPresence.setPrenom(etudiant.getPrenom());
            tauxPresence.setAttendanceRate(attendanceRate);

            tauxPresenceList.add(tauxPresence);

            // Log pour vérifier les valeurs
            System.out.println("Etudiant: " + etudiant.getNom() + " Nb Presences: " + nbPresences +
                    " Total Seances: " + totalSeances + " Taux Presence: " + attendanceRate);
        }

        return tauxPresenceList;
    }
    public List<Map<String, Object>> calculerTauxPresence(Long ueId) {
        List<Object[]> result = presenceRepository.calculerTauxPresenceParUE(ueId);

        return result.stream().map(row -> {
            Map<String, Object> etudiantData = new HashMap<>();
            Long etudiantId = (Long) row[0];
            String nom = (String) row[1];
            String prenom = (String) row[2];
            String matricule = (String) row[3];
            Long totalSeances = (Long) row[4];
            Long presencesEffectives = (Long) row[5];

            // Calcul du taux de présence en pourcentage
            double tauxPresence = (totalSeances > 0) ? (presencesEffectives * 100.0) / totalSeances : 0.0;

            // Arrondir à deux décimales avec un arrondi par excès
            tauxPresence = Math.ceil(tauxPresence * 100.0) / 100.0;

            etudiantData.put("id", etudiantId);
            etudiantData.put("nom", nom);
            etudiantData.put("prenom", prenom);
            etudiantData.put("matricule", matricule);
            etudiantData.put("tauxPresence", tauxPresence);

            return etudiantData;
        }).toList();
    }

    /**
     * Créer un TauxPresence pour l'étudiant le plus absent dans une UE.
     */
    private TauxPresence createTauxPresenceForMostAbsentStudent(Object[] row, Long ueId) {
        Etudiant etudiant = (Etudiant) row[0];
        Long nbAbsences = (Long) row[1];
        Long totalSeances = seanceServices.getNombreSeancesByUE(ueId);

        // Vérifier que le nombre total de séances est valide
        if (totalSeances == null || totalSeances == 0) {
            throw new IllegalStateException("Le nombre total de séances ne peut pas être nul ou zéro.");
        }

        // Calcul du taux d'absence
        Double tauxAbsence = (nbAbsences != null ? nbAbsences.doubleValue() : 0.0) / totalSeances * 100.0;

        // Créer l'objet TauxPresence
        TauxPresence tauxPresence = new TauxPresence();
        tauxPresence.setEtudiantId(etudiant.getEtudiantId());
        tauxPresence.setMatricule(etudiant.getMatricule());
        tauxPresence.setNom(etudiant.getNom());
        tauxPresence.setPrenom(etudiant.getPrenom());

        // Calcul du taux de présence en soustrayant le taux d'absence de 100
        tauxPresence.setAttendanceRate(100.0 - tauxAbsence);  // Taux de présence correct

        return tauxPresence;
    }

    public void signalerAbsence(Long etudiantId) {
        // Récupérer les informations de l'étudiant et de son tuteur
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant introuvable avec l'ID : " + etudiantId));

        Tuteur tuteur = etudiant.getTuteur();
        String etudiantNom = etudiant.getNom() + " " + etudiant.getPrenom();
        String tuteurNom = tuteur.getNom() + " " + tuteur.getPrenom();

        // Récupérer l'historique des absences
        List<Presence> absences = presenceRepository.findByEtudiantAndPresentFalse(etudiant);
        int nombreAbsences = absences.size();

        // Envoyer l'email à l'étudiant
        azharEmailService.sendTropAbsencesEtudiantEmail(etudiant.getEmail(), etudiantNom, nombreAbsences);
        azharEmailService.sendAbsencesEnfantEmail(tuteur.getEmail(), tuteurNom, etudiantNom, nombreAbsences, absences);
    }

}
