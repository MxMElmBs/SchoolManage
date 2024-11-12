package com.defitech.GestUni.controller;

import com.defitech.GestUni.models.Chahib.Document;
import com.defitech.GestUni.service.Chahib.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/auth/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/document")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("typeDocument") String typeDocument,
            @RequestBody Document document) {
        try {
            // Appel de la méthode uploadDocument qui peut lancer GeneralSecurityException
            Document savedDocument = uploadService.uploadDocument(file, document, typeDocument);
            return ResponseEntity.ok(savedDocument);
        } catch (IOException | GeneralSecurityException e) {
            // Gestion de IOException et GeneralSecurityException
            return ResponseEntity.status(500).body(null);
        }
    }
}
