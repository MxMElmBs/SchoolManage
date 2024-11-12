package com.defitech.GestUni.service.Chahib;

import com.defitech.GestUni.models.Chahib.Document;
import com.defitech.GestUni.models.Chahib.TypeDocument;
import com.defitech.GestUni.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;

@Service
public class UploadService {

    @Autowired
    @Qualifier("chahibGoogleCloudStorageService")
    private GoogleCloudStorageService googleCloudStorageService;

    @Autowired
    private DocumentRepository documentRepository;


    public Document uploadDocument(MultipartFile file, Document document, String typeDocument) throws IOException, GeneralSecurityException {
        // Télécharger le document sur Google Drive
        String fileUrl = googleCloudStorageService.uploadFile(file);

        document.setDocumentUrl(fileUrl);
        document.setCreatedAt(LocalDateTime.now());
        document.setTypeDocument(TypeDocument.valueOf(typeDocument.toUpperCase()));

        Document savedDocument = documentRepository.save(document);

        return savedDocument;
    }


}
