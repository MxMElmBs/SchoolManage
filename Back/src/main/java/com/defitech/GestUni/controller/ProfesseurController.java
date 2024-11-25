package com.defitech.GestUni.controller;

import com.defitech.GestUni.config.Azhar.ResourceNotFoundException;
import com.defitech.GestUni.dto.Azhar.CahierDto;
import com.defitech.GestUni.dto.Azhar.EtudiantDTOA;
import com.defitech.GestUni.dto.Azhar.HistoriquePresence;
import com.defitech.GestUni.dto.Azhar.PresenceDTO;
import com.defitech.GestUni.dto.ProfesseurDto;
import com.defitech.GestUni.dto.UEDto;
import com.defitech.GestUni.models.Azhar.CahierTexte;
import com.defitech.GestUni.models.Azhar.Seance;
import com.defitech.GestUni.models.Bases.Professeur;
import com.defitech.GestUni.service.Azhar.CahierTexteService;
import com.defitech.GestUni.service.Azhar.PresenceServices;
import com.defitech.GestUni.service.Azhar.SeanceServices;
import com.defitech.GestUni.service.Chahib.ProfesseurDocsDto;
import com.defitech.GestUni.service.ProfesseurServices;
import com.defitech.GestUni.service.UEServices;
import com.defitech.GestUni.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth/professeur")
public class ProfesseurController {

    @Autowired
    private ProfesseurServices professeurService;
    @Autowired
    private UEServices ueService;
    @Autowired
    private CahierTexteService cahierTexteService;
    @Autowired
    private PresenceServices presenceService;
    @Autowired
    private SeanceServices seanceService;
    @Autowired
    private UtilisateurService utilisateurService;
    /////////////////////////////////////////////////////////Seance//////////////////////////////////////
    // Endpoint pour ouvrir une séance
    @PostMapping("/ouvrir-seance/{ueId}")
    public ResponseEntity<Seance> createSeance(@PathVariable Long ueId) {
        Seance seance = seanceService.createSeance(ueId);
        return ResponseEntity.ok(seance);
    }



    @GetMapping("/seanceofue/{ueId}")
    public List<Seance> getSeancesByUeId(@PathVariable Long ueId) {
        return seanceService.getSeancesByUeId(ueId);
    }

    @GetMapping("/etudiantbyUE/{ueId}")
    public ResponseEntity<List<EtudiantDTOA>> getEtudiantsByUeId(@PathVariable Long ueId) {
        List<EtudiantDTOA> etudiants = presenceService.getEtudiantsByUeId(ueId);
        return ResponseEntity.ok(etudiants);
    }

