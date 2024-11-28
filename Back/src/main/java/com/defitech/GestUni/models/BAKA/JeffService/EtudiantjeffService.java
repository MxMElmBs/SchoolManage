package com.defitech.GestUni.models.BAKA.JeffService;

import com.defitech.GestUni.dto.Note.EtudiantMoyenneDto;
import com.defitech.GestUni.dto.Note.EtudiantjeffDto;
import com.defitech.GestUni.dto.Note.NiveauStatsDto;
import com.defitech.GestUni.enums.NiveauEtude;
import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.BAKA.JeffRepository.EtudiantjeffRepository;
import com.defitech.GestUni.models.BAKA.Note;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.UE;
import com.defitech.GestUni.repository.NoteRepository;
import com.defitech.GestUni.service.NoteService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EtudiantjeffService {
//    @Autowired
//    private EtudiantjeffRepository etudiantRepository;
    @Autowired
    private EtudiantjeffRepository etudiantjeffRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private NoteService notesService;

    public List<EtudiantjeffDto> getEtudiantsParSemestre(TypeSemestre semestre) {
        NiveauEtude niveau = getNiveauEtudeParSemestre(semestre);  // Convertir semestre en niveau
        List<Etudiant> etudiants = etudiantjeffRepository.findByNiveauEtude(niveau);

        // Convertir la liste d'étudiants en DTO
        return etudiants.stream()
                .map(this::convertToEtudiantDto)
                .collect(Collectors.toList());
    }

    // Méthode pour mapper semestre à niveau d'étude
    public NiveauEtude getNiveauEtudeParSemestre(TypeSemestre semestre) {
        switch (semestre) {
            case SEMESTRE_1:
            case SEMESTRE_2:
                return NiveauEtude.PREMIERE_ANNEE;
            case SEMESTRE_3:
            case SEMESTRE_4:
                return NiveauEtude.DEUXIEME_ANNEE;
            case SEMESTRE_5:
            case SEMESTRE_6:
                return NiveauEtude.TROISIEME_ANNEE;
            default:
                throw new IllegalArgumentException("Semestre inconnu : " + semestre);
        }
    }

    public EtudiantjeffDto convertToEtudiantDto(Etudiant etudiant) {
        EtudiantjeffDto dto = new EtudiantjeffDto();
        dto.setId(etudiant.getEtudiantId());
        dto.setMatricule(etudiant.getMatricule());
        dto.setNom(etudiant.getNom());
        dto.setPrenom(etudiant.getPrenom());
        // Vérification si la filière est non nulle
        if (etudiant.getFiliere() != null) {
            dto.setFiliereNom(etudiant.getFiliere().getNomFiliere());
        } else {
            dto.setFiliereNom("Non attribuée");  // Ou une valeur par défaut si filière est null
        }
        return dto;
    }

    public List<EtudiantjeffDto> getEtudiantsWithNotes() {
        List<Etudiant> etudiants = etudiantjeffRepository.findEtudiantsWithNotes();
        return etudiants.stream()
                .map(this::convertToEtudiantjeffDto) // Conversion de chaque Etudiant en EtudiantjeffDto
                .collect(Collectors.toList());
    }

    public EtudiantjeffDto convertToEtudiantjeffDto(Etudiant etudiant) {
        EtudiantjeffDto dto = new EtudiantjeffDto();
        dto.setId(etudiant.getEtudiantId());
        dto.setNom(etudiant.getNom());
        dto.setPrenom(etudiant.getPrenom());
        return dto;
    }
        //Récupérer la liste des étudiants d'un niveau
    public List<EtudiantjeffDto> getEtudiantsByNiveau(NiveauEtude niveau) {
        List<Etudiant> etudiants = etudiantjeffRepository.findByNiveauEtude(niveau);
        return etudiants.stream()
                .map(this::convertToEtudDto)
                .collect(Collectors.toList());
    }
    private EtudiantjeffDto convertToEtudDto(Etudiant etudiant) {
        EtudiantjeffDto dto = new EtudiantjeffDto();
        dto.setId(etudiant.getEtudiantId());
        dto.setMatricule(etudiant.getMatricule());
        dto.setNom(etudiant.getNom());
        dto.setPrenom(etudiant.getPrenom());
        dto.setFiliereNom(etudiant.getFiliere() != null ? etudiant.getFiliere().getNomFiliere() : "Non attribuée");
        dto.setNiveauEtude(etudiant.getNiveauEtude()); // Utiliser l'Enum ici
        dto.setDateNaiss(etudiant.getDateNaiss());
        return dto;
    }



    @Transactional
    public Etudiant getEtudiantById(Long id) {
        return etudiantjeffRepository.findEtudiantWithRelations(id);
    }


    //Les notes en fonction du niveau d'étude
    public List<Note> getNotesParNiveauEtude(Long etudiantId) {
        // Récupérer l'étudiant à partir de son ID
        Etudiant etudiant = etudiantjeffRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable"));

        // Déterminer les semestres associés au niveau d'étude
        List<TypeSemestre> semestresAssocies = getSemestresAssocies(etudiant.getNiveauEtude());

        // Récupérer les notes pour les semestres associés
        return getNotesBySemestres(etudiant, semestresAssocies);
    }

    // Méthode pour récupérer les semestres associés à un niveau d'étude
    private List<TypeSemestre> getSemestresAssocies(NiveauEtude niveauEtude) {
        switch (niveauEtude) {
            case PREMIERE_ANNEE:
                return Arrays.asList(TypeSemestre.SEMESTRE_1, TypeSemestre.SEMESTRE_2);
            case DEUXIEME_ANNEE:
                return Arrays.asList(TypeSemestre.SEMESTRE_3, TypeSemestre.SEMESTRE_4);
            case TROISIEME_ANNEE:
                return Arrays.asList(TypeSemestre.SEMESTRE_5, TypeSemestre.SEMESTRE_6);
            default:
                throw new RuntimeException("Niveau d'étude non reconnu");
        }
    }

    // Filtrer les notes d'un étudiant en fonction de semestres spécifiques
    public List<Note> getNotesBySemestres(Etudiant etudiant, List<TypeSemestre> semestres) {
        return etudiant.getNotes().stream()
                .filter(note -> note.getUe() != null && semestres.contains(note.getUe().getTypeSemestre()))  // Vérifier que l'UE n'est pas null
                .collect(Collectors.toList());
    }

    // Regrouper les notes par semestre
    public Map<TypeSemestre, List<Note>> getNotesBySemestres(Etudiant etudiant) {
        return etudiant.getNotes().stream()
                .filter(note -> note.getUe() != null)  // Vérification que l'UE n'est pas null
                .collect(Collectors.groupingBy(note -> note.getUe().getTypeSemestre()));
    }


    public Map<String, Object> getUeDetailsParSemestre(Long etudiantId) {
        // Logique existante pour récupérer les données des UE par semestre
        Etudiant etudiant = getEtudiantById(etudiantId);

        // Récupérer les notes par semestre pour cet étudiant
        Map<TypeSemestre, List<Note>> notesBySemestre = getNotesBySemestres(etudiant);
        Map<TypeSemestre, Map<String, Object>> ueDetailsBySemestre = new HashMap<>();

        double moyenneNiveau = 0.0;
        int totalSemestres = 0;

        for (Map.Entry<TypeSemestre, List<Note>> entry : notesBySemestre.entrySet()) {
            TypeSemestre semestre = entry.getKey();
            List<Note> notesSemestre = entry.getValue();

            // Grouper les notes par UE
            Map<UE, List<Note>> notesByUE = notesSemestre.stream().collect(Collectors.groupingBy(Note::getUe));
            List<Map<String, Object>> ueDetailsList = new ArrayList<>();

            int totalCreditsSemestre = 0;
            int creditsValides = 0;
            double moyenneSemestre = 0.0;

            for (Map.Entry<UE, List<Note>> ueEntry : notesByUE.entrySet()) {
                UE ue = ueEntry.getKey();
                List<Note> notesUE = ueEntry.getValue();

                // Calculer la moyenne de l'UE
                double moyenneUE = notesService.calculerMoyenneUE(notesUE);

                // Déterminer si l'UE est validée ou non (moyenne supérieure à 10)
                String valide = (moyenneUE >= 10) ? "OUI" : "NON";

                // Calculer l'appréciation
                String appreciation = notesService.AvoirAppreciation(moyenneUE);

                // Mettre à jour le total des crédits
                totalCreditsSemestre += ue.getCredit();
                if (moyenneUE >= 10) {
                    creditsValides += ue.getCredit();
                }

                // Construire les détails de l'UE
                Map<String, Object> ueDetails = new HashMap<>();
                ueDetails.put("codeUE", ue.getCode());
                ueDetails.put("libelle", ue.getLibelle());
                ueDetails.put("type", ue.getTypeUe());
                ueDetails.put("credit", ue.getCredit());
                ueDetails.put("note", moyenneUE);
                ueDetails.put("valide", valide);
                ueDetails.put("appreciation", appreciation);
                ueDetails.put("semestre", semestre);

                // Ajouter les détails de l'UE à la liste
                ueDetailsList.add(ueDetails);
            }

            // Calculer la moyenne du semestre
            moyenneSemestre = notesService.calculerMoyenneSemestre(notesSemestre);
            moyenneNiveau += moyenneSemestre;
            totalSemestres++;


            // Ajouter les détails du semestre à la map
            Map<String, Object> semestreDetails = new HashMap<>();
            semestreDetails.put("ueDetails", ueDetailsList);
            semestreDetails.put("moyenneSemestre", moyenneSemestre);
            semestreDetails.put("creditsValides", creditsValides);
            semestreDetails.put("totalCredits", totalCreditsSemestre);

            ueDetailsBySemestre.put(semestre, semestreDetails);
        }

        // Calculer la moyenne globale du niveau
        moyenneNiveau = (totalSemestres > 0) ? (moyenneNiveau / totalSemestres) : 0.0;
        String mention = notesService.AppreciMoyenne(moyenneNiveau);
        // Préparer la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("ueDetailsBySemestre", ueDetailsBySemestre);
        response.put("moyenneNiveau", moyenneNiveau);
        response.put("mention", mention);

        return response;
    }


    // Méthode pour obtenir le nombre total d'étudiants
    public long getTotalEtudiants() {
        return etudiantjeffRepository.countTotalEtudiants();
    }

    // Méthode pour obtenir le nombre d'étudiants par niveau d'étude
    public long getTotalEtudiantsParNiveau(NiveauEtude niveauEtude) {
        return etudiantjeffRepository.countEtudiantsParNiveau(niveauEtude);
    }

    public List<EtudiantMoyenneDto> getTop5EtudiantsByMoyenne() {
        Pageable pageable = PageRequest.of(0, 5); // Limiter à 5 étudiants
        List<Object[]> result = etudiantjeffRepository.findTop5EtudiantsByMoyenne(pageable);

        List<EtudiantMoyenneDto> top5 = new ArrayList<>();
        for (Object[] row : result) {
            Etudiant etudiant = (Etudiant) row[0];
            Double moyenne = (Double) row[1];
            top5.add(new EtudiantMoyenneDto(etudiant.getNom(), etudiant.getPrenom(), moyenne));
        }
        return top5;
    }

    public List<NiveauStatsDto> getStatsParNiveau() {
        List<NiveauEtude> niveaux = Arrays.asList(NiveauEtude.PREMIERE_ANNEE, NiveauEtude.DEUXIEME_ANNEE, NiveauEtude.TROISIEME_ANNEE);
        List<Object[]> result = etudiantjeffRepository.findStatsParNiveau(niveaux);

        List<NiveauStatsDto> stats = new ArrayList<>();
        for (Object[] row : result) {
            NiveauEtude niveau = (NiveauEtude) row[0];
            Long nbEtudiants = (Long) row[1];
            Double moyenneGenerale = (Double) row[2];
            Double tauxReussite = (Double) row[3];

            stats.add(new NiveauStatsDto(niveau, nbEtudiants, moyenneGenerale, tauxReussite));
        }
        return stats;
    }
}
//    public List<EtudiantjeffDto> getEtudiantsBySemestre(TypeSemestre typeSemestre) {
//        List<Etudiant> etudiants = etudiantjeffRepository.findEtudiantsByTypeSemestre(typeSemestre);
//        // Conversion en DTO si nécessaire
//        return etudiants.stream().map(this::convertToEtudiantDto).collect(Collectors.toList());
//    }
//        List<Note> notes = new ArrayList<>();
//        for (TypeSemestre semestre : semestresAssocies) {
//            notes.addAll(noteRepository.findByEtudiantAndTypeSemestre(etudiant, semestre));
//        }
