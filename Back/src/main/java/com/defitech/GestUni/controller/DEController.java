package com.defitech.GestUni.controller;

import com.defitech.GestUni.dto.*;
import com.defitech.GestUni.dto.Azhar.*;
import com.defitech.GestUni.dto.Note.*;
import com.defitech.GestUni.enums.NiveauEtude;
import com.defitech.GestUni.enums.TypeNote;
import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.BAKA.JeffService.BulletinPdfService;
import com.defitech.GestUni.models.BAKA.JeffService.EtudiantjeffService;
import com.defitech.GestUni.models.BAKA.JeffService.ExcelExportService;
import com.defitech.GestUni.models.BAKA.JeffService.UejeffService;
import com.defitech.GestUni.models.BAKA.Note;
import com.defitech.GestUni.models.Bases.*;
import com.defitech.GestUni.service.*;
import com.defitech.GestUni.service.Azhar.CahierTexteService;
import com.defitech.GestUni.service.Azhar.PresenceServices;
import com.defitech.GestUni.service.Azhar.SeanceServices;
import com.google.cloud.storage.Blob;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import com.defitech.GestUni.enums.PermissionStatus;
import com.defitech.GestUni.models.Azhar.Permission;
import com.defitech.GestUni.service.Azhar.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth/de")
public class DEController {

    @Autowired
    private UEServices ueService;
    @Autowired
    private CahierTexteService cahierTexteService;
    @Autowired
    private FiliereServices filiereService;
    @Autowired
    private ProfesseurServices professeurService;
    @Autowired
    private ParcoursServices parcoursService;
    @Autowired
    private SeanceServices seanceServices;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PresenceServices presenceServices;
    @Autowired
    private UejeffService uejeffService;
    @Autowired
    private EtudiantjeffService etudiantjeffService;
    @Autowired
    private BulletinPdfService bulletinPdfService;
    @Autowired
    private BulletinService bulletinService;
    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private NoteService notesService;
    @Autowired
    private UtilisateurService utilisateurService;


    //////////////////////////////////////////////---UE---///////////////////////////////////////////////////
    // Endpoint pour sauvegarder une UE
    // Endpoint pour sauvegarder une UE
    @PostMapping("ajout-ue")
    public ResponseEntity<UEDto> saveUE(@RequestBody UE ue) {
        UE savedUE = ueService.saveUE(ue);
        UEDto ueDto = ueService.convertToDTO(savedUE);
        return new ResponseEntity<>(ueDto, HttpStatus.CREATED);
    }

    @GetMapping("/courbyprofesseur/{professeurId}")
    public List<UEDto> getUEsByProfesseur(@PathVariable Long professeurId) {
        return ueService.getUEsByProfesseur(professeurId);
    }

