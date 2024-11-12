package com.defitech.GestUni.service.Chahib;

import com.defitech.GestUni.Event.DocumentSavedEvent;
import com.defitech.GestUni.dto.CreateDocumentDto;
import com.defitech.GestUni.dto.FormulaireVerification;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.Professeur;
import com.defitech.GestUni.models.Chahib.Document;
import com.defitech.GestUni.models.Chahib.StatutDocument;
import com.defitech.GestUni.models.Chahib.TypeDocument;
import com.defitech.GestUni.repository.*;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;


    @Autowired
    @Qualifier("chahibEmailService")
    private EmailService emailService;
    @Autowired
    private ParcoursRepository parcoursRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private DocumentStatusUpdater statusUpdater;

    @Autowired
    private FiliereRepository filiereRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    @Qualifier("chahibGoogleCloudStorageService")
    private GoogleCloudStorageService googleCloudStorageService;


    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private static final double SIMILARITY_THRESHOLD = 0.95;
    private static final double MAX_SIMILARITY_THRESHOLD = 0.95;

    private static final List<String> ALLOWED_FILE_TYPES = List.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    // Méthode pour vérifier si un thème existe
    public boolean themeExists(String theme) {
        return documentRepository.existsByTheme(theme);
    }

    // Service : Vérifier si un document existe pour un étudiant et un type de document
    public boolean checkDocumentExists(Long etudiantId, TypeDocument typeDocument) {
        return documentRepository.existsByEtudiantEtudiantIdAndTypeDocument(etudiantId, typeDocument);
    }

    public List<Document> searchDocuments(String theme) {
        return documentRepository.findByThemeContainingIgnoreCaseAndTypeDocumentAndNomParcours(theme);
    }

    public List<Document> findByTheme(String theme) {
        return documentRepository.findByThemeContainingIgnoreCase(theme);
    }

    public boolean isThemeUniqueAndNotSimilar(String newTheme) {
        List<Document> documents = documentRepository.findAll();
        return documents.stream().noneMatch(document -> isThemeSimilar(newTheme, document.getTheme()));
    }

    private boolean isThemeSimilar(String theme1, String theme2) {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        Map<CharSequence, Integer> vector1 = toVector(theme1);
        Map<CharSequence, Integer> vector2 = toVector(theme2);
        double similarity = cosineSimilarity.cosineSimilarity(vector1, vector2);
        return similarity >= SIMILARITY_THRESHOLD;
    }

    // Méthode pour mettre à jour les informations d'un document (thème et professeur)
    public void updateDocumentInfo(Long documentId, CreateDocumentDto updatedDocumentDto) {
        documentRepository.findById(documentId).ifPresent(document -> {
            // Mise à jour du thème
            document.setTheme(updatedDocumentDto.getTheme());

            // Recherche du professeur par ID et mise à jour s'il est trouvé
            professeurRepository.findById(updatedDocumentDto.getProfesseurId())
                    .ifPresent(document::setProfesseur);

            // Sauvegarde du document mis à jour
            documentRepository.save(document);
        });
    }

    // Méthode pour trouver un professeur par son ID
    public Optional<Professeur> findProfesseurById(Long professeurId) {
        return professeurRepository.findById(professeurId);
    }

    // Méthode pour trouver les documents par l'ID de l'étudiant
    public List<Document> findByEtudiantId(Long etudiantId) {
        return documentRepository.findByEtudiant_EtudiantId(etudiantId);
    }


    private Map<CharSequence, Integer> toVector(String text) {
        Map<CharSequence, Integer> vector = new HashMap<>();
        for (char ch : text.toCharArray()) {
            String charAsString = String.valueOf(ch);
            vector.put(charAsString, vector.getOrDefault(charAsString, 0) + 1);
        }
        return vector;
    }

    private boolean isAllowedFileType(MultipartFile file) {
        String fileType = file.getContentType();
        return ALLOWED_FILE_TYPES.contains(fileType);
    }

    public String addDocument(CreateDocumentDto createDocumentDto) {
        try {
            // Vérification du professeur
            Optional<Professeur> professeurOptional = professeurRepository.findById(createDocumentDto.getProfesseurId());
            if (professeurOptional.isEmpty()) {
                return "Professeur non trouvé.";
            }

            // Vérification de l'étudiant
            Optional<Etudiant> etudiantOptional = etudiantRepository.findById(createDocumentDto.getEtudiantId());
            if (etudiantOptional.isEmpty()) {
                return "Étudiant non trouvé.";
            }

            Etudiant etudiant = etudiantOptional.get();

            // Vérification du thème
            if (!isThemeUniqueAndNotSimilar(createDocumentDto.getTheme())) {
                return "Le thème du document est trop similaire à un document existant.";
            }

            // Création du document
            Document document = new Document();
            document.setTypeDocument(createDocumentDto.getTypeDocument());
            document.setTheme(createDocumentDto.getTheme());
            document.setCreatedAt(LocalDateTime.now());
            document.setStatut(StatutDocument.EN_REDACTION);
            document.setProfesseur(professeurOptional.get());
            document.setEtudiant(etudiant);

            // Récupérer et associer explicitement le parcours et la filière de l'étudiant au document
            if (etudiant.getParcours() != null) {
                document.setParcours(etudiant.getParcours());
            }

            if (etudiant.getFiliere() != null) {
                document.setFiliere(etudiant.getFiliere());
            }

            emailService.sendCreationDocumentProf(professeurOptional.get().getEmail(), document);

            // Envoi de l'email de confirmation à l'étudiant
            emailService.sendConfirmationDocumentEtudiant(etudiant.getEmail(), document);

            // Sauvegarde et publication de l'événement
            saveDocument(document);


            return "Un nouveau document créé avec succès.";

        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du document : ", e);
            return "Erreur lors de la création du document.";
        }
    }

    public String updateDocument(Long documentId, MultipartFile file) throws IOException, GeneralSecurityException {
        try {
            Optional<Document> documentOptional = documentRepository.findById(documentId);
            if (documentOptional.isEmpty()) {
                logger.warn("Document non trouvé pour l'ID: {}", documentId);
                return "Document non trouvé.";
            }

            Document document = documentOptional.get();
            if (file != null && !file.isEmpty()) {
                if (!isAllowedFileType(file)) {
                    logger.warn("Type de fichier non pris en charge pour le fichier: {}", file.getOriginalFilename());
                    return "Le type de fichier n'est pas pris en charge. Veuillez télécharger un fichier PDF ou Word.";
                }

                String fileUrl = googleCloudStorageService.uploadFile(file);
                if (fileUrl == null || fileUrl.isEmpty()) {
                    logger.error("Erreur: L'URL du fichier est null après le téléchargement.");
                    return "Erreur lors du téléchargement du fichier.";
                }
                document.setDocumentUrl(fileUrl);
            }

            saveDocument(document);

            return "Document mis à jour avec succès.";

        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du document : ", e);
            return "Erreur lors de la mise à jour du document.";
        }
    }

    // Mise à jour du professeur associé au document et notification
    public String updateDocumentProfessor(Long documentId, Long newProfesseurId) {
        Optional<Document> documentOptional = documentRepository.findById(documentId);
        if (documentOptional.isEmpty()) {
            return "Document non trouvé.";
        }

        Document document = documentOptional.get();

        Optional<Professeur> professeurOptional = professeurRepository.findById(newProfesseurId);
        if (professeurOptional.isEmpty()) {
            return "Nouveau professeur non trouvé.";
        }

        document.setProfesseur(professeurOptional.get());

        saveDocument(document);

        return "Professeur mis à jour avec succès.";
    }

    public void saveDocument(Document document) {
        documentRepository.save(document);
        logger.info("Document sauvegardé : " + document.getTheme());
        eventPublisher.publishEvent(new DocumentSavedEvent(this, document));
        logger.info("Événement DocumentSavedEvent publié pour le document : " + document.getTheme());
    }

    public void updateDocumentStatus(Document document, StatutDocument statut) {
        statusUpdater.updateStatus(document, statut);
        saveDocument(document);

        // Envoi de la notification pour changement de statut

    }

    private double calculateAverageSimilarity(String text, List<Document> documents, Function<Document, String> fieldExtractor) {
        if (text == null) {
            throw new IllegalArgumentException("Le texte ne peut pas être nul.");
        }

        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        Map<CharSequence, Integer> vector1 = toVector(text);
        double totalSimilarity = 0.0;
        int count = 0;

        for (Document document : documents) {
            String fieldValue = fieldExtractor.apply(document);
            if (fieldValue != null) {
                Map<CharSequence, Integer> vector2 = toVector(fieldValue);
                double similarity = cosineSimilarity.cosineSimilarity(vector1, vector2);
                totalSimilarity += similarity;
                count++;
            }
        }
        return count > 0 ? totalSimilarity / count : 0.0;
    }

    private String buildRejectionMessage(Map<String, Double> similarityResults) {
        StringBuilder message = new StringBuilder("Formulaire rejeté :");
        for (Map.Entry<String, Double> entry : similarityResults.entrySet()) {
            if (entry.getValue() > MAX_SIMILARITY_THRESHOLD) {
                message.append("\n")
                        .append(entry.getKey())
                        .append(" a une similarité moyenne de ")
                        .append(String.format("%.2f", entry.getValue()))  // Formater avec 2 chiffres après la virgule
                        .append("%");  // Ajouter le symbole de pourcentage
            }
        }
        return message.toString();
    }

    public Map<String, Object> verifyAndUpdateDocumentWithDetails(Long documentId, FormulaireVerification formulaire) {
        Map<String, Double> similarityResults = verifySimilarityWithDetails(formulaire, documentId);
        Map<String, Object> response = new HashMap<>();

        boolean allBelowThreshold = true;
        for (Map.Entry<String, Double> entry : similarityResults.entrySet()) {
            if (entry.getValue() > MAX_SIMILARITY_THRESHOLD) {
                allBelowThreshold = false;
                response.put(entry.getKey(), "Similarité moyenne : " + entry.getValue());
            }
        }

        if (allBelowThreshold) {
            // Mise à jour du document si la vérification réussit
            Document existingDocument = documentRepository.findById(documentId)
                    .orElseThrow(() -> new DocumentSaveException("Document non trouvé"));

            existingDocument.setIntroduction(formulaire.getIntroduction());
            existingDocument.setProblematique(formulaire.getProblematique());
            existingDocument.setEtude_critique_existant(formulaire.getEtude_critique_existant());
            existingDocument.setResumeDoc(formulaire.getResumeDoc());
            existingDocument.setConclusion(formulaire.getConclusion());

            try {
                documentRepository.save(existingDocument);
                response.put("status", "success");
            } catch (Exception e) {
                throw new DocumentSaveException("Échec de la mise à jour du document : " + e.getMessage());
            }
        } else {
            response.put("status", "rejected");
        }

        return response;
    }

    private Map<String, Double> verifySimilarityWithDetails(FormulaireVerification formulaire, Long excludeDocumentId) {
        Map<String, Double> similarityResults = new HashMap<>();

        // Récupérer tous les documents, exclure celui correspondant à excludeDocumentId
        List<Document> documents = documentRepository.findAll()
                .stream()
                .filter(document -> excludeDocumentId == null || !document.getId().equals(excludeDocumentId))
                .collect(Collectors.toList());

        // Calculer la similarité pour chaque champ du formulaire s'il est renseigné
        if (formulaire.getIntroduction() != null && !formulaire.getIntroduction().isEmpty()) {
            similarityResults.put("introduction", calculateAverageSimilarity(formulaire.getIntroduction(), documents, Document::getIntroduction));
        }
        if (formulaire.getProblematique() != null && !formulaire.getProblematique().isEmpty()) {
            similarityResults.put("Problematique", calculateAverageSimilarity(formulaire.getProblematique(), documents, Document::getProblematique));
        }
        if (formulaire.getEtude_critique_existant() != null && !formulaire.getEtude_critique_existant().isEmpty()) {
            similarityResults.put("etude et critique de l'existant", calculateAverageSimilarity(formulaire.getEtude_critique_existant(), documents, Document::getEtude_critique_existant));
        }
        if (formulaire.getResumeDoc() != null && !formulaire.getResumeDoc().isEmpty()) {
            similarityResults.put("resumeDoc", calculateAverageSimilarity(formulaire.getResumeDoc(), documents, Document::getResumeDoc));
        }
        if (formulaire.getConclusion() != null && !formulaire.getConclusion().isEmpty()) {
            similarityResults.put("conclusion", calculateAverageSimilarity(formulaire.getConclusion(), documents, Document::getConclusion));
        }

        return similarityResults;
    }

    //Verification and saving
    public Map<String, Object> verifySimilarityWithoutSaving(FormulaireVerification formulaire, Long excludeMemoireId) {
        Map<String, Double> similarityResults = verifySimilarityWithDetails(formulaire, excludeMemoireId);
        Map<String, Object> response = new HashMap<>();

        boolean allBelowThreshold = true;
        for (Map.Entry<String, Double> entry : similarityResults.entrySet()) {
            if (entry.getValue() > MAX_SIMILARITY_THRESHOLD) {
                allBelowThreshold = false;
                response.put(entry.getKey(), "Similarité moyenne : " + entry.getValue());
            }
        }

        if (allBelowThreshold) {
            response.put("status", "success");
        } else {
            response.put("status", "rejected");
        }

        return response;
    }

    public String verifyAndUpdateDocument(Long documentId, FormulaireVerification formulaire) {
        Map<String, Double> similarityResults = verifySimilarityWithDetails(formulaire, documentId);

        boolean allBelowThreshold = true;
        for (Map.Entry<String, Double> entry : similarityResults.entrySet()) {
            if (entry.getValue() > MAX_SIMILARITY_THRESHOLD) {
                allBelowThreshold = false;
                break;
            }
        }

        if (allBelowThreshold) {
            Document existingDocument = documentRepository.findById(documentId)
                    .orElseThrow(() -> new DocumentSaveException("Document not found"));

            existingDocument.setIntroduction(formulaire.getIntroduction());
            existingDocument.setProblematique(formulaire.getProblematique());
            existingDocument.setEtude_critique_existant(formulaire.getEtude_critique_existant());
            existingDocument.setResumeDoc(formulaire.getResumeDoc());
            existingDocument.setConclusion(formulaire.getConclusion());

            try {
                documentRepository.save(existingDocument);
                return "Document updated successfully";
            } catch (Exception e) {
                throw new DocumentSaveException("Failed to update the document: " + e.getMessage());
            }
        } else {
            return buildRejectionMessage(similarityResults);
        }
    }

    public String verifyAndUpdateDocumentWithFile(Long documentId, FormulaireVerification formulaire, MultipartFile file) {
        Map<String, Double> similarityResults = verifySimilarityWithDetails(formulaire, documentId);
        boolean allBelowThreshold = true;

        // Vérifier si toutes les similarités sont en dessous du seuil
        for (Map.Entry<String, Double> entry : similarityResults.entrySet()) {
            if (entry.getValue() > MAX_SIMILARITY_THRESHOLD) {
                allBelowThreshold = false;
                break;
            }
        }

        // Si la vérification échoue, retourner un message de rejet
        if (!allBelowThreshold) {
            return buildRejectionMessage(similarityResults);
        }

        // Si la vérification réussit, on peut commencer à mettre à jour le document
        Optional<Document> documentOptional = documentRepository.findById(documentId);
        if (documentOptional.isEmpty()) {
            return "Document non trouvé.";
        }

        Document document = documentOptional.get();

        // Mise à jour des champs du document
        document.setIntroduction(formulaire.getIntroduction());
        document.setProblematique(formulaire.getProblematique());
        document.setEtude_critique_existant(formulaire.getEtude_critique_existant());
        document.setResumeDoc(formulaire.getResumeDoc());
        document.setConclusion(formulaire.getConclusion());

        boolean isNewFileUploaded = false;

        // Si un fichier est fourni et qu'il n'est pas vide, le traiter
        if (file != null && !file.isEmpty()) {
            // Vérification du type de fichier
            if (!isAllowedFileType(file)) {
                return "Le type de fichier n'est pas pris en charge. Veuillez télécharger un fichier PDF ou Word.";
            }

            // Télécharger le fichier sur Google Cloud Storage
            String fileUrl;
            try {
                fileUrl = googleCloudStorageService.uploadFile(file);
            } catch (Exception e) {
                return "Erreur lors du téléchargement du fichier sur Google Cloud Storage : " + e.getMessage();
            }

            if (fileUrl == null || fileUrl.isEmpty()) {
                return "Erreur: L'URL du fichier est null après le téléchargement.";
            }

            // Mise à jour de l'URL du fichier dans le document
            document.setDocumentUrl(fileUrl);
            isNewFileUploaded = true;
        }

        // Sauvegarder le document
        try {
            saveDocument(document);
        } catch (Exception e) {
            return "Erreur lors de la sauvegarde du document dans la base de données : " + e.getMessage();
        }

        // Envoyer les emails appropriés
        if (isNewFileUploaded) {
            // Envoyer les emails pour le téléversement de la première version
            emailService.sendFirstVersionProf(document.getProfesseur().getEmail(), document);
            emailService.sendFirstVersionEtudiant(document.getEtudiant().getEmail(), document);
        } else {
            // Envoyer les emails pour la mise à jour
            emailService.sendDocumentUpdateProf(document.getProfesseur().getEmail(), document);
            emailService.sendDocumentUpdateEtudiant(document.getEtudiant().getEmail(), document);
        }

        return "Document mis à jour avec succès.";
    }

    // Méthode pour trouver les documents d'un étudiant
    public List<Document> getDocumentsByEtudiantId(Long etudiantId) {  // Renommer ici
        return documentRepository.findByEtudiant_EtudiantId(etudiantId);
    }

    // Méthode pour récupérer un document par ID
    public Optional<Document> findById(Long documentId) {
        return documentRepository.findById(documentId);
    }

    public List<Document> getDocumentsByProfesseurId(Long professeurId) {
        return documentRepository.findByProfesseurProfesseurId(professeurId);
    }

    // Méthode pour vérifier la similarité des thèmes avant la mise à jour
    public boolean isThemeSimilarForUpdate(Long documentId, String newTheme) {
        List<Document> documents = documentRepository.findAll()
                .stream()
                .filter(document -> !document.getId().equals(documentId))  // Exclure le document actuel
                .collect(Collectors.toList());

        return documents.stream().anyMatch(document -> isThemeSimilar(newTheme, document.getTheme()));
    }

    // Nouvelle méthode pour récupérer le nom complet de l'étudiant
    public String getEtudiantFullName(Document document) {
        Etudiant etudiant = document.getEtudiant();
        if (etudiant != null) {
            return etudiant.getPrenom() + " " + etudiant.getNom();
        }
        return "Étudiant non trouvé";
    }

    // Méthode pour regrouper les documents par année
    public Map<Integer, List<Document>> getDocumentsGroupedByYear() {
        List<Document> documents = documentRepository.findAll();

        // Grouper les documents par année de création
        return documents.stream()
                .collect(Collectors.groupingBy(document -> document.getCreatedAt().getYear()));
    }



}
