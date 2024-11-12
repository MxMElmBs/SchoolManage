package com.defitech.GestUni.service;

import com.defitech.GestUni.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EtudiantService {
    @Autowired
    private EtudiantRepository etudiantRepository;

    /**
     * Méthode pour récupérer les étudiants inscrits à une UE spécifique pour un semestre donné.
     *
     * @param ueId     L'ID de l'UE.
     * @param semestre Le semestre sélectionné.
     * @return Une liste d'étudiants inscrits à cette UE et pour ce semestre.
     */
}
