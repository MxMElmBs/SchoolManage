package com.defitech.GestUni.service;

import com.defitech.GestUni.dto.Note.*;
import com.defitech.GestUni.enums.TypeNote;
import com.defitech.GestUni.models.BAKA.Exception.ResourceNotFoundException;
import com.defitech.GestUni.models.BAKA.JeffRepository.EtudiantjeffRepository;
import com.defitech.GestUni.models.BAKA.JeffRepository.UejeffRepository;
import com.defitech.GestUni.models.BAKA.Note;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.UE;
import com.defitech.GestUni.repository.NoteRepository;
import com.defitech.GestUni.repository.UERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private EtudiantjeffRepository etudiantjeffRepository;
    @Autowired
    private UejeffRepository uejeffRepository;

    @Transactional
    public void enregistrerNotes(List<SaisieNoteDto> saisieNotes) {
        if (saisieNotes.isEmpty()) {
            throw new RuntimeException("La liste des notes à enregistrer est vide.");
        }

        // Récupérer le type d'UE et le type de note de la première note
        SaisieNoteDto premierSaisieNote = saisieNotes.get(0);

        if (premierSaisieNote.getTypeNote() == TypeNote.EXAMEN) {
            // Vérifier s'il existe déjà des notes de type EXAMEN pour cette UE
            boolean examenExiste = noteRepository.existsByUeUeIdAndTypeNote(premierSaisieNote.getUeId(), TypeNote.EXAMEN);

            if (examenExiste) {
                throw new RuntimeException("Des notes de type EXAMEN existent déjà pour cette UE : " + premierSaisieNote.getUeId());
            }
        }

        if (premierSaisieNote.getTypeNote() == TypeNote.DEVOIR) {
            // Vérifier s'il existe déjà des notes de type DEVOIR pour cette UE
            boolean devoirExiste = noteRepository.existsByUeUeIdAndTypeNote(premierSaisieNote.getUeId(), TypeNote.DEVOIR);
            if (devoirExiste) {
                throw new RuntimeException("Des notes de type DEVOIR existent déjà pour cette UE : " + premierSaisieNote.getUeId());
            }
        }

        // Enregistrement des notes
        for (SaisieNoteDto saisieNoteDto : saisieNotes) {
            System.out.println("Enregistrement des données pour Etudiant ID: " + saisieNoteDto.getEtudiantId() + ", UE ID: " + saisieNoteDto.getUeId());

            // Validation de la valeur de la note
            if (saisieNoteDto.getValeurNote() < 0 || saisieNoteDto.getValeurNote() > 20) {
                throw new RuntimeException("La valeur de la note doit être comprise entre 0 et 20 pour l'étudiant " + saisieNoteDto.getEtudiantId());
            }

            Note note = new Note();
            note.setValeur(saisieNoteDto.getValeurNote());
            note.setTypeNote(saisieNoteDto.getTypeNote());

            // Récupérer l'étudiant et l'UE
            Etudiant etudiant = etudiantjeffRepository.findById(saisieNoteDto.getEtudiantId())
                    .orElseThrow(() -> new RuntimeException("Etudiant non trouvé : " + saisieNoteDto.getEtudiantId()));
            note.setEtudiant(etudiant);

            UE ue = uejeffRepository.findById(saisieNoteDto.getUeId())
                    .orElseThrow(() -> new RuntimeException("UE non trouvée : " + saisieNoteDto.getUeId()));
            note.setUe(ue);

            // Le semestre sera récupéré depuis l'UE
            note.setTypeSemestre(ue.getTypeSemestre());

            // Enregistrer la note
            noteRepository.save(note);
        }
    }
    @Transactional
    public void modifierNotes(List<SaisieNoteDto> saisieNotes) {
        for (SaisieNoteDto saisieNoteDto : saisieNotes) {
            Note note = noteRepository.findByEtudiantAndUeAndTypeNote(
                    saisieNoteDto.getEtudiantId(), saisieNoteDto.getUeId(), saisieNoteDto.getTypeNote()
            );
            if (note != null) {
                note.setValeur(saisieNoteDto.getValeurNote());
                noteRepository.save(note);
            }
        }
    }
    //PAGE NOTE modifier les notes des UE
    public List<NoteEtudiantDto> getNotesEtudiantsParUEEtTypeNote(Long ueId, TypeNote typeNote) {
        List<Note> notes = noteRepository.findByUeAndTypeNote(ueId, typeNote);
        return notes.stream().map(this::convertToNotesEtudiantDto).collect(Collectors.toList());
    }

    // Convertir les notes en DTO contenant les informations nécessaires
    private NoteEtudiantDto convertToNotesEtudiantDto(Note note) {
        NoteEtudiantDto dto = new NoteEtudiantDto();
        dto.setEtudiantId(note.getNoteId());
        dto.setEtudiantId(note.getEtudiant().getEtudiantId());
        dto.setMatricule(note.getEtudiant().getMatricule());
        dto.setNom(note.getEtudiant().getNom());
        dto.setPrenom(note.getEtudiant().getPrenom());
        dto.setValeur(note.getValeur());
        return dto;
    }


    @Transactional
    public void supprimerNotesParUEEtType(Long ueId, TypeNote typeNote) {
        List<Note> notesASupprimer = noteRepository.findByUe_UeIdAndTypeNote(ueId, typeNote);

        if (!notesASupprimer.isEmpty()) {
            noteRepository.deleteAll(notesASupprimer);
        } else {
            throw new RuntimeException("Aucune note trouvée pour l'UE et le type de note spécifié.");
        }
    }

    //Modifier une note d'un étudiant par UE
    public NoteDto getNoteByEtudiantAndUe(Long etudiantId, Long ueId) {
        Note note = noteRepository.findNoteByEtudiantAndUe(etudiantId, ueId)
                .orElseThrow(() -> new RuntimeException("Note non trouvée pour cet étudiant et cette UE"));
        return convertToNoteDto(note);
    }

    public NoteDto updateNote(Long noteId, double nouvelleValeur) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note non trouvée"));
        note.setValeur((float) nouvelleValeur);
        noteRepository.save(note);
        return convertToNoteDto(note);
    }

    private NoteDto convertToNoteDto(Note note) {
        NoteDto dto = new NoteDto();
        dto.setId(note.getNoteId());
        dto.setValeur(note.getValeur());
        dto.setTypeNote(note.getTypeNote());
        dto.setEtudiantId(note.getEtudiant().getEtudiantId());
        dto.setEtudiantNom(note.getEtudiant().getNom());
        dto.setEtudiantPrenom(note.getEtudiant().getPrenom());
        dto.setUeId(note.getUe().getUeId());
        dto.setUeCode(note.getUe().getCode());
        dto.setUeLibelle(note.getUe().getLibelle());
        return dto;
    }

    public List<NoteDto> getNotesByEtudiant(Long etudiantId) {
        return noteRepository.findByEtudiantId(etudiantId)
                .stream()
                .map(this::convertToNotedeuxDto) // Conversion de l'entité Note vers NoteDto
                .collect(Collectors.toList());
    }

    private NoteDto convertToNotedeuxDto(Note note) {
        NoteDto dto = new NoteDto();
        dto.setId(note.getNoteId());
        dto.setEtudiantId(note.getEtudiant().getEtudiantId());
        dto.setEtudiantNom(note.getEtudiant().getNom());
        dto.setEtudiantPrenom(note.getEtudiant().getPrenom());
        dto.setUeId(note.getUe().getUeId());
        dto.setUeCode(note.getUe().getCode());
        dto.setUeLibelle(note.getUe().getLibelle());
        dto.setUeCredit(note.getUe().getCredit());
        dto.setValeur(note.getValeur());
        dto.setTypeNote(note.getTypeNote());
        dto.setSemestre(note.getTypeSemestre());
        return dto;
    }

    //Modifier les notes d'un étudiant dans la page étudiant
    // Récupérer une note par son ID
    public NoteDto getNoteById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found for this id :: " + noteId));

        return convertToNoteDto(note);
    }

    // Mettre à jour une note
    public NoteDto updateNoteEtudiant(Long noteId, NoteDto noteDto) {
        Note existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found for this id :: " + noteId));

        // Mettre à jour la valeur de la note
        existingNote.setValeur(noteDto.getValeur());

        // Sauvegarder la note mise à jour
        Note updatedNoteEtudiant = noteRepository.save(existingNote);

        return convertToNoteEtudiantDto(updatedNoteEtudiant);
    }

    private NoteDto convertToNoteEtudiantDto(Note note) {
        NoteDto dto = new NoteDto();
        dto.setEtudiantId(note.getEtudiant().getEtudiantId());
        dto.setEtudiantNom(note.getEtudiant().getNom());
        dto.setEtudiantPrenom(note.getEtudiant().getPrenom());
        dto.setUeId(note.getUe().getUeId());
        dto.setValeur(note.getValeur());
        dto.setTypeNote(note.getTypeNote());
        dto.setSemestre(note.getTypeSemestre());
        return dto;
    }

    //Surppmimer une note d'un étudiant
    // Supprimer une note par son ID
    public void deleteNote(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found for this id :: " + noteId));

        noteRepository.delete(note);
    }

    //Calcul de la moyenne d'une UE
    public double calculerMoyenneUE(List<Note> notesUE) {
        double devoirTotal = 0.0;
        double examenTotal = 0.0;
        int nbDevoirs = 0;
        int nbExamens = 0;

        for (Note note : notesUE) {
            if (note.getTypeNote() == TypeNote.DEVOIR) {
                devoirTotal += note.getValeur();
                nbDevoirs++;
            } else if (note.getTypeNote() == TypeNote.EXAMEN) {
                examenTotal += note.getValeur();
                nbExamens++;
            }
        }

        // Pondération (40% devoir, 60% examen)
        double moyenneDevoir = (nbDevoirs > 0) ? devoirTotal / nbDevoirs : 0.0;
        double moyenneExamen = (nbExamens > 0) ? examenTotal / nbExamens : 0.0;
        double moyenneUE = (moyenneDevoir * 0.4) + (moyenneExamen * 0.6);

        return arrondir(moyenneUE, 2);
    }

    //calcul de la moyenne d'un semestre
    public double calculerMoyenneSemestre(List<Note> notesSemestre) {
        Map<UE, List<Note>> notesParUE = notesSemestre.stream()
                .collect(Collectors.groupingBy(Note::getUe));

        double totalMoyenne = 0.0;
        int totalCredits = 0;

        for (Map.Entry<UE, List<Note>> entry : notesParUE.entrySet()) {
            UE ue = entry.getKey();
            List<Note> notesUE = entry.getValue();

            double moyenneUE = calculerMoyenneUE(notesUE);
            totalMoyenne += moyenneUE * ue.getCredit();
            totalCredits += ue.getCredit();
        }

        double moyenneSemestre = (totalCredits > 0) ? totalMoyenne / totalCredits : 0.0;

        // Arrondir la moyenne du semestre à deux décimales
        return arrondir(moyenneSemestre, 2);
    }


    //Calcul des crédits validés
    public int calculerCreditsValides(List<Note> notesSemestre) {
        Map<UE, List<Note>> notesParUE = notesSemestre.stream()
                .collect(Collectors.groupingBy(Note::getUe));

        int totalCreditsValides = 0;

        for (Map.Entry<UE, List<Note>> entry : notesParUE.entrySet()) {
            UE ue = entry.getKey();
            List<Note> notesUE = entry.getValue();

            double moyenneUE = calculerMoyenneUE(notesUE);
            if (moyenneUE >= 10.0) {  // Condition de validation d'une UE
                totalCreditsValides += ue.getCredit();
            }
        }

        return totalCreditsValides;
    }

    //
    public String AvoirAppreciation(double note) {
        if (note < 10) {
            return "-";
        } else if (note >= 10 && note < 12) {
            return "PASSABLE";
        } else if (note >= 12 && note < 14) {
            return "ASSEZ BIEN";
        } else if (note >= 14 && note < 16) {
            return "BIEN";
        } else {
            return "T-BIEN";
        }
    }

    public String AppreciMoyenne(double moyenneNiveau) {
        if (moyenneNiveau < 10) {
            return "INSUFFISANT";
        } else if (moyenneNiveau >= 10 && moyenneNiveau < 12) {
            return "PASSABLE";
        } else if (moyenneNiveau >= 12 && moyenneNiveau < 14) {
            return "ASSEZ BIEN";
        } else if (moyenneNiveau >= 14 && moyenneNiveau < 16) {
            return "BIEN";
        } else {
            return "T-BIEN";
        }
    }

    // Méthode utilitaire pour arrondir à deux chiffres après la virgule
    private double arrondir(double valeur, int precision) {
        BigDecimal bd = BigDecimal.valueOf(valeur);
        System.out.println("Valeur avant arrondi : " + bd.doubleValue());
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        System.out.println("Valeur après arrondi : " + bd.doubleValue());
        return bd.doubleValue();
    }

    // Calcul de la distribution des notes
    public Map<String, Long> calculerDistributionDesNotes() {
        List<Note> allNotes = noteRepository.findAll();
        Map<String, Long> distributionNotes = new HashMap<>();

        // Grouper par tranches de notes
        distributionNotes.put("10-12", allNotes.stream().filter(note -> note.getValeur() >= 10 && note.getValeur() < 12).count());
        distributionNotes.put("12-14", allNotes.stream().filter(note -> note.getValeur() >= 12 && note.getValeur() < 14).count());
        distributionNotes.put("14-16", allNotes.stream().filter(note -> note.getValeur() >= 14 && note.getValeur() < 16).count());
        distributionNotes.put("16-18", allNotes.stream().filter(note -> note.getValeur() >= 16 && note.getValeur() < 18).count());
        distributionNotes.put("18-20", allNotes.stream().filter(note -> note.getValeur() >= 18 && note.getValeur() <= 20).count());

        return distributionNotes;
    }


