package com.defitech.GestUni.service.Chahib;

import com.defitech.GestUni.dto.EtudiantDetailsDto;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.Professeur;
import com.defitech.GestUni.models.Chahib.Document;
import com.defitech.GestUni.repository.DocumentRepository;
import com.defitech.GestUni.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class EtudiantChahibService {


    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private DocumentRepository documentRepository;

    // Méthode pour récupérer les détails d'un étudiant à partir de l'ID utilisateur
    public Optional<EtudiantDetailsDto> getEtudiantDetailsByUserId(Long userId) {
        // Trouver l'étudiant par l'ID utilisateur
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByUtilisateurIdUser(userId);

        if (etudiantOpt.isPresent()) {
            Etudiant etudiant = etudiantOpt.get();

            // Récupérer la liste des documents associés à l'étudiant
            List<Document> documentList = documentRepository.findByEtudiant_EtudiantId(etudiant.getEtudiantId());

            // Si au moins un document est trouvé, utiliser le premier pour remplir les détails
            if (!documentList.isEmpty()) {
                Document document = documentList.get(0);  // Sélectionner le premier document (ou appliquer une autre logique)

                // Créer un DTO pour rassembler toutes les informations nécessaires
                EtudiantDetailsDto detailsDto = new EtudiantDetailsDto();
                detailsDto.setNom(etudiant.getNom());
                detailsDto.setPrenom(etudiant.getPrenom());
                detailsDto.setParcours(etudiant.getParcours().getNomParcours());
                detailsDto.setFiliere(etudiant.getFiliere().getNomFiliere());

                // Compléter les informations avec les détails du document
                detailsDto.setTheme(document.getTheme());
                detailsDto.setTypeDocument(document.getTypeDocument().name());
                detailsDto.setDocumentUrl(document.getDocumentUrl());  // Ajout de l'URL du document

                // Extraire l'année du champ createdAt
                LocalDateTime createdAt = document.getCreatedAt();
                String formatDate = createdAt!= null
                        ? createdAt.format(frenchFormat)
                        :"N/A"; // Extraire uniquement l'année
                detailsDto.setAnnee(formatDate);  // Définir l'année dans le DTO

                // Ajouter les informations sur le professeur si elles sont présentes
                Professeur professeur = document.getProfesseur();
                if (professeur != null) {
                    detailsDto.setProfesseurNom(professeur.getNom());
                    detailsDto.setProfesseurPrenom(professeur.getPrenom());
                }

                return Optional.of(detailsDto);
            }
        }
        return Optional.empty();  // Si l'étudiant ou les documents ne sont pas trouvés
    }


    private  DateTimeFormatter frenchFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public EtudiantDetailsDto getEtudiantByDocumentId(Long docsId) {
        Optional<Document> docsOpt = documentRepository.findById(docsId);
        EtudiantDetailsDto etuDto = new EtudiantDetailsDto();

        if (docsOpt.isPresent()) {
            Document docs = docsOpt.get();

            // Remplir le DTO avec les informations du document et de l'étudiant
            etuDto.setNom(docs.getEtudiant().getNom());
            etuDto.setPrenom(docs.getEtudiant().getPrenom());
            etuDto.setFiliere(docs.getFiliere().getNomFiliere());
            etuDto.setTheme(docs.getTheme());
            etuDto.setParcours(docs.getParcours().getNomParcours());
            etuDto.setTypeDocument(docs.getTypeDocument().name());
            etuDto.setProfesseurNom(docs.getProfesseur().getNom());
            etuDto.setProfesseurPrenom(docs.getProfesseur().getPrenom());

            // Formater la date si elle est présente
            String formatDate = docs.getCreatedAt() != null
                    ? docs.getCreatedAt().format(frenchFormat)
                    : "N/A";
            etuDto.setAnnee(formatDate);

            etuDto.setDocumentUrl(docs.getDocumentUrl());
        } else {
            return null;  // Retourner null si le document n'est pas trouvé
        }

        return etuDto;
    }

}
