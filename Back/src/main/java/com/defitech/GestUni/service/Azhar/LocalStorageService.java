package com.defitech.GestUni.service.Azhar;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LocalStorageService {

    private final String storageDirectory = "src/main/resources/DocumentPermission/";

    public LocalStorageService() throws IOException {
        // Créez le répertoire si ce n'est pas déjà fait
        Path directoryPath = Paths.get(storageDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Obtenez le nom de fichier original
        String originalFilename = file.getOriginalFilename();

        // Vérifiez si le nom de fichier n'est pas nul
        if (originalFilename == null) {
            throw new IllegalArgumentException("Le nom de fichier ne peut pas être nul");
        }

        // Séparez le nom de fichier et son extension
        String fileNameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        // Créez un nouveau nom de fichier unique
        String newFileName = "defi___" + fileNameWithoutExtension + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                "_____fichier_pour_permission" + fileExtension;

        // Déterminez le chemin complet pour enregistrer le fichier
        Path filePath = Paths.get(storageDirectory, newFileName);

        // Enregistrez le fichier sur le disque
        Files.write(filePath, file.getBytes());

        // Retournez le chemin relatif qui sera sauvegardé en base de données
        return filePath.toString();
    }

    public byte[] getFile(String fileName) throws IOException {
        Path filePath = Paths.get(storageDirectory, fileName);

        if (!Files.exists(filePath)) {
            throw new IOException("Le fichier spécifié n'existe pas : " + fileName);
        }

        return Files.readAllBytes(filePath);
    }
}
