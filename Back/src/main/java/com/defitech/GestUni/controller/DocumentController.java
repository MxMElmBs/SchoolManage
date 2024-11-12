package com.defitech.GestUni.controller;

import com.defitech.GestUni.dto.*;
import com.defitech.GestUni.models.Chahib.Document;
import com.defitech.GestUni.models.Chahib.StatutDocument;
import com.defitech.GestUni.models.Chahib.TypeDocument;
import com.defitech.GestUni.repository.ProfesseurRepository;
import com.defitech.GestUni.service.Chahib.*;
import com.defitech.GestUni.service.UtilisateurService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    @Qualifier("chahibEmailService")
    private EmailService emailService;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private EtudiantChahibService etudiantChahibService;

    @Autowired
    private EtudiantDocServices etudiantDocServices;

    @Autowired
    private UtilisateurService userService;



    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    // Endpoint pour ajouter un document
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addDocument(@RequestBody CreateDocumentDto createDocumentDto) {
        String result = documentService.addDocument(createDocumentDto);

        // Journalisation pour vérifier la valeur de "result"
        logger.info("Résultat de l'ajout de document: " + result);

        Map<String, String> response = new HashMap<>();
        if (result.equals("Un nouveau document créé avec succès.")) {
            Document document = documentService.findByTheme(createDocumentDto.getTheme()).get(0);
            response.put("message", result);
            logger.info("Document créé avec succès : " + result);
            return ResponseEntity.ok(response);
        } else {
            response.put("error", result);
            logger.warn("Erreur lors de la création du document : " + result);
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Vérification si un thème existe déjà
    @GetMapping("/checkTheme")
    public ResponseEntity<Boolean> checkThemeExists(@RequestParam String theme) {
        boolean exists = documentService.themeExists(theme); // Méthode à définir dans le service
        return ResponseEntity.ok(exists);
    }

    // Endpoint pour vérifier si un document existe pour un étudiant et un type de document donné
    @GetMapping("/checkDocumentExists")
    public ResponseEntity<Map<String, String>> checkDocumentExists(
            @RequestParam Long etudiantId,
            @RequestParam String typeDocument) {

        Map<String, String> response = new HashMap<>();

        // Convertir le typeDocument en TypeDocument Enum
        TypeDocument documentType;
        try {
            // Compare en tenant compte de la casse exacte de l'énumération
            documentType = TypeDocument.valueOf(typeDocument);
        } catch (IllegalArgumentException e) {
            response.put("message", "Type de document invalide.");
            return ResponseEntity.badRequest().body(response);  // Retourne une réponse d'erreur si le type est invalide
        }

        // Vérifier si un document de ce type existe déjà pour l'étudiant
        boolean exists = documentService.checkDocumentExists(etudiantId, documentType);
        if (exists) {
            response.put("message", "Un document de type " + typeDocument + " existe déjà pour cet étudiant.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Aucun document de type " + typeDocument + " n'existe pour cet étudiant. Vous pouvez créer un nouveau document.");
            return ResponseEntity.ok(response);
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


    // Endpoint pour vérifier la similarité et mettre à jour le document
    @PutMapping("/verifyformulaire/{documentId}")
    public ResponseEntity<Map<String, Object>> verifyAndUpdateDocument(
            @PathVariable Long documentId,
            @RequestBody FormulaireVerification formulaire) {

        // Appel au service pour vérifier la similarité et éventuellement mettre à jour le document
        Map<String, Object> result = documentService.verifyAndUpdateDocumentWithDetails(documentId, formulaire);

        // Si la vérification a réussi
        if ("success".equals(result.get("status"))) {
            return ResponseEntity.ok(Map.of("message", "Le document a été vérifié et mis à jour avec succès."));
        } else {
            // Sinon, on renvoie les détails du rejet
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
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

    // Endpoint pour obtenir tous les documents
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.findAll();
        return ResponseEntity.ok(documents);
    }

    // Endpoint pour rechercher des documents selon le thème, type de document et nom de parcours
    @GetMapping("/search")
    public ResponseEntity<List<Document>> searchDocuments(
            @RequestParam(required = false) String theme) {
        List<Document> documents = documentService.searchDocuments(theme);
        return ResponseEntity.ok(documents);
    }

    // Endpoint pour mettre à jour le statut d'un document
    @PutMapping("/update-status/{documentId}")
    public ResponseEntity<String> updateDocumentStatus(
            @PathVariable Long documentId,
            @RequestParam StatutDocument statut) {
        Optional<Document> documentOptional = documentService.findById(documentId);
        if (documentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document non trouvé.");
        }

        Document document = documentOptional.get();
        documentService.updateDocumentStatus(document, statut);
        return ResponseEntity.ok("Statut du document mis à jour avec succès.");
    }

    // Endpoint pour mettre à jour un document avec un fichier
    @PutMapping("/updatedocument/{documentId}")
    public ResponseEntity<String> updateDocument(
            @PathVariable Long documentId,
            @RequestParam(value = "fichier", required = false) MultipartFile fichier) {

        if (fichier == null || fichier.isEmpty()) {
            logger.warn("Aucun fichier fourni pour la mise à jour du document ID: {}", documentId);
            return ResponseEntity.badRequest().body("Aucun fichier n'a été fourni pour la mise à jour.");
        }

        try {
            Optional<Document> documentOptional = documentService.findById(documentId);
            if (documentOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document non trouvé.");
            }

            Document oldDocument = documentOptional.get();
            String result = documentService.updateDocument(documentId, fichier);

            if (result.equals("Document mis à jour avec succès.")) {
                Document updatedDocument = documentService.findById(documentId).orElseThrow();
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }

        } catch (IOException | GeneralSecurityException e) {
            logger.error("Erreur lors de la mise à jour du document ID: {}", documentId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du document: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Paramètre invalide: " + e.getMessage());
        }
    }

    // Endpoint pour mettre à jour le professeur associé à un document
    @PutMapping("/{documentId}/professeur")
    public ResponseEntity<String> updateDocumentProfessor(
            @PathVariable Long documentId,
            @RequestParam("newProfesseurId") Long newProfesseurId) {
        try {
            String result = documentService.updateDocumentProfessor(documentId, newProfesseurId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint pour vérifier la similarité d'un formulaire sans enregistrer
    @PostMapping("/verifyformulaire")
    public ResponseEntity<Map<String, Object>> verifySimilarityWithoutSaving(
            @RequestBody FormulaireVerification formulaire,
            @RequestParam(required = false) Long excludeDocumentId) {

        Map<String, Object> result = documentService.verifySimilarityWithoutSaving(formulaire, excludeDocumentId);
        if ("success".equals(result.get("status"))) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    // Endpoint pour obtenir un document par son ID
    @GetMapping("/documentby/{documentId}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long documentId) {
        Optional<Document> document = documentService.findById(documentId);
        return document.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint pour obtenir les détails d'un étudiant en fonction de son ID utilisateur
    @GetMapping("/etudiant-by-user/{userId}/details")
    public ResponseEntity<EtudiantDetailsDto> getEtudiantDetailsByUserId(@PathVariable Long userId) {
        Optional<EtudiantDetailsDto> etudiantDetailsOpt = etudiantChahibService.getEtudiantDetailsByUserId(userId);

        if (etudiantDetailsOpt.isPresent()) {
            return ResponseEntity.ok(etudiantDetailsOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{documentId}/details")
    public ResponseEntity<EtudiantDetailsDto> getEtudiantDetailsDtoByDocsId(@PathVariable Long documentId) {
        EtudiantDetailsDto etudiantDetailsOpt = etudiantChahibService.getEtudiantByDocumentId(documentId);

        if (etudiantDetailsOpt != null) {
            return ResponseEntity.ok(etudiantDetailsOpt);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retourner 404 si non trouvé
        }
    }

    // Endpoint pour récupérer les documents d'un professeur
    @GetMapping("/documents-by-professeur/{professeurId}")
    public ResponseEntity<List<Document>> getDocumentsByProfesseurId(@PathVariable Long professeurId) {
        List<Document> documents = documentService.getDocumentsByProfesseurId(professeurId);
        if (documents.isEmpty()) {
            return ResponseEntity.noContent().build();  // Si aucun document n'est trouvé
        }
        return ResponseEntity.ok(documents);  // Retourner les documents avec un code 200
    }


    @PutMapping("/updatedocumentinfo/{documentId}")
    public ResponseEntity<String> updateDocumentInfo(
            @PathVariable Long documentId,
            @RequestBody CreateDocumentDto updatedDocumentDto) {

        // Vérifier si le nouveau thème est similaire à d'autres documents
        if (documentService.isThemeSimilarForUpdate(documentId, updatedDocumentDto.getTheme())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Le nouveau thème est trop similaire à un document existant.");
        }

        // Continuer la mise à jour si le thème est unique
        documentService.updateDocumentInfo(documentId, updatedDocumentDto);
        return ResponseEntity.ok("Document mis à jour avec succès.");
    }


    @GetMapping("/checkThemeExists")
    public ResponseEntity<Boolean> checkThemeExists(
            @RequestParam String theme,
            @RequestParam(required = false) Long excludeId) {

        boolean themeExists = documentService.isThemeSimilarForUpdate(excludeId, theme);
        return ResponseEntity.ok(themeExists);
    }


    // Endpoint pour récupérer tous les documents regroupés par année
    @GetMapping("/all")
    public ResponseEntity<Map<Integer, List<Document>>> getAllDocumentsGroupedByYear() {
        Map<Integer, List<Document>> documentsByYear = documentService.getDocumentsGroupedByYear();
        return ResponseEntity.ok(documentsByYear);
    }

    // Endpoint pour récupérer le nom complet de l'étudiant associé à un document
    @GetMapping("/document/{documentId}/etudiant-fullname")
    public ResponseEntity<String> getEtudiantFullName(@PathVariable Long documentId) {
        Optional<Document> documentOpt = documentService.findById(documentId);
        if (documentOpt.isPresent()) {
            Document document = documentOpt.get();
            String fullName = documentService.getEtudiantFullName(document);
            return ResponseEntity.ok(fullName);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document non trouvé.");
    }

    // Endpoint pour récupérer les documents d'un étudiant spécifique via l'ID utilisateur
    @GetMapping("/documents-by-user/{userId}")
    public ResponseEntity<List<Document>> getDocumentsByUserId(@PathVariable Long userId) {
        try {
            // Utiliser EtudiantDocServices pour récupérer les documents via l'ID utilisateur
            List<Document> documents = etudiantDocServices.getDocumentsByUserId(userId);
            if (documents.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(documents);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Gérer le cas où l'étudiant n'est pas trouvé
        }
    }

    // Endpoint pour récupérer les informations d'un étudiant via l'ID utilisateur
    @GetMapping("/etudiant-by-user/{userId}")
    public ResponseEntity<EtudiantDocDto> getEtudiantByUserId(@PathVariable Long userId) {
        try {
            // Utiliser EtudiantDocServices pour récupérer les informations de l'étudiant
            EtudiantDocDto etudiantDocDto = etudiantDocServices.getEtudiantByUserId(userId);
            return ResponseEntity.ok(etudiantDocDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Gérer le cas où l'étudiant n'est pas trouvé
        }
    }


    // Endpoint pour récupérer le professeur associé à un document par ID du document
    @GetMapping("/{documentId}/professeur")
    public ResponseEntity<ProfesseurDocsDto> getProfesseurByDocumentId(@PathVariable Long documentId) {
        Optional<Document> documentOpt = documentService.findById(documentId);

        if (documentOpt.isPresent()) {
            ProfesseurDocsDto professeur = new ProfesseurDocsDto();
            professeur.setProfesseurId(documentOpt.get().getProfesseur().getProfesseurId());
            professeur.setNom(documentOpt.get().getProfesseur().getNom());
            professeur.setPrenom(documentOpt.get().getProfesseur().getPrenom());
            professeur.setEmail(documentOpt.get().getProfesseur().getEmail());
            if (professeur != null) {
                return ResponseEntity.ok(professeur);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Si aucun professeur n'est associé
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Document non trouvé
        }
    }

    @GetMapping("/connect/{userId}")
    public UtilisateurCoDto getUtilisateurCoDto(@PathVariable Long userId) {
        return userService.getUserCoDto(userId);
    }

}
