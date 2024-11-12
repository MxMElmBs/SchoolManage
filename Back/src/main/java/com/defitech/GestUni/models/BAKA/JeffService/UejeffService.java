package com.defitech.GestUni.models.BAKA.JeffService;

import com.defitech.GestUni.dto.Note.UejeffDto;
import com.defitech.GestUni.enums.NiveauEtude;
import com.defitech.GestUni.enums.TypeNote;
import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.BAKA.Exception.ResourceNotFoundException;
import com.defitech.GestUni.models.BAKA.JeffRepository.UejeffRepository;
import com.defitech.GestUni.models.BAKA.Note;
import com.defitech.GestUni.models.Bases.Filiere;
import com.defitech.GestUni.models.Bases.Professeur;
import com.defitech.GestUni.models.Bases.UE;
import com.defitech.GestUni.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UejeffService {

    @Autowired
    private UERepository ueRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UejeffRepository uejeffRepository;

    @Autowired
    private FiliereRepository filiereRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    public UejeffDto addUe(UejeffDto uejeffDto) {
        UE ue = new UE();
        ue.setCode(uejeffDto.getCode());
        ue.setLibelle(uejeffDto.getLibelle());
        ue.setTypeUe(uejeffDto.getType());
        ue.setDescriptUe(uejeffDto.getDescription());
        ue.setCredit(uejeffDto.getCredit());
        ue.setNiveauEtude(uejeffDto.getNiveauEtude());
        ue.setTypeSemestre(uejeffDto.getTypeSemestre());

        List<Filiere> filieres = filiereRepository.findAllById(uejeffDto.getFiliereIds());
        ue.setFilieres(filieres);

        Professeur professeur = professeurRepository.findById(uejeffDto.getProfesseurId())
                .orElseThrow(() -> new RuntimeException("Professeur not found"));
        ue.setProfesseur(professeur);

        UE savedUe = ueRepository.save(ue);

        UejeffDto savedUejeffDto = new UejeffDto();
        // Mapper les champs du savedUe à savedUejeffDto
        savedUejeffDto.setCode(savedUe.getCode());
        savedUejeffDto.setLibelle(savedUe.getLibelle());
        savedUejeffDto.setType(savedUe.getTypeUe());
        savedUejeffDto.setDescription(savedUe.getDescriptUe());
        savedUejeffDto.setCredit(savedUe.getCredit());
        savedUejeffDto.setNiveauEtude(savedUe.getNiveauEtude());
        savedUejeffDto.setTypeSemestre(savedUe.getTypeSemestre());
        savedUejeffDto.setProfesseurId(professeur.getProfesseurId());
        savedUejeffDto.setProfesseurName(professeur.getNom());
        savedUejeffDto.setProfesseurPrenom(professeur.getPrenom());
        savedUejeffDto.setFiliereIds(filieres.stream().map(Filiere::getFiliereId).collect(Collectors.toList()));
        return savedUejeffDto;
    }
    //Supprimer une UE
    public void supprimerUe(Long id) {
        // Vérifie d'abord si l'UE existe
        UE ue = ueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UE avec l'id " + id + " non trouvée."));

        // Suppression de l'UE
        ueRepository.delete(ue);
    }

    //Modifier une UE
    public UejeffDto modifierUe(Long id, UejeffDto uejeffDto) {
        // Rechercher l'UE par son id
        UE ueExistante = ueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UE avec l'id " + id + " non trouvée."));

        // Mise à jour des champs
        ueExistante.setCode(uejeffDto.getCode());
        ueExistante.setLibelle(uejeffDto.getLibelle());
        ueExistante.setTypeUe(uejeffDto.getType());
        ueExistante.setDescriptUe(uejeffDto.getDescription());
        ueExistante.setCredit(uejeffDto.getCredit());
        ueExistante.setNiveauEtude(uejeffDto.getNiveauEtude());
        ueExistante.setTypeSemestre(uejeffDto.getTypeSemestre());

        // Mise à jour des relations (Filière, Professeur, etc.)
        if (uejeffDto.getFiliereIds() != null && !uejeffDto.getFiliereIds().isEmpty()) {
            List<Filiere> filieres = filiereRepository.findAllById(uejeffDto.getFiliereIds());
            ueExistante.setFilieres(filieres);
        }

        Professeur professeur = professeurRepository.findById(uejeffDto.getProfesseurId())
                .orElseThrow(() -> new ResourceNotFoundException("Professeur non trouvé"));
        ueExistante.setProfesseur(professeur);

        // Enregistrer les modifications
        UE updatedUe = ueRepository.save(ueExistante);

        // Convertir l'entité en DTO et la retourner
        UejeffDto updatedUeDto = new UejeffDto();
        updatedUeDto.setCode(updatedUe.getCode());
        updatedUeDto.setLibelle(updatedUe.getLibelle());
        updatedUeDto.setType(updatedUe.getTypeUe());
        updatedUeDto.setDescription(updatedUe.getDescriptUe());
        updatedUeDto.setCredit(updatedUe.getCredit());
        updatedUeDto.setNiveauEtude(updatedUe.getNiveauEtude());
        updatedUeDto.setTypeSemestre(updatedUe.getTypeSemestre());
        updatedUeDto.setProfesseurId(professeur.getProfesseurId());
        updatedUeDto.setProfesseurName(professeur.getNom());
        updatedUeDto.setProfesseurPrenom(professeur.getPrenom());
        if (updatedUe.getFilieres() != null) {
            updatedUeDto.setFiliereIds(updatedUe.getFilieres().stream()
                    .map(Filiere::getFiliereId)
                    .collect(Collectors.toList()));
        }

        return updatedUeDto;
    }

    public List<UejeffDto> obtenirToutesLesUes() {
        List<UE> ues = ueRepository.findAll();
        return ues.stream().map(ue -> {
            UejeffDto dto = new UejeffDto();
            dto.setId(ue.getUeId());
            dto.setCode(ue.getCode());
            dto.setLibelle(ue.getLibelle());
            dto.setType(ue.getTypeUe());
            dto.setDescription(ue.getDescriptUe());
            dto.setCredit(ue.getCredit());
            dto.setNiveauEtude(ue.getNiveauEtude());
            dto.setTypeSemestre(ue.getTypeSemestre());
            if (ue.getFilieres() != null && !ue.getFilieres().isEmpty()) {
                List<Long> filiereIds = ue.getFilieres().stream()
                        .map(Filiere::getFiliereId)
                        .collect(Collectors.toList());
                dto.setFiliereIds(filiereIds); // Utilisation de filiereIds (liste)
            }
            dto.setProfesseurId(ue.getProfesseur().getProfesseurId());
            dto.setProfesseurName(ue.getProfesseur().getNom());
            dto.setProfesseurPrenom(ue.getProfesseur().getPrenom());
            dto.setProfesseurEmail(ue.getProfesseur().getEmail());
            return dto;
        }).collect(Collectors.toList());
    }


    public UejeffDto obtenirUeParId(Long id) {
        UE ue = ueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UE avec l'id " + id + " non trouvée."));

        UejeffDto dto = new UejeffDto();
        dto.setCode(ue.getCode());
        dto.setLibelle(ue.getLibelle());
        dto.setType(ue.getTypeUe());
        dto.setDescription(ue.getDescriptUe());
        dto.setCredit(ue.getCredit());
        dto.setNiveauEtude(ue.getNiveauEtude());
        dto.setTypeSemestre(ue.getTypeSemestre());
        if (ue.getFilieres() != null && !ue.getFilieres().isEmpty()) {
            List<Long> filiereIds = ue.getFilieres().stream()
                    .map(Filiere::getFiliereId)
                    .collect(Collectors.toList());
            dto.setFiliereIds(filiereIds); // Utilisation de filiereIds (liste)
        }
        dto.setProfesseurId(ue.getProfesseur().getProfesseurId());
        dto.setProfesseurName(ue.getProfesseur().getNom());
        dto.setProfesseurPrenom(ue.getProfesseur().getPrenom());

        return dto;
    }

    //PAGE NOTES
    public List<UejeffDto> getUesBySemestre(TypeSemestre typeSemestre) {
        List<UE> ues = uejeffRepository.findByTypeSemestre(typeSemestre);
        return ues.stream().map(this::convertToUeDto).collect(Collectors.toList());
    }

    // Méthode pour convertir une UE en UeDto
    private UejeffDto convertToUeDto(UE ue) {
        UejeffDto dto = new UejeffDto();
        dto.setId(ue.getUeId());
        dto.setCode(ue.getCode());
        dto.setLibelle(ue.getLibelle());
        dto.setCredit(ue.getCredit());
        dto.setType(ue.getTypeUe());
        dto.setTypeSemestre(TypeSemestre.valueOf(ue.getTypeSemestre().name()));  // Assumant que tu veux afficher le type de semestre en tant que texte
        return dto;
    }


    // Méthode pour récupérer les UE qui ont reçu des notes par semestre et par type de note
    public List<UejeffDto> getUesWithNotesBySemestreAndTypeNote(TypeSemestre typeSemestre) {
        // Récupérer les UE qui ont des notes pour le semestre donné
        List<UE> ues = uejeffRepository.findUesWithNotesBySemestre(typeSemestre);

        // Création de la liste des DTO à partir des UE récupérées
        return ues.stream().flatMap(ue -> {
            List<UejeffDto> ueDtos = new ArrayList<>();

            // Vérifier si l'UE a des notes de type DEVOIR
            boolean hasDevoir = noteRepository.existsByUe_UeIdAndTypeNote(ue.getUeId(), TypeNote.DEVOIR);
            if (hasDevoir) {
                UejeffDto dtoDevoir = convertToUeNoteDto(ue, TypeNote.DEVOIR);
                ueDtos.add(dtoDevoir);
            }

            // Vérifier si l'UE a des notes de type EXAMEN
            boolean hasExamen = noteRepository.existsByUe_UeIdAndTypeNote(ue.getUeId(), TypeNote.EXAMEN);
            if (hasExamen) {
                UejeffDto dtoExamen = convertToUeNoteDto(ue, TypeNote.EXAMEN);
                ueDtos.add(dtoExamen);
            }

            // Retourner les deux DTO (ou un seul selon le cas) pour chaque UE
            return ueDtos.stream();
        }).collect(Collectors.toList());
    }
    // Conversion d'une UE en DTO
    private UejeffDto convertToUeNoteDto(UE ue, TypeNote typeNote) {
        UejeffDto dto = new UejeffDto();
        dto.setId(ue.getUeId()); // Ajouter l'ID de l'UE
        dto.setCode(ue.getCode());
        dto.setLibelle(ue.getLibelle());
        dto.setCredit(ue.getCredit());
        dto.setType(ue.getTypeUe());
        dto.setTypeSemestre(ue.getTypeSemestre());
        dto.setTypeNote(typeNote); // Ajouter le type de note (DEVOIR ou EXAMEN)

        return dto;
    }

    // Méthode pour obtenir le nombre total d'UE
    public long getTotalUE() {
        return uejeffRepository.countTotalUE();
    }

    // Méthode pour calculer le nombre total d'UE pour la Première Année
    public long countUEPremiereAnnee() {
        long semestre1UE = uejeffRepository.countUEParSemestre(TypeSemestre.SEMESTRE_1);
        long semestre2UE = uejeffRepository.countUEParSemestre(TypeSemestre.SEMESTRE_2);
        return semestre1UE + semestre2UE;
    }

    // Méthode pour calculer le nombre total d'UE pour la Deuxième Année
    public long countUEDeuxiemeAnnee() {
        long semestre3UE = uejeffRepository.countUEParSemestre(TypeSemestre.SEMESTRE_3);
        long semestre4UE = uejeffRepository.countUEParSemestre(TypeSemestre.SEMESTRE_4);
        return semestre3UE + semestre4UE;
    }

    // Méthode pour calculer le nombre total d'UE pour la Troisième Année
    public long countUETroisiemeAnnee() {
        long semestre5UE = uejeffRepository.countUEParSemestre(TypeSemestre.SEMESTRE_5);
        long semestre6UE = uejeffRepository.countUEParSemestre(TypeSemestre.SEMESTRE_6);
        return semestre5UE + semestre6UE;
    }

    // Taux de réussite par UE
    public Map<UE, Double> calculerTauxReussiteParUe() {
        List<UE> ues = uejeffRepository.findAll();
        Map<UE, Double> tauxReussiteParUe = new HashMap<>();

        for (UE ue : ues) {
            List<Note> notesUE = noteRepository.findByUe_UeId(ue.getUeId());
            if (!notesUE.isEmpty()) {
                long nbEtudiantsValides = notesUE.stream()
                        .filter(note -> note.getValeur() >= 10)
                        .count();
                double tauxReussite = (double) nbEtudiantsValides / notesUE.size() * 100;
                tauxReussiteParUe.put(ue, tauxReussite);
            }
        }
        return tauxReussiteParUe;
    }

}
