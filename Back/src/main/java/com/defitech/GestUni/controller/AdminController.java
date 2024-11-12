package com.defitech.GestUni.controller;

import com.defitech.GestUni.dto.DirecteurEtudeDto;
import com.defitech.GestUni.dto.OtherUserDto;
import com.defitech.GestUni.dto.UserDto;
import com.defitech.GestUni.enums.UserRole;
import com.defitech.GestUni.models.Bases.DirecteurEtude;
import com.defitech.GestUni.models.Bases.OtherUser;
import com.defitech.GestUni.models.Bases.Parcours;
import com.defitech.GestUni.service.DirectEtudeService;
import com.defitech.GestUni.service.Max.AdminMailServices;
import com.defitech.GestUni.service.OtherUserService;
import com.defitech.GestUni.service.ParcoursServices;
import com.defitech.GestUni.service.UtilisateurService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth/admin")
public class AdminController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private DirectEtudeService directeurEtudeService;

    @Autowired
    private OtherUserService otherUserService;

    @Autowired
    private ParcoursServices parcoursService;

    @Autowired
    private AdminMailServices emailService;


    @PostMapping("/ajouter-de")
    public ResponseEntity<DirecteurEtude> ajouterDirecteurEtude(@RequestBody DirecteurEtudeDto dto) {
        DirecteurEtude directeurEtude = directeurEtudeService.ajouterDirecteurEtude(dto);
        return ResponseEntity.ok(directeurEtude);
    }

    @PostMapping("/ajouter-co")
    public ResponseEntity<OtherUser> ajouterDirecteurEtude(@RequestBody OtherUserDto dto) {
        OtherUser ouser = otherUserService.ajouterOtherUser(dto);
        return ResponseEntity.ok(ouser);
    }

    @GetMapping("/creer-utilisateurs-student")
    public String creerStudentUtilisateurs() {
        utilisateurService.creerUtilisateurPourEtudiantsSansUtilisateur();
        return "Utilisateurs créés avec succès";
    }

    @GetMapping("/creer-utilisateurs-prof")
    public String creerTeacherUtilisateurs() {
        utilisateurService.creerUtilisateurPourProfSansUtilisateur();
        return "Utilisateurs créés avec succès";
    }

    @GetMapping("/creer-utilisateurs-co")
    public String creerOtherUtilisateurs() {
        utilisateurService.creerUtilisateurPourOuserSansUtilisateur();
        return "Utilisateurs créés avec succès";
    }

    @GetMapping("/creer-utilisateurs-de")
    public String creerDEUtilisateurs() {
        utilisateurService.creerUtilisateurPourDESansUtilisateur();
        return "Utilisateurs créés avec succès";
    }

    @GetMapping("/getUserByRole")
    public ResponseEntity<List<UserDto>> getUserByRole(@RequestParam UserRole role) {
        List<UserDto> userDtos;
        // Vérification du type de rôle
        if (role == UserRole.DE) {
            // Appel de la méthode pour le rôle Directeur d'Études (DE)
            userDtos = utilisateurService.getUserByRoleDe(role);
        } else if (role == UserRole.COMPTABLE) {
            // Appel de la méthode pour le rôle Comptable (CO)
            userDtos = utilisateurService.getUserByRoleCo(role);
        } else if (role == UserRole.ETUDIANT) {
            // Appel de la méthode pour le rôle Comptable (CO)
            userDtos = utilisateurService.getUserByRoleEtud(role);
        } else if (role == UserRole.PROFESSEUR) {
            // Appel de la méthode pour le rôle Comptable (CO)
            userDtos = utilisateurService.getUserByRoleProf(role);
        } else {
            return ResponseEntity.badRequest().body(null);  // Ou une liste vide selon les besoins
        }

        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/count")
    public Integer countByRole(@RequestParam UserRole role) {
        return utilisateurService.countByUserRole(role);
    }

    @GetMapping("/all-parcours")
    public List<Parcours> getAllParcours() {
        return parcoursService.getAllParcours();
    }

    @GetMapping("/sendCredentials")
    public ResponseEntity<Map<String, String>> sendCredentials(
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        if (email == null || email.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Invalid user data provided"));
        }

        // Appel du service pour l'envoi de l'email avec les identifiants
        emailService.sendCredentials(email, username, password);

        // Retourner une réponse positive sous forme de JSON
        return ResponseEntity.ok(Collections.singletonMap("message", "Credentials sent successfully!"));
    }


    @PostMapping("/updateUserPrint")
        public ResponseEntity<String> updateUserPrint(@RequestBody UserDto updateUserStatusDTO) {
            // Vérification des paramètres dans le DTO
            if (updateUserStatusDTO == null) {
                return ResponseEntity.badRequest().body("Invalid data provided");
            }

            // Mise à jour de l'état de l'utilisateur
            utilisateurService.updateUserPrint(updateUserStatusDTO, "Oui");

            // Réponse en cas de succès
            return ResponseEntity.ok("User status updated successfully!");
        }


}
