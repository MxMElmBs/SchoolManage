package com.defitech.GestUni.service.Chahib;

import com.defitech.GestUni.dto.EtudiantDocDto;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.Utilisateur;
import com.defitech.GestUni.models.Chahib.Document;
import com.defitech.GestUni.repository.DocumentRepository;
import com.defitech.GestUni.repository.EtudiantDocRepository;
import com.defitech.GestUni.repository.EtudiantRepository;
import com.defitech.GestUni.repository.UtilisateurRepository;
import com.defitech.GestUni.service.Max.EtudiantMaxConnect;
import jakarta.persistence.EntityNotFoundException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EtudiantDocServices {

    @Autowired
    private UtilisateurRepository userR;

    @Autowired
    private DocumentRepository documentRepository;


    @Autowired
    private EtudiantRepository etudiantRepository;

    private final DateTimeFormatter FRENCH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public EtudiantDocDto getEtudiantByUserId(Long userId) {
        Utilisateur user = userR.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("utilisateur non trouvé"));

        Etudiant etudiant = etudiantRepository.findById(user.getEtudiant().getEtudiantId())
                .orElseThrow(() -> new EntityNotFoundException("Étudiant introuvable pour l'utilisateur avec l'ID : " + userId));

        return new EtudiantDocDto(
                etudiant.getEtudiantId(),
                etudiant.getNom(),
                etudiant.getPrenom(),
                etudiant.getEmail(),
                etudiant.getMatricule()
        );
    }

    // Méthode pour récupérer les documents associés à un étudiant via l'ID utilisateur
    public List<Document> getDocumentsByUserId(Long userId) {
        // Utilisation de DocumentRepository pour récupérer les documents d'un utilisateur via son ID
        return documentRepository.findByEtudiant_EtudiantId(userId);
    }

}