    // Endpoint pour obtenir tous les cours
    @GetMapping("all-ue")
    public ResponseEntity<List<UEDto>> findAllCours() {
        List<UEDto> coursList = ueService.findAllCours();
        return new ResponseEntity<>(coursList, HttpStatus.OK);
    }
    @GetMapping("/libUe/{ueId}")
    public ResponseEntity<Map<String, String>> getUeLibelleAndCode(@PathVariable Long ueId) {
        Optional<UE> ueOptional = ueService.getUeById(ueId);
        if (ueOptional.isPresent()) {
            UE ue = ueOptional.get();
            Map<String, String> response = new HashMap<>();
            response.put("libelle", ue.getLibelle());
            response.put("code", ue.getCode());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/cahierParUe/{ueId}")
    public ResponseEntity<List<CahierDto>> getCahiersByUeId(@PathVariable Long ueId) {
        List<CahierDto> cahiers = cahierTexteService.findCahiersByUeId(ueId);
        return ResponseEntity.ok(cahiers);
    }

    // Endpoint pour obtenir les cours par parcours
    @GetMapping("/ueByparcours/{parcoursId}")
    public ResponseEntity<List<UEDto>> getCoursByParcours(@PathVariable Long parcoursId) {
        List<UEDto> coursList = ueService.getCoursByFiliere(parcoursId);
        return new ResponseEntity<>(coursList, HttpStatus.OK);
    }

    // Endpoint pour obtenir un cours par ID
    @GetMapping("uebyId/{id}")
    public ResponseEntity<UEDto> getCoursById(@PathVariable Long id) {
        UEDto ueDto = ueService.getCoursById(id);
        if (ueDto != null) {
            return new ResponseEntity<>(ueDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    ///////////////////////////////////////////----Filiere----///////////////////////////////////////////

    @PostMapping("add-filiere")
    public Filiere createFiliere(@RequestBody Filiere filiere) {
        return filiereService.saveFiliere(filiere);
    }

    // Mettre à jour une filière
    @PutMapping("update-filiere/{id}")
    public Filiere updateFiliere(@PathVariable Long id, @RequestBody Filiere filiere) {
        filiere.setFiliereId(id);
        return filiereService.saveFiliere(filiere);
    }
//    @GetMapping("all-filieres-sans-etudiants")
//    public List<FiliereDto> getFilieresSansEtudiants() {
//        return filiereService.getFilieresDto();
//    }

    // Supprimer une filière
    @DeleteMapping("delfiliere/{id}")
    public void deleteFiliere(@PathVariable Long id) {
        filiereService.deleteFiliere(id);
    }

    @GetMapping("/filiereByparcours/{parcoursId}")
    public List<Filiere> getFilieresByParcours(@PathVariable Long parcoursId) {
        return filiereService.getFilieresByParcours(parcoursId);
    }


    ///////////////////////////////////// ----Professeur--------- //////////////////////////////////////

    @GetMapping("all-prof")
    public List<Professeur> getAllProfs() {
        return professeurService.getAllProfesseurs();
    }

    // Récupérer un professeur par son ID
    @GetMapping("profbyid/{id}")
    public Professeur getProfById(@PathVariable Long id) {
        return professeurService.getProfesseurById(id);
    }

    // Créer un nouveau professeur
    @PostMapping("add-prof")
    public Professeur createProf(@RequestBody Professeur professeur) {
        return professeurService.saveProfesseur(professeur);
    }

    // Mettre à jour un professeur
    @PutMapping("update-prof/{id}")
    public Professeur updateProf(@PathVariable Long id, @RequestBody Professeur professeur) {
        professeur.setProfesseurId(id);
        return professeurService.saveProfesseur(professeur);
    }

    // Supprimer un professeur
    @DeleteMapping("delete-prof/{id}")
    public void deleteProf(@PathVariable Long id) {
        professeurService.deleteProfesseur(id);
    }

    ////////////////////////////----{Parcours}----///////////////////////////////////////////////

    @GetMapping("all-parcours")
    public List<Parcours> getAllParcours() {
        return parcoursService.getAllParcours();
    }

    // Récupérer un parcours par son ID
//    @GetMapping("parcoursbyid/{id}")
//    public Parcours getParcoursById(@PathVariable Long id) {
//        return parcoursService.getParcoursById(id);
//    }

    // Créer un nouveau parcours
    @PostMapping("add-parcours")
    public Parcours createParcours(@RequestBody Parcours parcours) {
        return parcoursService.saveParcours(parcours);
    }

    // Mettre à jour un parcours
    @PutMapping("update-parcours/{id}")
    public Parcours updateParcours(@PathVariable Long id, @RequestBody Parcours parcours) {
        parcours.setParcoursId(id);
        return parcoursService.saveParcours(parcours);
    }

    // Supprimer un parcours
    @DeleteMapping("del-parcours/{id}")
    public void deleteParcours(@PathVariable Long id) {
        parcoursService.deleteParcours(id);
    }

    //////////////////////////////////////////////Permission/////////////////////////////////////////////


    @GetMapping("/permission-par-filiere/{filiereId}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByFiliere(@PathVariable Long filiereId) {
        List<PermissionDTO> permissions = permissionService.getPermissionsByFiliere(filiereId);
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/permission-par-filiere/{filiereId}/status/{status}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByFiliereAndStatus(@PathVariable Long filiereId, @RequestParam PermissionStatus status) {
        List<PermissionDTO> permissions = permissionService.getPermissionsByFiliereAndStatus(filiereId, status);
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/permission-filiere/{filiereId}/status/{status}/period")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByPeriod(@PathVariable Long filiereId, @RequestParam PermissionStatus status,
                                                                      @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<PermissionDTO> permissions = permissionService.getPermissionsByPeriod(filiereId, status, startDate, endDate);
        return ResponseEntity.ok(permissions);
    }

    // Approuvee Permission
    @PutMapping("/approuver/{permissionId}/{directeurEtudeId}")
    public ResponseEntity<PermissionDTO> approvePermission(
            @PathVariable Long permissionId,
            @RequestBody Map<String, String> requestBody, // Modification ici
            @PathVariable Long directeurEtudeId) {
        String remarque = requestBody.get("remarque");  // Récupérer la remarque depuis le corps
        PermissionDTO updatedPermission = permissionService.approvePermission(permissionId, remarque, directeurEtudeId);
        return ResponseEntity.ok(updatedPermission);
    }

    @PutMapping("/rejeter/{permissionId}/{directeurEtudeId}")
    public ResponseEntity<PermissionDTO> rejectPermission(
            @PathVariable Long permissionId,
            @RequestBody Map<String, String> requestBody, // Modification ici
            @PathVariable Long directeurEtudeId) {
        String raison = requestBody.get("raison");  // Récupérer la raison depuis le corps
        PermissionDTO updatedPermission = permissionService.rejectPermission(permissionId, raison, directeurEtudeId);
        return ResponseEntity.ok(updatedPermission);
    }


    // Get details of a specific permission by ID
    @GetMapping("/permission-by-id/{permissionId}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable("permissionId") Long permissionId) {
        PermissionDTO permissionDTO = permissionService.getPermissionDTOById(permissionId);
        return ResponseEntity.ok(permissionDTO);
    }

    // Download the permission file
    @GetMapping("/download-permfile/{permissionId}")
    public ResponseEntity<Void> downloadFile(@PathVariable Long permissionId) {
        Optional<Permission> permission = permissionService.getPermissionById(permissionId);

        if (permission.isPresent()) {
            String fileUrl = permission.get().getFileUrl();

            if (fileUrl != null && !fileUrl.isEmpty()) {
                // Redirection vers l'URL publique du fichier
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, fileUrl)
                        .build();
            } else {
                System.out.println("Le champ fileUrl est vide ou nul pour la permission avec ID: " + permissionId);
                return ResponseEntity.status(404).build();
            }
        } else {
            System.out.println("Permission avec ID " + permissionId + " non trouvée.");
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/permission-par-parcours/{parcoursId}")
    public List<PermissionDTO> getPermissionsByParcours(@PathVariable Long parcoursId) {
        return permissionService.getPermissionsByParcoursId(parcoursId);
    }

    @GetMapping("/ueParparcours/{parcoursId}")
    public ResponseEntity<List<UEDto>> getUEByParcours(@PathVariable Long parcoursId) {
        List<UEDto> ueDtos = ueService.getUEByParcours(parcoursId);
        return ResponseEntity.ok(ueDtos);
    }
    ///////////////////////////////////////////----Presence-----///////////////////////////////////////////////////
    /**
     * Récupérer l'étudiant le plus absent pour une UE donnée
     */
    @GetMapping("/ue/{ueId}/mostAbsentStudent")
    public ResponseEntity<String> getMostAbsentStudentName(@PathVariable Long ueId) {
        String mostAbsentStudentName = presenceServices.findMostAbsentStudentNameForUE(ueId);
        return ResponseEntity.ok(mostAbsentStudentName);
    }

    @GetMapping("/ue/{ueId}/absence-info")
    public ResponseEntity<AbsenceInfoResponse> getAbsenceInfo(@PathVariable Long ueId) {
        AbsenceInfoResponse response = presenceServices.getAbsenceInfoForUE(ueId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/historique-presence/ue/{ueId}")
    public List<HistoriquePresence> getHistoriqueByUeId(@PathVariable Long ueId) {
        return presenceServices.getHistoriqueByUeId(ueId);
    }
    @GetMapping("/etudiantbyUE/{ueId}")
    public ResponseEntity<List<EtudiantDTOA>> getEtudiantsByUeId(@PathVariable Long ueId) {
        List<EtudiantDTOA> etudiants = presenceServices.getEtudiantsByUeId(ueId);
        return ResponseEntity.ok(etudiants);
    }
    /**
     * Récupérer le taux de présence de chaque étudiant pour une UE donnée
     */
    @GetMapping("/ue/{ueId}/attendanceRates")
    public List<Map<String, Object>> calculerTauxPresence(@PathVariable Long ueId) {
        return presenceServices.calculerTauxPresence(ueId);
    }
//    public ResponseEntity<List<TauxPresence>> getAttendanceRatesForUE(@PathVariable Long ueId) {
//        List<TauxPresence> tauxPresence = presenceServices.findAttendanceRateForStudentsInUE(ueId);
//        return ResponseEntity.ok(tauxPresence);
//    }

    @PostMapping("/signaler/{etudiantId}")
    public ResponseEntity<Void> signalerAbsence(@PathVariable Long etudiantId) {
        presenceServices.signalerAbsence(etudiantId);
        return ResponseEntity.ok().build();
    }

    /**
     * Récupérer le taux d'absence pour une UE donnée
     */
    @GetMapping("/{ueId}/absenceRate")
    public ResponseEntity<Double> getAbsenceRateForUE(@PathVariable Long ueId) {
        Double tauxAbsence = presenceServices.findAbsenceRateForUE(ueId);
        return ResponseEntity.ok(tauxAbsence);
    }

    /**
     * Récupérer le taux de présence de tous les étudiants d'un parcours donné
     */
    @GetMapping("/parcours/{parcoursId}/attendanceRates")
    public ResponseEntity<List<TauxPresence>> getAttendanceRatesForParcours(@PathVariable Long parcoursId) {
        List<TauxPresence> tauxPresence = presenceServices.findAttendanceRateForStudentsInParcours(parcoursId);
        return ResponseEntity.ok(tauxPresence);
    }

    /**
     * Récupérer le taux de présence de chaque étudiant dans un parcours donné pour une UE spécifique
     */
    @GetMapping("/{ueId}/parcours/{parcoursId}/presenceRateEachStudent")
    public ResponseEntity<List<TauxPresence>> getPresenceRateForEachStudentByUEAndParcours(
            @PathVariable Long ueId, @PathVariable Long parcoursId) {
        List<TauxPresence> tauxPresence = presenceServices.findPresenceRateForEachStudentByUEAndParcours(ueId, parcoursId);
        return ResponseEntity.ok(tauxPresence);
    }


    //////////////////////////////////////////////-seance-//////////////////////

    /**
     * Récupérer le nombre de séances pour une UE donnée
     */
    @GetMapping("/{ueId}/nombreSeances")
    public ResponseEntity<Long> getNombreSeancesForUE(@PathVariable Long ueId) {
        Long nombreSeances = seanceServices.getNombreSeancesByUE(ueId);
        return ResponseEntity.ok(nombreSeances);
    }


    /**
     * Récupérer le taux d'achèvement d'une UE donnée
     */
    @GetMapping("/{ueId}/tauxAchevement")
    public ResponseEntity<Double> getTauxAchevementForUE(@PathVariable Long ueId) {
        Double tauxAchevement = seanceServices.getTauxAchevementUE(ueId);
        return ResponseEntity.ok(tauxAchevement);
    }

    @PutMapping("/signaler/{ueId}")
    public ResponseEntity<Map<String, String>> markUeAsRetard(@PathVariable Long ueId) {
        cahierTexteService.markUeAsRetard(ueId);

        // Retourner une réponse JSON au lieu d'une chaîne simple
        Map<String, String> response = new HashMap<>();
        response.put("message", "L'UE signalée avec succès");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/etudiantInfo/{userId}")
    public ResponseEntity<EtudiantDto> getEtudiantByUserId(@PathVariable Long idUser) {
        EtudiantDto etudiantDto = utilisateurService.getEtudiantByUserId(idUser);
        return ResponseEntity.ok(etudiantDto);
    }

    @GetMapping("/professeurInfo/{userId}")
    public ResponseEntity<ProfesseurDto> getProfesseurByUserId(@PathVariable Long idUser) {
        ProfesseurDto professeurDto = utilisateurService.getProfesseurByUserId(idUser);
        return ResponseEntity.ok(professeurDto);
    }

    @GetMapping("/directeurInfo/{userId}")
    public ResponseEntity<DirecteurEtudeDto> getDirecteurEtudeByUserId(@PathVariable Long userId) {
        try {
            DirecteurEtudeDto directeurEtudeDto = utilisateurService.getDirecteurEtudeByUserId(userId);
            if (directeurEtudeDto != null) {
                return ResponseEntity.ok(directeurEtudeDto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @PostMapping("/add-cours")
//    public ResponseEntity<String> addUe(@RequestBody UE ue) {
//////        // Vérifiez et récupérez le professeur
//////        if (ue.getProfesseur() == null || ue.getProfesseur().getProfesseurId() == null) {
//////            return ResponseEntity.badRequest().body("Le professeur doit être spécifié.");
//////        }
////
////        Professeur professeur = professeurRepository.findById(ue.getProfesseur().getProfesseurId())
////                .orElseThrow(() -> new ResourceNotFoundException("Professeur non trouvé."));
////        ue.setProfesseur(professeur);
//
//        // Vérifiez et récupérez les filières
//        if (ue.getFilieres() == null || ue.getFilieres().isEmpty()) {
//            return ResponseEntity.badRequest().body("Aucune filière spécifiée.");
//        }
//
//        List<Filiere> persistedFilieres = new ArrayList<>();
//        for (Filiere filiere : ue.getFilieres()) {
//            if (filiere.getFiliereId() == null) {
//                return ResponseEntity.badRequest().body("Une filière doit avoir un ID valide.");
//            }
//            Filiere persistedFiliere = filiereRepository.findById(filiere.getFiliereId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Filière non trouvée."));
//            persistedFilieres.add(persistedFiliere);
//        }
//        ue.setFilieres(persistedFilieres);
//
//        // Ajout de l'UE
//        UE newUe = ueService.addUe(ue);
//        return ResponseEntity.ok("UE ajoutée avec succès : " + newUe.getLibelle());
//    }

    @PostMapping("/add-cours")
    public ResponseEntity<?> addCours(@RequestBody UeDto ueDto) {
        try {
            ueService.addCours(ueDto);
            return ResponseEntity.ok("Cours ajouté avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Une erreur est survenue: " + e.getMessage());
        }
    }

    @GetMapping("/professors")
    public ResponseEntity<List<ProfesseurDto>> getAllProfessors() {
        List<ProfesseurDto> professors = professeurService.getAllProfessors();
        return ResponseEntity.ok(professors);
    }

    // Fetch all filieres as FiliereDto for a given parcours
    @GetMapping("/filieres/{parcoursId}")
    public ResponseEntity<List<FiliereDto>> getFilieresDTOByParcours(@PathVariable Long parcoursId) {
        List<FiliereDto> filieres = filiereService.getFilieresDTOByParcours(parcoursId);
        return ResponseEntity.ok(filieres);
    }


    // Fetch enumeration values for NiveauEtude and TypeSemestre
    @GetMapping("/enums/niveau-etude")
    public ResponseEntity<NiveauEtude[]> getAllNiveauEtude() {
        return ResponseEntity.ok(NiveauEtude.values());
    }

    @GetMapping("/enums/type-semestre")
    public ResponseEntity<TypeSemestre[]> getAllTypeSemestre() {
        return ResponseEntity.ok(TypeSemestre.values());
    }












    //////////////////////////////////////////////---Notes---///////////////////////////////////////////////////


    //
    @GetMapping("/toutefiliere")
    public List<FiliereDto> getAllFilieres() {
        return filiereService.getAllFilieres();
    }
    //PAGE UE
    //Ajouter une UE
    @PostMapping("/noteue")
    public ResponseEntity<UejeffDto> addUe(@RequestBody UejeffDto uejeffDto) {
        UejeffDto createdUe = uejeffService.addUe(uejeffDto);
        return new ResponseEntity<>(createdUe, HttpStatus.CREATED);
    }
    //Supprimer une UE
    @DeleteMapping("/noteue/{id}")
    public ResponseEntity<String> supprimerUe(@PathVariable Long id) {
        uejeffService.supprimerUe(id);
        return ResponseEntity.ok().body("{\"message\": \"L'UE a été supprimée avec succès.\"}");
    }
    //Modifier une UE
    @PutMapping("/noteue/{id}")
    public ResponseEntity<UejeffDto> modifierUe(@PathVariable Long id, @RequestBody UejeffDto uejeffDto) {
        // Log pour vérifier l'ID reçu dans le backend
        System.out.println("Mise à jour de l'UE avec l'ID : " + id);
        UejeffDto updatedUe = uejeffService.modifierUe(id, uejeffDto);
        return ResponseEntity.ok(updatedUe);
    }
    //Récuperer un UE ou une liste
    @GetMapping("/noteue")
    public ResponseEntity<List<UejeffDto>> obtenirToutesLesUes() {
        List<UejeffDto> ueList = uejeffService.obtenirToutesLesUes();
        return ResponseEntity.ok(ueList);
    }
    @GetMapping("/noteue/{id}")
    public ResponseEntity<UejeffDto> obtenirUeParId(@PathVariable Long id) {
        UejeffDto ue = uejeffService.obtenirUeParId(id);
        return ResponseEntity.ok(ue);
    }

    //PAGE NOTE
    //Récupérer les Ue d'un semestre donné
    @GetMapping("/noteue/semestre/{typeSemestre}")
    public ResponseEntity<List<UejeffDto>> getUEsBySemestre(@PathVariable TypeSemestre typeSemestre) {
        try {
            List<UejeffDto> ues = uejeffService.getUesBySemestre(typeSemestre);
            if (ues.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ues);
        } catch (Exception e) {
            // Log de l'erreur pour plus de détails dans la console
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Récupérer les étudiants d'un niveau en fontion du semestre
    @GetMapping("/etudiants/semestre/{typesemestre}")
    public ResponseEntity<List<EtudiantjeffDto>> getEtudiantsParSemestre(@PathVariable TypeSemestre typesemestre) {
        List<EtudiantjeffDto> etudiants = etudiantjeffService.getEtudiantsParSemestre(typesemestre);
        if (etudiants.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(etudiants);
    }
    //Pour enrégistrer les notes
    @PostMapping("/notes/enregistrer")
    public ResponseEntity<?> enregistrerNotes(@RequestBody List<SaisieNoteDto> saisieNotes) {
        System.out.println("Données reçues : " + saisieNotes);  // Ajout de logs pour voir les données reçues
        try {
            notesService.enregistrerNotes(saisieNotes);

            // Créer une réponse JSON avec un message de succès
            Map<String, String> response = new HashMap<>();
            response.put("message", "Notes enregistrées avec succès");
            return ResponseEntity.ok(response); // Renvoie une réponse HTTP 200 avec du JSON

        } catch (RuntimeException e) {
            // Gérer les exceptions et renvoyer une réponse d'erreur
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // Renvoie une erreur 500 avec du JSON
        }
    }

    //listes des UE qui ont reçu les notes par semestre
    @GetMapping("/ues/notes/{typeSemestre}")
    public ResponseEntity<List<UejeffDto>> getUesWithNotesBySemestre(@PathVariable TypeSemestre typeSemestre) {
        List<UejeffDto> ues = uejeffService.getUesWithNotesBySemestreAndTypeNote(typeSemestre);

        if (ues.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ues);
    }

    // Endpoint pour récupérer les notes des Etudiants d'une UE
    @GetMapping("/ues/{ueId}/notes/{typeNote}")
    public ResponseEntity<List<NoteEtudiantDto>> getNotesEtudiantsParUEEtTypeNote(
            @PathVariable Long ueId,
            @PathVariable TypeNote typeNote) {
        List<NoteEtudiantDto> notesEtudiants = notesService.getNotesEtudiantsParUEEtTypeNote(ueId, typeNote);
        if (notesEtudiants.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notesEtudiants);
    }

    // Endpoint pour mettre à jour les notes d'une UE
    @PutMapping("/notes/modifier")
    public ResponseEntity<?> modifierNotes(@RequestBody List<SaisieNoteDto> saisieNotes) {
        // Log des données reçues
        System.out.println("Données reçues pour modification : " + saisieNotes);

        // Appel du service de modification
        notesService.modifierNotes(saisieNotes);

        // Réponse JSON après modification
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notes modifiées avec succès");
        return ResponseEntity.ok(response);
    }

    // Endpoint pour supprimer toutes les notes d'une UE
    @DeleteMapping("/notes/{ueId}/{typeNote}")
    public ResponseEntity<?> supprimerNotes(@PathVariable Long ueId, @PathVariable TypeNote typeNote) {
        try {
            notesService.supprimerNotesParUEEtType(ueId, typeNote);
            return ResponseEntity.ok("Notes supprimées avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression des notes : " + e.getMessage());
        }
    }

    //PAGE ETUDIANT
    // Récupérer les étudiants avec au moins une note
    @GetMapping("/noteetudiant/with-notes")
    public List<EtudiantjeffDto> getEtudiantsWithNotes() {
        return etudiantjeffService.getEtudiantsWithNotes();
    }

    // Récupérer les notes d'un étudiant
    @GetMapping("/noteetudiant/{id}")
    public List<NoteDto> getNotesByEtudiant(@PathVariable Long id) {
        return notesService.getNotesByEtudiant(id);
    }

    //Modifier les notes de l'étudiant
    // Récupérer une note par son ID
    @GetMapping("/note/{noteId}")
    public NoteDto getNoteById(@PathVariable Long noteId) {
        return notesService.getNoteById(noteId);
    }
    // Méthode pour mettre à jour une note
    @PutMapping("/note/{noteId}")
    public NoteDto updateNoteEtudiant(@PathVariable Long noteId, @RequestBody NoteDto noteDto) {
        return notesService.updateNoteEtudiant(noteId, noteDto);
    }

    //Supprimer une note d'un étudiant
    @DeleteMapping("/note/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable Long noteId) {
        notesService.deleteNote(noteId);
        return ResponseEntity.ok().build();
    }
    //PAGE BULLETIN
    // Endpoint pour récupérer les étudiants par niveau
    @GetMapping("/bulletin/niveau/{niveau}")
    public ResponseEntity<List<EtudiantjeffDto>> getEtudiantsParNiveau(@PathVariable NiveauEtude niveau) {
        List<EtudiantjeffDto> etudiants = etudiantjeffService.getEtudiantsByNiveau(niveau);
        return ResponseEntity.ok(etudiants);
    }

    // Endpoint pour récupérer les informations spécifiques d'un étudiant par son ID
    @GetMapping("/bulletin/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id) {
        Etudiant etudiant = etudiantjeffService.getEtudiantById(id);
        if (etudiant != null) {
            return ResponseEntity.ok(etudiant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Endpoint pour récupérer les notes en fonction du niveau de l'étudiant
    @GetMapping("/bulletin/{etudiantId}/notes")
    public ResponseEntity<List<Note>> getNotesParNiveauEtude(@PathVariable Long etudiantId) {
        List<Note> notes = etudiantjeffService.getNotesParNiveauEtude(etudiantId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/bulletin/{id}/semestres/moyennes")
    public ResponseEntity<Map<TypeSemestre, Map<String, Object>>> getStatistiquesParSemestre(@PathVariable Long id) {
        Etudiant etudiant = etudiantjeffService.getEtudiantById(id);
        if (etudiant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Map<TypeSemestre, List<Note>> notesBySemestre = etudiantjeffService.getNotesBySemestres(etudiant);
        Map<TypeSemestre, Map<String, Object>> statistiquesBySemestre = new HashMap<>();

        for (Map.Entry<TypeSemestre, List<Note>> entry : notesBySemestre.entrySet()) {
            List<Note> notesSemestre = entry.getValue();
            Map<String, Object> stats = new HashMap<>();

            if (!notesSemestre.isEmpty()) {
                double moyenneSemestre = notesService.calculerMoyenneSemestre(notesSemestre);
                int creditsValides = notesService.calculerCreditsValides(notesSemestre);

                // Ajouter la moyenne et les crédits dans la Map
                stats.put("moyenne", moyenneSemestre);
                stats.put("creditsValides", creditsValides);
            } else {
                // Si pas de notes, on met la moyenne à 0 et les crédits à 0
                stats.put("moyenne", 0.0);
                stats.put("creditsValides", 0);
            }

            statistiquesBySemestre.put(entry.getKey(), stats);
        }

        return ResponseEntity.ok(statistiquesBySemestre);
    }


    @GetMapping("/etudiants/{id}/ue-details-par-semestre")
    public ResponseEntity<Map<String, Object>> getUeDetailsParSemestre(@PathVariable Long id) {
        Etudiant etudiant = etudiantjeffService.getEtudiantById(id);
        if (etudiant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Récupérer les notes par semestre pour cet étudiant
        Map<TypeSemestre, List<Note>> notesBySemestre = etudiantjeffService.getNotesBySemestres(etudiant);
        Map<TypeSemestre, Map<String, Object>> ueDetailsBySemestre = new HashMap<>();

        double moyenneNiveau = 0.0;
        int totalSemestres = 0;

        Map<String, Double> moyennesSemestres = new HashMap<>();

        for (Map.Entry<TypeSemestre, List<Note>> entry : notesBySemestre.entrySet()) {
            TypeSemestre semestre = entry.getKey();
            List<Note> notesSemestre = entry.getValue();

            // Grouper les notes par UE
            Map<UE, List<Note>> notesByUE = notesSemestre.stream().collect(Collectors.groupingBy(Note::getUe));
            List<Map<String, Object>> ueDetailsList = new ArrayList<>();

            int totalCreditsSemestre = 0;
            int creditsValides = 0;
            double moyenneSemestre = 0.0;

            for (Map.Entry<UE, List<Note>> ueEntry : notesByUE.entrySet()) {
                UE ue = ueEntry.getKey();
                List<Note> notesUE = ueEntry.getValue();

                // Calculer la moyenne de l'UE
                double moyenneUE = notesService.calculerMoyenneUE(notesUE);

                // Déterminer si l'UE est validée ou non (moyenne supérieure à 10)
                String valide = (moyenneUE >= 10) ? "OUI" : "NON";

                // Calculer l'appréciation
                String appreciation = notesService.AvoirAppreciation(moyenneUE);

                // Mettre à jour le total des crédits
                totalCreditsSemestre += ue.getCredit();
                if (moyenneUE >= 10) {
                    creditsValides += ue.getCredit();
                }

                // Construire les détails de l'UE
                Map<String, Object> ueDetails = new HashMap<>();
                ueDetails.put("codeUE", ue.getCode());
                ueDetails.put("libelle", ue.getLibelle());
                ueDetails.put("type", ue.getTypeUe());
                ueDetails.put("credit", ue.getCredit());
                ueDetails.put("note", moyenneUE);
                ueDetails.put("valide", valide);
                ueDetails.put("appreciation", appreciation);
                ueDetails.put("semestre", semestre);

                // Ajouter les détails de l'UE à la liste
                ueDetailsList.add(ueDetails);
            }

            // Calculer la moyenne du semestre
            moyenneSemestre = notesService.calculerMoyenneSemestre(notesSemestre);
            moyenneNiveau += moyenneSemestre;
            totalSemestres++;

            moyennesSemestres.put("moyenne" + semestre.name(), moyenneSemestre);
            // Ajouter les détails du semestre à la map

            Map<String, Object> semestreDetails = new HashMap<>();
            semestreDetails.put("ueDetails", ueDetailsList);
            semestreDetails.put("creditsValides", creditsValides);
            semestreDetails.put("totalCredits", totalCreditsSemestre);

            ueDetailsBySemestre.put(semestre, semestreDetails);
        }

        // Calculer la moyenne globale du niveau
        moyenneNiveau = (totalSemestres > 0) ? (moyenneNiveau / totalSemestres) : 0.0;
        String mention = notesService.AppreciMoyenne(moyenneNiveau);

        // Préparer la réponse
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("ueDetailsBySemestre", ueDetailsBySemestre);
        // Ajouter les moyennes des semestres à la fin de la réponse
        for (Map.Entry<String, Double> moyenneEntry : moyennesSemestres.entrySet()) {
            response.put(moyenneEntry.getKey(), moyenneEntry.getValue());
        }
        response.put("moyenneNiveau", moyenneNiveau);
        response.put("mention", mention);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/etudiants/{id}/bulletin-pdf")
    public void genererBulletinPDF(@PathVariable Long id, HttpServletResponse response) {
        try {
            bulletinPdfService.genererBulletinPDF(id, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/etudiants/{id}/bulletin-excel")
    public void genererBulletinExcel(@PathVariable Long id, HttpServletResponse response) {
        try {
            excelExportService.exporterBulletinExcel(id, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/bulletin/envoyer/{etudiantId}")
    public ResponseEntity<String> envoyerBulletin(@PathVariable Long etudiantId) {
        try {
            bulletinPdfService.genererEtEnvoyerBulletinPDF(etudiantId);
            return ResponseEntity.ok("Bulletin envoyé avec succès à l'étudiant.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'envoi du bulletin.");
        }
    }

    //tableau de bord
    @GetMapping("/nombre-ue")
    public ResponseEntity<Map<String, Long>> getNombreUE() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalUE", uejeffService.getTotalUE());
        return ResponseEntity.ok(stats);
    }

    // Endpoint pour obtenir le nombre total d'UE en Première Année
    @GetMapping("/count/premiere-annee")
    public long getUECountPremiereAnnee() {
        return uejeffService.countUEPremiereAnnee();
    }

    // Endpoint pour obtenir le nombre total d'UE en Deuxième Année
    @GetMapping("/count/deuxieme-annee")
    public long getUECountDeuxiemeAnnee() {
        return uejeffService.countUEDeuxiemeAnnee();
    }

    // Endpoint pour obtenir le nombre total d'UE en Troisième Année
    @GetMapping("/count/troisieme-annee")
    public long getUECountTroisiemeAnnee() {
        return uejeffService.countUETroisiemeAnnee();
    }

    @GetMapping("/nombre-etudiants")
    public ResponseEntity<Map<String, Long>> getNombreEtudiants() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalEtudiants", etudiantjeffService.getTotalEtudiants());
        stats.put("etudiantsPremiereAnnee", etudiantjeffService.getTotalEtudiantsParNiveau(NiveauEtude.PREMIERE_ANNEE));
        stats.put("etudiantsDeuxiemeAnnee", etudiantjeffService.getTotalEtudiantsParNiveau(NiveauEtude.DEUXIEME_ANNEE));
        stats.put("etudiantsTroisiemeAnnee", etudiantjeffService.getTotalEtudiantsParNiveau(NiveauEtude.TROISIEME_ANNEE));
        return ResponseEntity.ok(stats);
    }

    // Endpoint pour obtenir le taux de réussite par UE
    @GetMapping("/taux-reussite-ue")
    public ResponseEntity<Map<String, Double>> getTauxReussiteParUe() {
        Map<UE, Double> tauxReussite = uejeffService.calculerTauxReussiteParUe();
        // Transformer la réponse en une Map avec le nom de l'UE et le taux
        Map<String, Double> tauxReussiteDto = tauxReussite.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getLibelle(),
                        Map.Entry::getValue
                ));
        return ResponseEntity.ok(tauxReussiteDto);
    }

    // Endpoint pour obtenir la distribution des notes
    @GetMapping("/distribution-notes")
    public ResponseEntity<Map<String, Long>> getDistributionDesNotes() {
        Map<String, Long> distributionNotes = notesService.calculerDistributionDesNotes();
        return ResponseEntity.ok(distributionNotes);
    }

    @GetMapping("/etudiants/top5")
    public ResponseEntity<List<EtudiantMoyenneDto>> getTop5EtudiantsByMoyenne() {
        List<EtudiantMoyenneDto> top5 = etudiantjeffService.getTop5EtudiantsByMoyenne();
        return ResponseEntity.ok(top5);
    }

    @GetMapping("/niveaux/stats")
    public ResponseEntity<List<NiveauStatsDto>> getStatsParNiveau() {
        List<NiveauStatsDto> stats = etudiantjeffService.getStatsParNiveau();
        return ResponseEntity.ok(stats);
    }
}
