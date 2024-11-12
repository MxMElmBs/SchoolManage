package com.defitech.GestUni.controller;

import com.defitech.GestUni.dto.*;
import com.defitech.GestUni.dto.Azhar.CahierDto;
import com.defitech.GestUni.dto.Azhar.PermissionDTO;
import com.defitech.GestUni.dto.Note.EtudiantNoteMaxDto;
import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.Azhar.CahierTexte;
import com.defitech.GestUni.models.Azhar.Seance;
import com.defitech.GestUni.models.Chahib.Document;
import com.defitech.GestUni.models.Chahib.TypeDocument;
import com.defitech.GestUni.service.Azhar.CahierTexteService;
import com.defitech.GestUni.service.Azhar.SeanceServices;


import com.defitech.GestUni.service.Chahib.DocumentService;
import com.defitech.GestUni.service.Chahib.EtudiantDocServices;
import com.defitech.GestUni.service.Chahib.GoogleCloudStorageService;
import com.defitech.GestUni.service.Max.EtudiantCoursService;
import com.defitech.GestUni.service.Max.EtudiantMaxConnect;
import com.defitech.GestUni.service.UtilisateurService;
import com.google.cloud.storage.Blob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import com.defitech.GestUni.models.Azhar.Permission;
import com.defitech.GestUni.service.Azhar.PermissionService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth/etudiant")
public class EtudiantController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private CahierTexteService cahierTexteService;
    @Autowired
    private SeanceServices seanceService;
    @Autowired
    private EtudiantCoursService etudiantCoursService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private EtudiantDocServices etudiantCoService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    @Qualifier("chahibGoogleCloudStorageService")
    private GoogleCloudStorageService googleCloudStorageService;

    private static final Logger log = LoggerFactory.getLogger(EtudiantController.class);

    ////////////////DOCUMENT////////////////////////


    //////////////////////////////////ETUD CONNECTE////////////////////
    @GetMapping("/etudiantdocs/{idUser}")
    public ResponseEntity<EtudiantDocDto> getEtudiantByUserId2(@PathVariable Long idUser) {
        EtudiantDocDto etudiantDoc = etudiantCoService.getEtudiantByUserId(idUser);
        return ResponseEntity.ok(etudiantDoc);
    }





    //////////////////////////////////FIN EC//////////////////////////////