//    public void enregistrerNotes(EnregistrementNoteDto dto) {
//        for (EtudiantNoteDto etudiantNoteDto : dto.getEtudiants()) {
//            Note note = new Note();
//            note.setValeur(etudiantNoteDto.getNote());
//            note.setTypeNote(dto.getTypeNote());
//            note.setEtudiant(etudiantjeffRepository.findById(etudiantNoteDto.getEtudiantId()).orElseThrow());
//            note.setUe(UERepository.findById(dto.getUeId()).orElseThrow());
//            note.setTypeSemestre(dto.getTypeSemestre());
//            noteRepository.save(note);
//        }
//    }





//    public List<Note> getNotesByEtudiant(Long etudiantId) {
//        return noteRepository.findByEtudiant_EtudiantId(etudiantId);
//    }
//
//    public List<Note> getNotesByUe(Long ueId) {
//        return noteRepository.findByUe_UeId(ueId);
//    }
//
//    public void saisirNotes(List<NoteDto> notes) {
//        List<Note> notesEntities = new ArrayList<>();
//        for (NoteDto noteDto : notes) {
//            Note note = new Note();
//            note.setValeur(noteDto.getValeur());
//            note.setTypeNote(noteDto.getTYpeNote());
//            note.setAppreciation(noteDto.getAppreciation());
//            note.setEtudiant(noteDto.getEtudiant());
//            note.setUe(noteDto.getUe());
////            note.setAnneescolaire(noteDto.getAnneescolaire());
//            notesEntities.add(note);
//        }
//        noteRepository.saveAll(notesEntities);
//    }

// Méthode pour la liste des UE notés par Semestre
//    public List<UE> getUeNotedBySemestre(TypeSemestre semestre) {
//        return noteRepository.findUeNotedBySemestre(semestre);
//    }
//
//
//    public List<UE> getUeWithNotes() {
//        return noteRepository.findDistinctUeWithNotes();
//    }
//
//    public List<UE> getUeWithNotesBySemestre(TypeSemestre typeSemestre) {
//        return getUeWithNotes().stream()
//                .filter(ue -> ue.getTypeSemestre() == typeSemestre)
//                .collect(Collectors.toList());
//    }
}
//    public List<UeNoteDto> getUeAndSemestreAndNoteByEtudiant(Long etudiantId) {
//        List<Object[]> results = noteRepository.findUeAndSemestreAndNoteByEtudiant(etudiantId);
//        List<UeNoteDto> ueNotes = new ArrayList<>();
//        for (Object[] result : results) {
//            UE ue = (UE) result[0];
//            TypeSemestre semestre = (TypeSemestre) result[1];
//            Float note = (Float) result[2];
//            ueNotes.add(new UeNoteDto(ue, semestre, note));
//        }
//        return ueNotes;
//    }