    @GetMapping("/verifier-seance/{ueId}")
    public ResponseEntity<Map<String, Boolean>> verifierSeance(@PathVariable Long ueId) {
        boolean seanceEnCours = seanceService.verifierSeanceEnCours(ueId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("seanceEnCours", seanceEnCours);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/fermer-seance/{seanceId}")
    public ResponseEntity<String> fermerSeance(@PathVariable Long seanceId) {
        try {
            seanceService.closeSeance(seanceId);
            return ResponseEntity.ok("Séance fermée avec succès");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la fermeture de la séance");
        }
    }

    ////////////////////////////////////////////////////////Ue////////////////////////////////////////
    @GetMapping("/courbyprofesseur/{professeurId}")
    public List<UEDto> getUEsByProfesseur(@PathVariable Long professeurId) {
        return ueService.getUEsByProfesseur(professeurId);
    }
    ////////////////////////////////////////////profile///////////////////////////////////////////
    @GetMapping("profbyid/{id}")
    public Professeur getProfById(@PathVariable Long id) {
        return professeurService.getProfesseurById(id);
    }

    @PutMapping("update-prof/{id}")
    public Professeur updateProf(@PathVariable Long id, @RequestBody Professeur professeur) {
        professeur.setProfesseurId(id);
        return professeurService.saveProfesseur(professeur);
    }
    ///////////////////////////////////////////////Presences///////////////////////////////////////////
    @GetMapping("/presences-seance/{seanceId}")
    public List<PresenceDTO> getPresencesBySeance(@PathVariable Long seanceId) {
        return presenceService.getPresencesBySeance(seanceId);
    }

    // Endpoint pour prendre les presences
    @PostMapping("prendrepresences/{seanceId}")
    public ResponseEntity<Void> updatePresences(@PathVariable Long seanceId, @RequestBody List<PresenceDTO> presenceDTOs) {
        presenceService.updatePresences(seanceId, presenceDTOs);
        return ResponseEntity.ok().build();
    }
    // Endpoint pour prendre les presences et fermer la seance
    @PostMapping("/prendrepresencesCloreSeance/{seanceId}")
    public ResponseEntity<Void> updatePresencesAfterClose(@PathVariable Long seanceId, @RequestBody List<PresenceDTO> presenceDTOs) {
        presenceService.updatePresencesAfterClose(seanceId, presenceDTOs);
        return ResponseEntity.noContent().build();
    }
    //Historique des presences
    @GetMapping("/historique-presence/ue/{ueId}")
    public List<HistoriquePresence> getHistoriqueByUeId(@PathVariable Long ueId) {
        return presenceService.getHistoriqueByUeId(ueId);
    }

    @GetMapping("etat-seance/{seanceId}")
    public Map<String, Boolean> getSeanceEtat(@PathVariable Long seanceId) {
        boolean estFermee = seanceService.isSeanceFermee(seanceId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("fermee", estFermee);
        return response;
    }

    // Endpoint pour confirmer un cahier de texte
    @PutMapping("/confirmercahiertexte/{cahierTexteId}")
    public ResponseEntity<CahierTexte> confirmerCahierTexte(@PathVariable Long cahierTexteId) {
        CahierTexte cahierTexte = cahierTexteService.confirmerCahierTexte(cahierTexteId);
        return ResponseEntity.ok(cahierTexte);
    }

    @PutMapping("/updateOrConfirmercahiertexte/{id}")
    public ResponseEntity<CahierTexte> updateCahierTexte(@PathVariable Long id, @RequestBody CahierTexte dto) {
        CahierTexte updatedCahierTexte = cahierTexteService.updateOrModifyCahierTexte(id, dto);
        return ResponseEntity.ok(updatedCahierTexte);
    }
    @GetMapping("/cahier-details/{seanceId}")
    public ResponseEntity<CahierTexte> getCahierTexteBySeanceId(@PathVariable Long seanceId) {
        Optional<CahierTexte> cahierTexte = cahierTexteService.findCahierTexteBySeanceId(seanceId);
        return cahierTexte.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Endpoint pour confirmer un cahier de texte et fermer la séance
    @PostMapping("/confirmer-cahier/{cahierTexteId}")
    public ResponseEntity<CahierTexte> confirmerCahierTexteClose(@PathVariable Long cahierTexteId) {
        CahierTexte cahierTexte = cahierTexteService.confirmerCahierTexteClose(cahierTexteId);
        return ResponseEntity.ok(cahierTexte);
    }
    @GetMapping("/cahierParUe/{ueId}")
    public ResponseEntity<List<CahierDto>> getCahiersByUeId(@PathVariable Long ueId) {
        List<CahierDto> cahiers = cahierTexteService.findCahiersByUeId(ueId);
        return ResponseEntity.ok(cahiers);
    }
    // Endpoint pour créer un cahier de texte par un professeur et fermer la séance
    @PostMapping("/create-cahier")
    public ResponseEntity<CahierTexte> createCahierTexteByProf(@RequestBody CahierTexte cahierTexteDTO) {
        CahierTexte cahierTexte = cahierTexteService.createCahierTexteByProf(cahierTexteDTO);
        return ResponseEntity.ok(cahierTexte);
    }

    @GetMapping("/checkEnregistrer/{seanceId}")
    public ResponseEntity<Boolean> checkIfCahierEnregistrer(@PathVariable Long seanceId) {
        boolean hasCahierEnregistrer = cahierTexteService.hasCahierEnregistrerForSeance(seanceId);
        return ResponseEntity.ok(hasCahierEnregistrer);
    }

    @GetMapping("/professeurInfo/{idUser}")
    public ResponseEntity<ProfesseurDto> getProfesseurByUserId(@PathVariable("idUser") Long idUser) {
        ProfesseurDto professeurDto = utilisateurService.getProfesseurByUserId(idUser);
        return ResponseEntity.ok(professeurDto);
    }

}
