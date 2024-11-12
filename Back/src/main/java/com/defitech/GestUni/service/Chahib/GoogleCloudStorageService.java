package com.defitech.GestUni.service.Chahib;

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

@Service("chahibGoogleCloudStorageService")
public class GoogleCloudStorageService {

    private final Storage storage;
    private final String bucketName;

    /**
     * Constructeur pour initialiser le service de stockage Google Cloud avec les identifiants et le bucket spécifiés.
     *
     * @param projectId        ID du projet Google Cloud
     * @param bucketName       Nom du bucket de stockage Google Cloud
     * @param credentialsBase64 Chaîne d'identifiants encodée en Base64 pour l'authentification
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de la lecture des identifiants
     */
    public GoogleCloudStorageService(@Value("${google.cloud.project-id}") String projectId,
                                     @Value("${google.cloud.bucket-name}") String bucketName,
                                     @Value("${chahib.google.application.credentials}") String credentialsBase64) throws IOException {
        this.bucketName = bucketName;

        // Décoder la chaîne Base64 contenant les identifiants
        byte[] decodedCredentials = Base64.getDecoder().decode(credentialsBase64);

        // Initialiser le client Google Cloud Storage avec les identifiants décodés
        try (ByteArrayInputStream credentialsStream = new ByteArrayInputStream(decodedCredentials)) {
            this.storage = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build()
                    .getService();
        }
    }

    /**
     * Télécharge un fichier vers Google Cloud Storage.
     *
     * @param file MultipartFile représentant le fichier à télécharger
     * @return Lien public vers le fichier téléchargé
     * @throws IOException Si une erreur d'entrée/sortie se produit lors du téléchargement du fichier
     */
    public String uploadFile(MultipartFile file) throws IOException {
        try {
            // Obtenez le nom de fichier original
            String originalFilename = file.getOriginalFilename();

            // Vérifiez si le nom de fichier n'est pas nul
            if (originalFilename == null) {
                throw new IllegalArgumentException("Le nom de fichier ne peut pas être nul");
            }

            // Séparez le nom du fichier et l'extension
            String fileNameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));


            // Génère un nom de fichier unique avec un horodatage
            String fileName = "defi_DOCUMENT_" + fileNameWithoutExtension  + "__" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH-mm-ss")) +
                    "__fichier_pour_document" + fileExtension;

            // Crée un objet BlobInfo pour le fichier sans spécifier d'ACL
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                    .build();

            // Télécharge le fichier dans le bucket Google Cloud Storage
            Blob blob = storage.create(blobInfo, file.getBytes());

            // Retourne le lien public vers le fichier
            return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
        } catch (Exception e) {
            e.printStackTrace(); // Ajouter des logs pour débogage
            throw new IOException("Erreur lors du téléchargement du fichier sur GCS", e);
        }
    }


    /**
     * Télécharge un fichier depuis Google Cloud Storage.
     *
     * @param fileUrl L'URL du fichier à télécharger
     * @return Tableau d'octets représentant le fichier téléchargé
     * @throws IOException Si une erreur d'entrée/sortie se produit lors du téléchargement du fichier
     */
    public byte[] downloadFile(String fileUrl) throws IOException {
        // Récupère le nom du fichier depuis l'URL
        String fileName = extractFileNameFromUrl(fileUrl);

        // Récupère l'objet Blob du fichier à partir du nom du bucket et du nom du fichier
        Blob blob = storage.get(bucketName, fileName);

        // Vérifie si le fichier exis84te
        if (blob == null) {
            throw new IOException("Fichier non trouvé dans le stockage Google Cloud : " + fileName);
        }

        // Télécharge le contenu du fichier sous forme de tableau d'octets
        return blob.getContent();
    }

    /**
     * Extrait le nom du fichier à partir de l'URL donnée.
     *
     * @param fileUrl URL complète du fichier
     * @return Nom du fichier extrait de l'URL
     */
    private String extractFileNameFromUrl(String fileUrl) {
        // Supposons que l'URL soit de la forme "https://storage.googleapis.com/{bucketName}/{fileName}"
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}
