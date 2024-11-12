package com.defitech.GestUni.service.Max;

import com.defitech.GestUni.dto.EtudiantCoursDto;
import com.defitech.GestUni.dto.Note.EtudiantNoteMaxDto;
import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.BAKA.Note;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.Filiere;
import com.defitech.GestUni.models.Bases.UE;
import com.defitech.GestUni.models.Bases.Utilisateur;
import com.defitech.GestUni.repository.EtudiantRepository;
import com.defitech.GestUni.repository.NoteRepository;
import com.defitech.GestUni.repository.UERepository;
import com.defitech.GestUni.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EtudiantCoursService {
    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private UERepository ueRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private NoteRepository noteRepository;

    private final DateTimeFormatter FRENCH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public List<EtudiantCoursDto> getCoursByIdUserAndSemestre(Long idUser, TypeSemestre semestre) {
        // Récupérer l'utilisateur
        Utilisateur user = utilisateurRepository.findByIdUser(idUser);

        if (user == null) {
            throw new IllegalArgumentException("Aucun utilisateur trouvé avec cet ID.");
        }

        // Vérifier si l'utilisateur a un étudiant associé
        Etudiant etudiant = user.getEtudiant();
        if (etudiant == null) {
            throw new IllegalArgumentException("Aucun étudiant trouvé pour cet utilisateur.");
        }

        // Vérifier que l'étudiant a bien une filière associée
        Filiere filiere = etudiant.getFiliere();
        if (filiere == null) {
            throw new IllegalArgumentException("Aucune filière trouvée pour cet étudiant.");
        }

        // Récupérer les UE (cours) via le parcours, la filière et le semestre
        List<UE> ues = ueRepository.findByEtudiantAndFiliereAndSemestre(filiere.getFiliereId(), semestre);

        // Transformer les données en DTO
        List<EtudiantCoursDto> dtoList = new ArrayList<>();
        for (UE ue : ues) {
            EtudiantCoursDto dto = new EtudiantCoursDto(
                    filiere.getNomFiliere(),
                    ue.getCode(),
                    ue.getLibelle(),
                    ue.getNiveauEtude().name(),
                    ue.getCredit(),
                    ue.getTypeSemestre().name(),
                    ue.getTypeUe(),
                    ue.getDescriptUe()
            );
            dtoList.add(dto);
        }

        return dtoList;
    }


//    public List<EtudiantCoursDto> getCoursByIdUser(Long idUser) {
//        // Récupérer l'étudiant via son id_user
//        Utilisateur user = utilisateurRepository.findById(idUser)
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//        // Récupérer l'étudiant via son id_user
//        Etudiant etudiant = etudiantRepository.findById(user.getEtudiant().getEtudiantId())
//                .orElseThrow(() -> new EntityNotFoundException("Étudiant non trouvé pour l'utilisateur avec id " + idUser));
//
//        // Récupérer les UE (cours) via le parcours, la filière, et le semestre
//        List<UE> ues = ueRepository.findByFilieres(etudiant.getFiliere());
//
//        // Transformer les données en DTO
//        List<EtudiantCoursDto> dtoList = new ArrayList<>();
//        for (UE ue : ues) {
//            EtudiantCoursDto dto = new EtudiantCoursDto(
//                    etudiant.getFiliere().getNomFiliere(),
//                    ue.getCode(),
//                    ue.getLibelle(),
//                    ue.getNiveauEtude().name(),
//                    ue.getCredit(),
//                    ue.getTypeSemestre().name()
//            );
//            dtoList.add(dto);
//        }
//
//        return dtoList;
//    }

    public List<EtudiantCoursDto> getCoursByIdUtilisateur(Long idUser) {
        // Récupérer l'utilisateur via l'id
        Utilisateur user = utilisateurRepository.findByIdUser(idUser);

        // Vérifier si l'utilisateur a un étudiant associé
        Etudiant etudiant = user.getEtudiant();

        if (etudiant == null) {
            throw new IllegalArgumentException("Aucun étudiant trouvé pour cet utilisateur.");
        }

        // Récupérer les UEs de l'étudiant dans la filière
        List<UE> ues = ueRepository.findByFiliere(etudiant.getFiliere().getFiliereId());

        // Transformer les UE en DTO
        return ues.stream()
                .map(ue -> new EtudiantCoursDto(
                        etudiant.getFiliere().getNomFiliere(),
                        ue.getCode(),
                        ue.getLibelle(),
                        ue.getNiveauEtude().name(),
                        ue.getCredit(),
                        ue.getTypeSemestre().name(),
                        ue.getTypeUe(),
                        ue.getDescriptUe()
                ))
                .collect(Collectors.toList());
    }

    public EtudiantMaxConnect getEtudiantMaxConnect(Long userId) {
        // Fetch the user from the repository using the provided userId
        Utilisateur user = utilisateurRepository.findByIdUser(userId);

        // Find the associated student (Etudiant) based on the user's etudiantId
        Etudiant etudiant = etudiantRepository.findById(user.getEtudiant().getEtudiantId())
                .orElseThrow(() -> new EntityNotFoundException("Etudiant not found for the connected user."));
        // Format the date of birth in French format, handle null case
        String formattedDateNaiss = etudiant.getDateNaiss() != null
                ? etudiant.getDateNaiss().format(FRENCH_DATE_FORMATTER)
                : "N/A";

        // Map the Etudiant entity to EtudiantMaxConnect DTO
        EtudiantMaxConnect etudiantMaxConnect = new EtudiantMaxConnect(
                etudiant.getEtudiantId(),
                etudiant.getMatricule(),
                etudiant.getNom(),
                etudiant.getPrenom(),
                etudiant.getEmail(),
                etudiant.getLieuNaiss(),
                etudiant.getSexe(),
                formattedDateNaiss,
                etudiant.getParcours().getNomParcours(),
                etudiant.getNiveauEtude().name()
                // Pass the formatted date here
        );
        return etudiantMaxConnect;
    }


    public List<EtudiantNoteMaxDto> getNotesByEtudiantIdAndFiliere(Long userId) {
        Utilisateur user = utilisateurRepository.findByIdUser(userId);
        // Find the associated student (Etudiant) based on the user's etudiantId
        Etudiant etudiant = etudiantRepository.findById(user.getEtudiant().getEtudiantId())
                .orElseThrow(() -> new EntityNotFoundException("Etudiant not found for the connected user."));

        List<Note> notes = noteRepository.findNotesByEtudiantIdAndFiliere(etudiant.getEtudiantId(), etudiant.getFiliere().getFiliereId());

        // Group notes by UE (Course Unit)
        Map<Long, List<Note>> notesByUe = notes.stream().collect(Collectors.groupingBy(note -> note.getUe().getUeId()));

        List<EtudiantNoteMaxDto> dtoList = new ArrayList<>();

        // Map grouped notes to DTO, handling two notes per UE
        for (Map.Entry<Long, List<Note>> entry : notesByUe.entrySet()) {
            List<Note> ueNotes = entry.getValue();
            if (ueNotes.size() >= 2) {
                Note note1 = ueNotes.get(0);
                Note note2 = ueNotes.get(1);

                EtudiantNoteMaxDto dto = new EtudiantNoteMaxDto(
                        note1.getUe().getCode(),
                        note1.getUe().getLibelle(),
                        note1.getValeur(),
                        note2.getValeur(),
                        note1.getUe().getTypeUe(),
                        note1.getUe().getTypeSemestre() // Assuming both notes have the same semester
                );

                dtoList.add(dto);
            }
        }

        return dtoList;
    }

}
