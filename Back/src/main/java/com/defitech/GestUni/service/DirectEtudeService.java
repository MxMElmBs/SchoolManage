package com.defitech.GestUni.service;

import com.defitech.GestUni.dto.DirecteurEtudeDto;
import com.defitech.GestUni.models.Bases.DirecteurEtude;
import com.defitech.GestUni.models.Bases.Parcours;
import com.defitech.GestUni.repository.Azhar.DirecteurEtudeRepository;
import com.defitech.GestUni.repository.ParcoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DirectEtudeService {
    @Autowired
    private DirecteurEtudeRepository directeurEtudeRepository;

    @Autowired
    private ParcoursRepository parcoursRepository;

    @Transactional
    public DirecteurEtude ajouterDirecteurEtude(DirecteurEtudeDto dto) {
        // Rechercher le parcours par son libellé
        Parcours parcours = parcoursRepository.findByNomParcour(dto.getParcoursDirecteurEtude());
        if (parcours == null) {
            throw new RuntimeException("Parcours introuvable avec le libellé : " + dto.getParcoursDirecteurEtude());
        }

        // Créer un nouvel objet DirecteurEtude
        DirecteurEtude directeurEtude = new DirecteurEtude();
        directeurEtude.setDirecteurEtudeNom(dto.getNomDirecteurEtude());
        directeurEtude.setDirecteurEtudePrenom(dto.getPrenomDirecteurEtude());
        directeurEtude.setEmail(dto.getEmailDirecteurEtude());
        directeurEtude.setParcours(parcours);

        // Sauvegarder le directeur dans la base de données
        return directeurEtudeRepository.save(directeurEtude);
    }

    public DirecteurEtudeDto getDirecteurEtudeById(Long id) {
        DirecteurEtude directeurEtude = directeurEtudeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Directeur d'étude not found"));

        DirecteurEtudeDto directeurEtudeDto = new DirecteurEtudeDto();
        directeurEtudeDto.setDirecteurId(directeurEtude.getDirecteurEtudeId());
        directeurEtudeDto.setParcoursId(directeurEtude.getParcours().getParcoursId());
        directeurEtudeDto.setNomDirecteurEtude(directeurEtude.getDirecteurEtudeNom());
        directeurEtudeDto.setPrenomDirecteurEtude(directeurEtude.getDirecteurEtudePrenom());
        directeurEtudeDto.setEmailDirecteurEtude(directeurEtude.getEmail());
        return directeurEtudeDto;
    }
}
