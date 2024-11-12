package com.defitech.GestUni.service.Azhar;

import com.defitech.GestUni.config.Azhar.ResourceNotFoundException;
import com.defitech.GestUni.dto.Azhar.DemandePermisionDto;
import com.defitech.GestUni.dto.Azhar.PermissionDTO;
import com.defitech.GestUni.enums.PermissionStatus;
import com.defitech.GestUni.models.Azhar.Permission;
import com.defitech.GestUni.models.Bases.DirecteurEtude;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.Parcours;
import com.defitech.GestUni.repository.Azhar.DirecteurEtudeRepository;
import com.defitech.GestUni.repository.Azhar.PermissionRepository;
import com.defitech.GestUni.repository.EtudiantRepository;
import com.google.cloud.storage.Blob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private azharEmailService emailService;
    @Autowired
    private NotificationsServices notificationService;
    @Autowired
    private DirecteurEtudeRepository directeurEtudeRepository;
    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    @Scheduled(cron = "0 0 0 * * ?", zone = "GMT")
    public void scheduledUpdatePermissionsStatus() {
        updatePermissionsAutomatically();
    }

    public void updatePermissionsAutomatically() {
        LocalDate today = LocalDate.now();

        List<Permission> permissionsInProgress = permissionRepository.findPermissionsByStatusAndDateDebutAbsenceBefore(PermissionStatus.ACCEPTER, today);

        for (Permission permission : permissionsInProgress) {
            if (permission.getDateFinAbsence().isAfter(today) || permission.getDateFinAbsence().isEqual(today)) {
                permission.setStatus(PermissionStatus.EN_COURS);
            }
            permissionRepository.save(permission);
        }

        // Find all permissions that should be terminated but are not updated
        List<Permission> permissionsToTerminate = permissionRepository.findPermissionsByStatusAndDateFinAbsenceBefore(PermissionStatus.EN_COURS, today);

        for (Permission permission : permissionsToTerminate) {
            permission.setStatus(PermissionStatus.TERMINEE);
            permissionRepository.save(permission);
        }
    }

    // Ajout permission
