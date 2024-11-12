package com.defitech.GestUni.service;

import com.defitech.GestUni.dto.OtherUserDto;
import com.defitech.GestUni.models.Bases.OtherUser;
import com.defitech.GestUni.models.Bases.Parcours;
import com.defitech.GestUni.repository.OtherUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OtherUserService {
    @Autowired
    private OtherUserRepository otherUserRepository;

    @Transactional
    public OtherUser ajouterOtherUser(OtherUserDto dto) {
        // Rechercher le parcours par son libellé

        // Créer un nouvel objet DirecteurEtude
        OtherUser ouser = new OtherUser();
        ouser.setNom(dto.getNom());
        ouser.setPrenom(dto.getPrenom());
        ouser.setEmail(dto.getEmail());

        // Sauvegarder le directeur dans la base de données
        return otherUserRepository.save(ouser);
    }
}
