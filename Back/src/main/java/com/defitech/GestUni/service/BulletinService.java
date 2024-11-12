package com.defitech.GestUni.service;


import com.defitech.GestUni.dto.Note.UeNoteDto;
import com.defitech.GestUni.models.BAKA.Note;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.repository.EtudiantRepository;
import com.defitech.GestUni.repository.NoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BulletinService {

    @Autowired
    private EtudiantRepository jeffRepository;

    @Autowired
    private NoteRepository noteRepository;

//    public List<UeNoteDto> getBulletinForEtudiant(Long etudiantId) {
//        List<UeNoteDto> ueNoteDtos = new ArrayList<>();
//
//        List<Note> notes = noteRepository.findByEtudiant_EtudiantId(etudiantId);
//
//        for (Note note : notes) {
//            UeNoteDto ueNoteDto = new UeNoteDto();
//            ueNoteDto.setCode(note.getUe().getCode());
//            ueNoteDto.setLibelle(note.getUe().getLibelle());
//            ueNoteDto.setCredit(note.getUe().getCredit());
//            ueNoteDto.setNote(note.getValeur());
//            ueNoteDto.setSemestre(ueNoteDto.getSemestre());
//
//            ueNoteDtos.add(ueNoteDto);
//        }
//
//        return ueNoteDtos;
//    }

//    public EtudiantNoteDto getEtudiantInfo(Long etudiantId) {
//        Etudiant etudiant = jeffRepository.findById(etudiantId)
//                .orElseThrow(() -> new EntityNotFoundException("Etudiant not found"));
//
//        EtudiantNoteDto etudiantDto = new EtudiantNoteDto();
//        etudiantDto.setEtudiantId(etudiant.getEtudiantId());
//        etudiantDto.setMatricule(etudiant.getMatricule());
//        etudiantDto.setNom(etudiant.getNom());
//        etudiantDto.setPrenom(etudiant.getPrenom());
//        etudiantDto.setDateNaissance(etudiant.getDateNaiss());
//        etudiantDto.setFiliere(etudiant.getFiliere().getNomFiliere());
//
//        return etudiantDto;
//    }


}