//    public void ajoutPermission(Long etudiantId, LocalDate dateDebutAbsence, LocalDate dateFinAbsence,
//                                String raison, String description, MultipartFile fichier) throws IOException {
//        logger.debug("Début de ajoutPermission");
//        Etudiant etudiant = etudiantRepository.findById(etudiantId)
//                .orElseThrow(() -> new RuntimeException("Etudiant non trouvé"));
//        logger.debug("Etudiant trouvé: " + etudiant);
//
//        Permission permission = new Permission();
//        permission.setEtudiant(etudiant);
//        permission.setDateDebutAbsence(dateDebutAbsence);
//        permission.setDateFinAbsence(dateFinAbsence);
//        permission.setDescription(description);
//        permission.setRaison(raison);
//        permission.setStatus(PermissionStatus.EN_ATTENTE);
//
//        long durationInDays = ChronoUnit.DAYS.between(dateDebutAbsence, dateFinAbsence);
//        permission.setDuree(durationInDays);
//
////        if (fichier != null && !fichier.isEmpty()) {
////            String fileUrl = googleCloudStorageService.uploadFile(fichier);
////            permission.setFileUrl(fileUrl);
////        }
//
//        permissionRepository.save(permission);
//        logger.debug("Permission sauvegardée: " + permission);
//
////        DirecteurEtude directeurEtude = directeurEtudeRepository.findByParcours(etudiant.getParcours());
////        if (directeurEtude == null) {
////            throw new RuntimeException("Aucun Directeur d'étude trouvé pour le parcours: " + etudiant.getParcours());
////        }
////        logger.debug("Directeur d'étude trouvé: " + directeurEtude);
//
////        notificationService.sendNotification(directeurEtude.getUtilisateur(), "Nouvelle demande de permission");
////        logger.debug("Notification envoyée au directeur d'étude");
//    }

    public Permission addPermission(Long etudiantId, LocalDate dateDebutAbsence, LocalDate dateFinAbsence,
                                    String raison, String description, MultipartFile file) throws IOException {
        // Validate dates
        if (dateDebutAbsence == null || dateFinAbsence == null) {
            throw new IllegalArgumentException("DateDebutAbsence or DateFinAbsence is null");
        }

        if (raison == null || raison.trim().isEmpty()) {
            throw new IllegalArgumentException("Raison is null or empty");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is null or empty");
        }

        LocalDate today = LocalDate.now();
        if (dateDebutAbsence.isBefore(today) || dateFinAbsence.isBefore(today)) {
            throw new IllegalArgumentException("La date de début et la date de fin ne doivent pas être avant la date actuelle.");
        }

        if (dateFinAbsence.isBefore(dateDebutAbsence)) {
            throw new IllegalArgumentException("La date de fin ne doit pas être avant la date de début.");
        }

        String fileUrl = null; // Initialize fileUrl to null

        // Check if the file is provided
        if (file != null && !file.isEmpty()) {
            fileUrl = googleCloudStorageService.uploadFile(file);
        }

        // Récupérer l'étudiant à partir de la base de données
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant introuvable avec l'ID: " + etudiantId));

        // Vérification de l'email de l'étudiant
        if (etudiant.getEmail() == null || etudiant.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'e-mail de l'étudiant est introuvable ou invalide.");
        }

        // Create and save permission
        Permission permission = new Permission();
        permission.setEtudiant(etudiant);
        permission.setDateDemande(today);
        permission.setDateDebutAbsence(dateDebutAbsence);
        permission.setDateFinAbsence(dateFinAbsence);
        permission.setRaison(raison);
        permission.setDescription(description);
        permission.setFileUrl(fileUrl);
        permission.setStatus(PermissionStatus.EN_ATTENTE);

        // Afficher l'email de l'étudiant dans les logs pour vérification
        System.out.println("email de l'etudiant : " + etudiant.getEmail());

        // Envoi de l'e-mail
        emailService.sendDemandeReceptionEmail(etudiant.getEmail(), permission);

        return permissionRepository.save(permission);
    }


    // Approuver une permission
    public PermissionDTO approvePermission(Long permissionId, String remarque, Long directeurEtudeId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission non trouvée"));

        DirecteurEtude directeurEtude = directeurEtudeRepository.findById(directeurEtudeId)
                .orElseThrow(() -> new RuntimeException("Directeur d'étude non trouvé"));

        // Vérifier que le directeur d'étude est associé au parcours de l'étudiant
        if (directeurEtude.getParcours() == null) {
            throw new RuntimeException("Le directeur d'étude n'est pas associé à un parcours");
        }

        if (!directeurEtude.getParcours().equals(permission.getEtudiant().getParcours())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à approuver cette permission");
        }

        if (remarque != null && !remarque.isEmpty()) {
            permission.setRemarque(remarque);
        }

        // Mettre à jour le statut de la permission
        permission.setStatus(PermissionStatus.ACCEPTER);
        permissionRepository.save(permission);

        emailService.sendDemandeAppouverEmail(permission.getEtudiant().getEmail(), permission);
        // Envoyer une notification à l'étudiant
        notificationService.sendNotification(permission.getEtudiant().getUtilisateur(),
                "Votre demande de permission a été approuvée");

        return toPermissionDTO(permission);
    }

    // Méthode pour rejeter une permission
    public PermissionDTO rejectPermission(Long permissionId, String raison, Long directeurEtudeId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission non trouvée"));

        DirecteurEtude directeurEtude = directeurEtudeRepository.findById(directeurEtudeId)
                .orElseThrow(() -> new RuntimeException("Directeur d'étude non trouvé"));

        // Vérifier que le directeur d'étude est associé au parcours de l'étudiant
        if (directeurEtude.getParcours() == null) {
            throw new RuntimeException("Le directeur d'étude n'est pas associé à un parcours");
        }

        if (!directeurEtude.getParcours().equals(permission.getEtudiant().getParcours())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à rejeter cette permission");
        }

        if (raison != null && !raison.isEmpty()) {
            permission.setRaisonR(raison);
        }

        // Mettre à jour le statut de la permission
        permission.setStatus(PermissionStatus.REJETER);

        permissionRepository.save(permission);

        emailService.sendDemandeRejetEmail(permission.getEtudiant().getEmail(), permission);
        // Envoyer une notification à l'étudiant
        notificationService.sendNotification(permission.getEtudiant().getUtilisateur(),
                "Votre demande de permission a été rejetée");

        return toPermissionDTO(permission);
    }

    //Permission par filiere
    public List<PermissionDTO> getPermissionsByFiliere(Long filiereId) {
        return permissionRepository.findByFiliereId(filiereId);
    }

    public List<PermissionDTO> getPermissionsByParcours(Long parcoursId) {
        return permissionRepository.findByParcoursId(parcoursId);
    }

    public List<PermissionDTO> getPermissionsByFiliereAndStatus(Long filiereId, PermissionStatus status) {
        return permissionRepository.findByFiliereIdAndStatus(filiereId, status);
    }

    public List<PermissionDTO> getPermissionsByPeriod(Long filiereId, PermissionStatus status, LocalDate startDate, LocalDate endDate) {
        return permissionRepository.findByFiliereIdAndStatusAndPeriod(filiereId, status, startDate, endDate);
    }

    public Permission getpermissionById(Long id){ return permissionRepository.findById(id).orElse(null);}

    public Optional<Permission> getPermissionById(Long permissionId) {
        return permissionRepository.findById(permissionId);
    }

    public PermissionDTO getPermissionDTOById(Long permissionId) {
        Optional<Permission> permissionOptional = permissionRepository.findById(permissionId);

        if (permissionOptional.isPresent()) {
            Permission permission = permissionOptional.get();
            return toPermissionDTO(permission);  // Utilise la méthode toPermissionDTO pour mapper
        } else {
            throw new ResourceNotFoundException("Permission avec l'ID " + permissionId + " non trouvée");
        }
    }

    public void deletePermission(Long id){
        permissionRepository.deleteById(id);
    }

    public PermissionDTO toPermissionDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setPermissionId(permission.getPermissionId());

        // Mappe les informations de l'étudiant
        Etudiant etudiant = permission.getEtudiant();
        dto.setEtudiantId(etudiant.getEtudiantId());
        dto.setNom(etudiant.getNom());
        dto.setPrenom(etudiant.getPrenom());
        dto.setEmail(etudiant.getEmail());
        dto.setMatricule(etudiant.getMatricule());

        // Mappe les informations de la permission
        dto.setDateDebutAbsence(permission.getDateDebutAbsence());
        dto.setDateFinAbsence(permission.getDateFinAbsence());
        dto.setDateDemande(permission.getDateDemande());
        dto.setRemarque(permission.getRemarque());
        dto.setStatus(permission.getStatus());
        dto.setRaison(permission.getRaison());
        dto.setRaisonR(permission.getRaisonR());
        dto.setDuree(permission.getDuree() != null ? Long.valueOf(permission.getDuree()) : null);
        dto.setDescription(permission.getDescription());
        dto.setFileUrl(permission.getFileUrl());

        return dto;
    }

    public List<PermissionDTO> getPermissionsByStudent(Long etudiantId) {
        List<Permission> permissions = permissionRepository.findByEtudiant_EtudiantId(etudiantId);
        return permissions.stream()
                .map(this::toPermissionDTO)
                .collect(Collectors.toList());
    }

    public List<PermissionDTO> getPermissionsByParcoursId(Long parcoursId) {
        List<Permission> permissions = permissionRepository.findAllByParcoursId(parcoursId);
        return permissions.stream().map(this::toPermissionDTO).collect(Collectors.toList());
    }

    public Optional<Blob> getFileByPermissionId(Long permissionId) {
        Optional<Permission> permission = permissionRepository.findById(permissionId);

        if (permission.isPresent()) {
            String fileUrl = permission.get().getFileUrl();
            if (fileUrl != null && !fileUrl.isEmpty()) {
                Blob blob = googleCloudStorageService.getFile(fileUrl);
                if (blob != null) {
                    return Optional.of(blob);
                } else {
                    System.out.println("Fichier non trouvé dans Google Cloud Storage pour fileUrl: " + fileUrl);
                }
            } else {
                System.out.println("Le champ fileUrl est vide ou nul pour la permission avec ID: " + permissionId);
            }
        } else {
            System.out.println("Permission avec ID " + permissionId + " non trouvée.");
        }
        return Optional.empty();
    }

}
