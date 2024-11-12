package com.defitech.GestUni.service;

import com.defitech.GestUni.dto.Note.UeDto;
import com.defitech.GestUni.dto.UEDto;
import com.defitech.GestUni.enums.Etat;
import com.defitech.GestUni.enums.NiveauEtude;
import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.Bases.Filiere;
import com.defitech.GestUni.models.Bases.Parcours;
import com.defitech.GestUni.models.Bases.Professeur;
import com.defitech.GestUni.models.Bases.UE;
import com.defitech.GestUni.repository.FiliereRepository;
import com.defitech.GestUni.repository.ParcoursRepository;
import com.defitech.GestUni.repository.ProfesseurRepository;
import com.defitech.GestUni.repository.UERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UEServices {

    @Autowired
    private UERepository ueRepository;
    @Autowired
    private ParcoursRepository parcoursRepository;
    @Autowired
    private ProfesseurRepository professeurRepository;
    @Autowired
    private FiliereRepository filiereRepository;


    // Enregistrer les cours
    public UE saveUE(UE ue) {

        // Vérification du professeur
        if (ue.getProfesseur() != null) {
            Optional<Professeur> professeurOpt = professeurRepository.findById(ue.getProfesseur().getProfesseurId());
            if (professeurOpt.isEmpty()) {
                throw new IllegalArgumentException("Professeur avec ID " + ue.getProfesseur().getProfesseurId() + " n'existe pas.");
            }
            ue.setProfesseur(professeurOpt.get());
        } else {
            ue.setProfesseur(null);
        }
        ue.setEtat(Etat.NORMALE);
        ue.setHeureEffectuer(0);
        // Vérification des filières
        if (ue.getFilieres() != null && !ue.getFilieres().isEmpty()) {
            for (int i = 0; i < ue.getFilieres().size(); i++) {
                Long filiereId = ue.getFilieres().get(i).getFiliereId();
                Optional<Filiere> filiereOpt = filiereRepository.findById(filiereId);
                if (filiereOpt.isEmpty()) {
                    throw new IllegalArgumentException("Filiere avec ID " + filiereId + " n'existe pas.");
                }
                ue.getFilieres().set(i, filiereOpt.get());
            }
        }

        return ueRepository.save(ue);
    }

    public UEDto convertToDTO(UE ue) {
        UEDto ueDto = new UEDto();
        ueDto.setUeId(ue.getUeId());
        ueDto.setLibelle(ue.getLibelle());
        ueDto.setCode(ue.getCode());
        ueDto.setCredit(ue.getCredit());
        ueDto.setTypeUe(ue.getTypeUe());
        ueDto.setDescriptUe(ue.getDescriptUe());
        ueDto.setProfesseurNom(ue.getProfesseur().getNom() + " " + ue.getProfesseur().getPrenom());

        // Set niveauEtude if not null
        ueDto.setNiveauEtude(ue.getNiveauEtude() != null ? ue.getNiveauEtude() : null);

        // Set typeSemestre if not null
        ueDto.setTypeSemestre(ue.getTypeSemestre() != null ? ue.getTypeSemestre() : null);

        // Set filiereNom if filieres exist
        ueDto.setFiliereNom(ue.getFilieres() != null ?
                ue.getFilieres().stream().map(Filiere::getNomFiliere).collect(Collectors.toList()) :
                Collections.emptyList());

        // Set parcoursNom by retrieving the parcours from the filiere
        if (ue.getFilieres() != null && !ue.getFilieres().isEmpty()) {
            // Assuming each filiere has a parcours associated with it
            ueDto.setParcoursNom(ue.getFilieres().get(0).getParcours().getNomParcours());
        } else {
            ueDto.setParcoursNom(null);
        }

        return ueDto;
    }


    // Obtenir tous les cours
    public List<UEDto> findAllCours() {
        List<UE> coursList = ueRepository.findAll();
        return coursList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Obtenir les cours par filiere
    public List<UEDto> getCoursByFiliere(Long filiereId) {
        List<UE> coursList = ueRepository.findByFilieres_filiereId(filiereId);
        return coursList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Obtenir un cours par ID
    public UEDto getCoursById(Long id) {
        UE ue = ueRepository.findById(id).orElse(null);
        if (ue != null) {
            return convertToDTO(ue);
        }
        return null;
    }

    public List<UEDto> getUEsByProfesseur(Long professeurId) {
        List<UE> ueList = ueRepository.findByProfesseur_ProfesseurId(professeurId);
        return ueList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UEDto> getUEByParcours(Long parcoursId) {
        List<UE> ues = ueRepository.findByFilieres_Parcours_ParcoursId(parcoursId);
        return ues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UE> getUeById(Long ueId) {
        return ueRepository.findById(ueId);
    }

////////////////////////////////////NEW////////////////////////////////////


    public UE addUe(UE ue) {
        ue.setEtat(Etat.NORMALE);
        ue.setHeureEffectuer(0);
        return ueRepository.save(ue);
    }
    public void addCours(UeDto ueDto) {
        UE ue = new UE();
        ue.setLibelle(ueDto.getLibelle());
        ue.setCode(ueDto.getCode());
        ue.setCredit(ueDto.getCredit());
        ue.setQuotaSemaine(ueDto.getQuotaSemaine());
        ue.setDescriptUe(ueDto.getDescriptUe());

        Professeur professeur = professeurRepository.findById(ueDto.getProfesseur().getProfesseurId())
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé"));
        ue.setProfesseur(professeur);

        ue.setFilieres(
                ueDto.getFilieres().stream()
                        .map(filiereDto -> filiereRepository.findById(filiereDto.getId())
                                .orElseThrow(() -> new RuntimeException("Filiere non trouvée")))
                        .collect(Collectors.toList())
        );

        ue.setNiveauEtude(NiveauEtude.valueOf(ueDto.getNiveauEtude()));
        ue.setTypeSemestre(TypeSemestre.valueOf(ueDto.getTypeSemestre()));
        ue.setEtat(Etat.NORMALE);
        ue.setHeureEffectuer(ueDto.getHeureEffectuer());

        ueRepository.save(ue);
    }





//    public List<Etudiant> findEtudiantsByUeAndSemestre(Long ueId, TypeSemestre semestre) {
//        return ueRepository.findByUe_ueIdAndSemestre(ueId, semestre);
//    }
}