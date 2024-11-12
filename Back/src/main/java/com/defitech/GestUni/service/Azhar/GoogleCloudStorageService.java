package com.defitech.GestUni.service.Azhar;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class GoogleCloudStorageService {

    private final Storage storage;
    private final String bucketName;

    public GoogleCloudStorageService(@Value("${google.cloud.project-id}") String projectId,
                                     @Value("${google.cloud.bucket-name}") String bucketName,
                                     @Value("${google.application.credentials}") String credentialsBase64) throws IOException {
        this.bucketName = bucketName;

        // Décoder la chaîne Base64
        byte[] decodedCredentials = Base64.getDecoder().decode(credentialsBase64);

        try (ByteArrayInputStream credentialsStream = new ByteArrayInputStream(decodedCredentials)) {
            this.storage = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build()
                    .getService();
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Obtenez le nom de fichier original
        String originalFilename = file.getOriginalFilename();

        // Vérifiez si le nom de fichier n'est pas nul
        if (originalFilename == null) {
            throw new IllegalArgumentException("Le nom de fichier ne peut pas être nul");
        }

        // Séparez le nom du fichier et l'extension
        String fileNameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        // Créez un nouveau nom de fichier sans l'extension et ajoutez l'extension à la fin
        String newFileName = "defi___" + fileNameWithoutExtension + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                "_____fichier_pour_permission" + fileExtension;

        // Créez le BlobInfo avec le nouveau nom de fichier
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, newFileName).build(),
                file.getBytes()
        );

        // Retournez le lien média du blob
        return blobInfo.getMediaLink();
    }

    public Blob getFile(String fileName) {
        BlobId blobId = BlobId.of(bucketName, fileName);
        return storage.get(blobId);
    }
}