///////////////////////////////////////Document///////////////////////////////////////////////

    @PostMapping("/add-document")
    public ResponseEntity<String> addDocument(@RequestBody CreateDocumentDto createDocumentDto) {
        String result = documentService.addDocument(createDocumentDto);
        return result.equals("Vous avez creer un nouveau document.")
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/search-document")
    public ResponseEntity<List<Document>> searchDocuments(
            @RequestParam(required = false) String theme) {
        List<Document> documents = documentService.searchDocuments(theme);
        return ResponseEntity.ok(documents);
    }

    @PostMapping(value = "/verify-update-document", consumes = "multipart/form-data")
    public ResponseEntity<String> verifyAndUpdateDocumentWithFile(
            @RequestParam Long documentId,
            @RequestParam(required = false) String introduction,
            @RequestParam(required = false) String Problematique,
            @RequestParam(required = false) String Etude_critique_existant,
            @RequestParam(required = false) String resumeDoc,
            @RequestParam(required = false) String conclusion,
            @RequestPart(name = "file", required = false) MultipartFile file) {

        try {
            // Créez un nouvel objet FormulaireVerification à partir des parties individuelles
            FormulaireVerification formulaire = new FormulaireVerification();
            formulaire.setIntroduction(introduction);
            formulaire.setProblematique(Problematique);
            formulaire.setEtude_critique_existant(Etude_critique_existant);
            formulaire.setResumeDoc(resumeDoc);
            formulaire.setConclusion(conclusion);

            // Appel du service fusionné pour vérifier et mettre à jour le document
            String result = documentService.verifyAndUpdateDocumentWithFile(documentId, formulaire, file);

            if (result.equals("Document mis à jour avec succès.")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);  // Si la vérification échoue
            }
        } catch (Exception e) {
            // Gestion des erreurs et réponse appropriée
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("documentby/{documentId}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long documentId) {
        Optional<Document> document = documentService.findById(documentId);
        return document.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/verifyformulaire-document")
    public ResponseEntity<Map<String, Object>> verifySimilarityWithoutSaving(
            @RequestBody FormulaireVerification formulaire,
            @RequestParam(required = false) Long excludeDocumentId) {

        // Appel du service pour vérifier la similarité
        Map<String, Object> result = documentService.verifySimilarityWithoutSaving(formulaire, excludeDocumentId);

        if ("success".equals(result.get("status"))) {
            // Retourner une réponse de succès si la similarité est en dessous du seuil
            return ResponseEntity.ok(result);
        } else {
            // Retourner une réponse de rejet si la similarité dépasse le seuil
            return ResponseEntity.status(400).body(result);
        }
    }


    // Endpoint pour obtenir les documents d'un étudiant spécifique
    @GetMapping("/documentbyetudiant/{etudiantId}")
    public ResponseEntity<List<Document>> getDocumentsByEtudiantId(@PathVariable Long etudiantId) {
        List<Document> documents = documentService.getDocumentsByEtudiantId(etudiantId);

        if (documents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(documents);
    }


    ///////////////////////FIN DOCUMENT//////////////////////////


    /////////////INTERFACE ETUDIANT//////////////////

    //API pour récupérer tous les cours d'un étudiant
    /*@GetMapping("/{idUser}/cours")
    public ResponseEntity<List<EtudiantCoursDto>> getCoursByIdUser(@PathVariable Long idUser) {
        List<EtudiantCoursDto> dtoList = etudiantCoursService.getCoursByIdUser(idUser);
        return ResponseEntity.ok(dtoList);
    }*/

    //API pour récupérer les cours d'un étudiant selon le semestre
    @GetMapping("/{idUser}/semestre/{semestre}")
    public ResponseEntity<List<EtudiantCoursDto>> getCoursByIdUserAndSemestre(
            @PathVariable Long idUser,
            @PathVariable TypeSemestre semestre) {

        List<EtudiantCoursDto> dtoList = etudiantCoursService.getCoursByIdUserAndSemestre(idUser, semestre);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/connect/{userId}")
    public EtudiantMaxConnect getConnectedEtudiant(@PathVariable Long userId) {
        return etudiantCoursService.getEtudiantMaxConnect(userId);
    }

    @GetMapping("/{idUser}/cours")
    public List<EtudiantCoursDto> getCoursByUserId(@PathVariable Long idUser) {
        return etudiantCoursService.getCoursByIdUtilisateur(idUser);
    }

    @GetMapping("/{idUser}/notemax")
    public ResponseEntity<List<EtudiantNoteMaxDto>> getNotesByEtudiantFiliere(
            @PathVariable Long idUser) {

        List<EtudiantNoteMaxDto> notes = etudiantCoursService.getNotesByEtudiantIdAndFiliere(idUser);
        return ResponseEntity.ok(notes);
    }
    /////////////FIN IE/////////////////////////////

//    @PostMapping("/ajoutee-permission/{etudiantId}")
//    public ResponseEntity<String> addPermission(
//            @PathVariable Long etudiantId,
//            @RequestParam LocalDate dateDebutAbsence,
//            @RequestParam LocalDate dateFinAbsence,
//            @RequestParam String raison,
//            @RequestParam String description,
//            @RequestParam(value = "fichier", required = false) MultipartFile fichier) {
//        try {
//            permissionService.ajoutPermission(etudiantId, dateDebutAbsence, dateFinAbsence, raison, description, fichier);
//            return ResponseEntity.ok("Demande de permission ajoutée avec succès.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur: " + e.getMessage());
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du traitement du fichier.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur inattendue: " + e.getMessage());
//        }
//    }


    @GetMapping("/permission-by-etudiant/{etudiantId}")
    public List<PermissionDTO> getPermissionsByStudent(@PathVariable Long etudiantId) {
        return permissionService.getPermissionsByStudent(etudiantId);
    }


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


    ///////
    @PostMapping("/enregistrer-cahier/{seanceId}")
    public ResponseEntity<CahierTexte> createCahierTexteByProf(
            @PathVariable Long seanceId,
            @RequestBody CahierTexte cahierTexteDTO) {
        CahierTexte cahierTexte = cahierTexteService.createCahierTexte(seanceId, cahierTexteDTO);
        return ResponseEntity.ok(cahierTexte);
    }


    @GetMapping("/cahierparfiliere/{etudiantId}")
    public ResponseEntity<List<CahierDto>> getCahiersByEtudiantId(@PathVariable Long etudiantId) {
        List<CahierDto> cahiers = cahierTexteService.findCahiersByEtudiantId(etudiantId);
        return ResponseEntity.ok(cahiers);
    }

    @GetMapping("/seance/open-without-cahier/{etudiantId}")
    public ResponseEntity<List<Seance>> getOpenSeancesWithoutCahierByEtudiant(@PathVariable Long etudiantId) {
        List<Seance> seances = seanceService.getOpenSeancesWithoutCahierByEtudiant(etudiantId);
        return ResponseEntity.ok(seances);
    }




    /////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////§§§§§§§§§§§§§§§§////////////////////////


    @PostMapping("/add-permission/{id}")
    public ResponseEntity<?> ajoutPermission(
            @PathVariable("id") Long etudiantId,
            @RequestParam("dateDebutAbsence") LocalDate dateDebutAbsence,
            @RequestParam("dateFinAbsence") LocalDate dateFinAbsence,
            @RequestParam("raison") String raison,
            @RequestParam("description") String description,
            @RequestParam(name = "file", required = false) MultipartFile file) {

        log.info("Ajout d'une permission pour l'étudiant avec ID: {}", etudiantId);
        log.debug("dateDebutAbsence: {}, dateFinAbsence: {}, raison: {}, description: {}",
                dateDebutAbsence, dateFinAbsence, raison, description);

        try {
            // Log the file information if it exists
            if (file != null && !file.isEmpty()) {
                log.info("Fichier uploadé: {}", file.getOriginalFilename());
            } else {
                log.info("Aucun fichier n'a été uploadé.");
            }

            // Appel du service pour ajouter la permission
            Permission permission = permissionService.addPermission(etudiantId, dateDebutAbsence, dateFinAbsence, raison, description, file);

            log.info("Permission ajoutée avec succès pour l'étudiant avec ID: {}", etudiantId);
            return ResponseEntity.ok(permission);

        } catch (IllegalArgumentException e) {
            // Log exception details for specific validation errors
            log.error("Erreur de validation: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erreur de validation: " + e.getMessage());

        } catch (Exception e) {
            // Log any other exceptions
            log.error("Erreur lors de l'ajout de la permission: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }



    @GetMapping("/etudiantInfo/{userId}")
    public ResponseEntity<EtudiantDto> getEtudiantByUserId(@PathVariable Long userId) {
        EtudiantDto etudiantDto = utilisateurService.getEtudiantByUserId(userId);
        return ResponseEntity.ok(etudiantDto);
    }

}
