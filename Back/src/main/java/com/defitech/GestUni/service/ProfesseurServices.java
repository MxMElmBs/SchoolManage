package com.defitech.GestUni.service;

import com.defitech.GestUni.dto.ProfesseurDto;
import com.defitech.GestUni.models.Bases.Professeur;
import com.defitech.GestUni.repository.ProfesseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesseurServices {

    @Autowired
    private ProfesseurRepository professeurRepository;

    public List<Professeur> getAllProfesseurs() {
        return professeurRepository.findAll();
    }

    public Professeur getProfesseurById(Long id) {
        return professeurRepository.findById(id).orElse(null);
    }

    public Professeur saveProfesseur(Professeur professeur) {
        return professeurRepository.save(professeur);
    }

    public void deleteProfesseur(Long id) {
        professeurRepository.deleteById(id);
    }


    public ProfesseurDto getProfesseurdtoById(Long id) {
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professeur not found"));

        ProfesseurDto professeurDto = new ProfesseurDto();
        professeurDto.setProfesseurId(professeur.getProfesseurId());
        professeurDto.setNom(professeur.getNom());
        professeurDto.setPrenom(professeur.getPrenom());
        return professeurDto;
    }



    public List<ProfesseurDto> getAllProfessors() {
        List<Professeur> professors = professeurRepository.findAll();
        return professors.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    private ProfesseurDto mapToDto(Professeur professeur) {
        ProfesseurDto dto = new ProfesseurDto();
        dto.setProfesseurId(professeur.getProfesseurId());
        dto.setNom(professeur.getNom());
        dto.setPrenom(professeur.getPrenom());
        return dto;
    }

}
