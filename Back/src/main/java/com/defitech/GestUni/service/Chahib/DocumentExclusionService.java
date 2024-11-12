package com.defitech.GestUni.service.Chahib;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DocumentExclusionService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentExclusionService.class);
    private static final String EXCLUDED_SECTIONS_DIRECTORY = "excluded_sections/";

    public String saveExcludedSectionsToFile(String excludedContent, Long documentId) {
        // Vérifier si le contenu exclu est null ou vide
        if (excludedContent == null || excludedContent.isEmpty()) {
            logger.warn("Le contenu exclu est null ou vide pour le document ID: {}", documentId);
            return "Le contenu exclu est vide ou non valide.";
        }

        // Assurez-vous que le répertoire de sections exclues existe
        try {
            Path path = Paths.get(EXCLUDED_SECTIONS_DIRECTORY);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            logger.error("Erreur lors de la création du répertoire pour les sections exclues", e);
            return "Erreur lors de la création du répertoire pour les sections exclues : " + e.getMessage();
        }

        // Définir le chemin du fichier
        String fileName = EXCLUDED_SECTIONS_DIRECTORY + "excluded_sections_" + documentId + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(excludedContent);
        } catch (IOException e) {
            logger.error("Erreur lors de l'écriture des sections exclues dans le fichier", e);
            return "Erreur lors de l'écriture des sections exclues dans le fichier : " + e.getMessage();
        }

        logger.info("Sections exclues pour le document ID: {} enregistrées dans le fichier: {}", documentId, fileName);
        return "Sections exclues enregistrées avec succès dans le fichier: " + fileName;
    }

    public String readExcludedSectionsFromFile(Long documentId) {
        String fileName = EXCLUDED_SECTIONS_DIRECTORY + "excluded_sections_" + documentId + ".txt";
        Path filePath = Paths.get(fileName);

        try {
            // Lire toutes les lignes du fichier
            List<String> lines = Files.readAllLines(filePath);
            // Joindre les lignes en une seule chaîne de caractères
            return lines.stream().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            logger.error("Erreur lors de la lecture du fichier de sections exclues", e);
            return "Erreur lors de la lecture du fichier de sections exclues : " + e.getMessage();
        }
    }
}
