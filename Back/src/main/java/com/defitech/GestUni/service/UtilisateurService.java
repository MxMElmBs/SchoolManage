package com.defitech.GestUni.service;

import com.defitech.GestUni.dto.*;
import com.defitech.GestUni.enums.UserRole;
import com.defitech.GestUni.models.Bases.*;
import com.defitech.GestUni.repository.*;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UtilisateurService {
    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;
    @Autowired
    private ProfesseurServices professeurService;

    @Autowired
    private OtherUserRepository ouserRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DirectEtudRepository directEtudRepository;
    @Autowired
    private DirectEtudeService directEtudeService;


    @Transactional
    public List<UserDto> getUserByRoleCo(UserRole role) {
        List<UserDto> userDtos = new ArrayList<>();
        List<Utilisateur> utilisateurs = utilisateurRepository.findByRole(role);
        for (Utilisateur utilisateur : utilisateurs) {
            UserDto userDto = new UserDto();
            userDto.setIdUser(utilisateur.getIdUser());
            userDto.setEmail(utilisateur.getEmail());
            userDto.setNom(utilisateur.getOtherUser().getNom());
            userDto.setPrenom(utilisateur.getOtherUser().getPrenom());
            userDto.setUsername(utilisateur.getUsername());
            userDto.setPassword(utilisateur.getPassword());
            userDto.setActif(utilisateur.getActif());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Transactional
    public List<UserDto> getUserByRoleDe(UserRole role) {
        List<UserDto> userDtos = new ArrayList<>();
        List<Utilisateur> utilisateurs = utilisateurRepository.findByRole(role);
        for (Utilisateur utilisateur : utilisateurs) {
            UserDto userDto = new UserDto();
            userDto.setIdUser(utilisateur.getIdUser());
            userDto.setEmail(utilisateur.getEmail());
            userDto.setNom(utilisateur.getDirecteurEtude().getDirecteurEtudeNom());
            userDto.setPrenom(utilisateur.getDirecteurEtude().getDirecteurEtudePrenom());
            userDto.setUsername(utilisateur.getUsername());
            userDto.setPassword(utilisateur.getPassword());
            userDto.setActif(utilisateur.getActif());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Transactional
    public List<UserDto> getUserByRoleEtud(UserRole role) {
        List<UserDto> userDtos = new ArrayList<>();
        List<Utilisateur> utilisateurs = utilisateurRepository.findByRole(role);

        for (Utilisateur utilisateur : utilisateurs) {
            UserDto userDto = new UserDto();
            userDto.setIdUser(utilisateur.getIdUser());
            userDto.setEmail(utilisateur.getEmail());
            userDto.setUsername(utilisateur.getUsername());
            userDto.setPassword(utilisateur.getPassword());
            userDto.setActif(utilisateur.getActif());

            // Check if the utilisateur has an associated Etudiant
            if (utilisateur.getEtudiant() != null) {
                userDto.setNom(utilisateur.getEtudiant().getNom());
                userDto.setPrenom(utilisateur.getEtudiant().getPrenom());
            } else {
                // Handle the case where the etudiant is null
                userDto.setNom("Unknown");
                userDto.setPrenom("Unknown");
            }

            userDtos.add(userDto);
        }

        return userDtos;
    }


    @Transactional
    public List<UserDto> getUserByRoleProf(UserRole role) {
        List<UserDto> userDtos = new ArrayList<>();
        List<Utilisateur> utilisateurs = utilisateurRepository.findByRole(role);
        for (Utilisateur utilisateur : utilisateurs) {
            UserDto userDto = new UserDto();
            userDto.setIdUser(utilisateur.getIdUser());
            userDto.setEmail(utilisateur.getEmail());
            userDto.setNom(utilisateur.getProfesseur().getNom());
            userDto.setPrenom(utilisateur.getProfesseur().getPrenom());
            userDto.setUsername(utilisateur.getUsername());
            userDto.setPassword(utilisateur.getPassword());
            userDto.setActif(utilisateur.getActif());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Transactional
    public void creerUtilisateurPourEtudiantsSansUtilisateur() {
        // Récupérer les étudiants sans utilisateur associé
        List<Etudiant> etudiants = etudiantRepository.findEtudiantsSansUtilisateur();

        for (Etudiant etudiant : etudiants) {
            // Générer un nom d'utilisateur
            String username = genererNomUtilisateur(etudiant);

            // Générer un mot de passe aléatoire
            String motDePasse = genererMotDePasseAleatoire();

            // Hacher le mot de passe avec BCrypt

            // Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setUsername(username);
            utilisateur.setEmail(etudiant.getEmail());
            utilisateur.setPassword(motDePasse);
            utilisateur.setRole(UserRole.ETUDIANT);
            utilisateur.setActif("Oui");
            utilisateur.setPrint("Non");

            // Associer l'utilisateur à l'étudiant
            utilisateur.setEtudiant(etudiant);
            utilisateurRepository.save(utilisateur);

            // Mettre à jour l'étudiant avec l'utilisateur associé
            etudiant.setUtilisateur(utilisateur);
            etudiantRepository.save(etudiant);
        }
    }

    @Transactional
    public void creerUtilisateurPourOuserSansUtilisateur() {
        // Récupérer les étudiants sans utilisateur associé
        List<OtherUser> ousers = ouserRepository.findOUserSansUtilisateur();

        for (OtherUser ouser : ousers) {
            // Générer un nom d'utilisateur
            String username = genererNomUtilisateur(ouser);

            // Générer un mot de passe aléatoire
            String motDePasse = genererMotDePasseAleatoire();

            // Hacher le mot de passe avec BCrypt

            // Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setUsername(username);
            utilisateur.setEmail(ouser.getEmail());
            utilisateur.setPassword(motDePasse);
            utilisateur.setRole(UserRole.COMPTABLE);
            utilisateur.setActif("Oui");
            utilisateur.setPrint("Non");

            // Associer l'utilisateur à l'étudiant
            utilisateur.setOtherUser(ouser);
            utilisateurRepository.save(utilisateur);

            // Mettre à jour l'étudiant avec l'utilisateur associé
            ouser.setUtilisateur(utilisateur);
            ouserRepository.save(ouser);
        }
    }

    @Transactional
    public void creerUtilisateurPourProfSansUtilisateur() {
        // Récupérer les étudiants sans utilisateur associé
        List<Professeur> profs = professeurRepository.findProfSansUtilisateur();

        for (Professeur prof  : profs) {
            // Générer un nom d'utilisateur
            String username = genererNomUtilisateur(prof);

            // Générer un mot de passe aléatoire
            String motDePasse = genererMotDePasseAleatoire();

            // Hacher le mot de passe avec BCrypt

            // Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setUsername(username);
            utilisateur.setEmail(prof.getEmail());
            utilisateur.setPassword(motDePasse);
            utilisateur.setRole(UserRole.PROFESSEUR);
            utilisateur.setActif("Oui");
            utilisateur.setPrint("Non");

            // Associer l'utilisateur à l'étudiant
            utilisateur.setProfesseur(prof);
            utilisateurRepository.save(utilisateur);

            // Mettre à jour l'étudiant avec l'utilisateur associé
            prof.setUtilisateur(utilisateur);
            professeurRepository.save(prof);
        }
    }

    @Transactional
    public void creerUtilisateurPourDESansUtilisateur() {
        // Récupérer les étudiants sans utilisateur associé
        List<DirecteurEtude> des = directEtudRepository.findDirectEtudSansUtilisateur();

        for (DirecteurEtude de : des) {
            // Générer un nom d'utilisateur
            String username = genererNomUtilisateur(de);

            // Générer un mot de passe aléatoire
            String motDePasse = genererMotDePasseAleatoire();

            // Hacher le mot de passe avec BCrypt

            // Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setUsername(username);
            utilisateur.setEmail(de.getEmail());
            utilisateur.setPassword(motDePasse);
            utilisateur.setRole(UserRole.DE);
            utilisateur.setActif("Oui");
            utilisateur.setPrint("Non");

            // Associer l'utilisateur à l'étudiant
            utilisateur.setDirecteurEtude(de);
            utilisateurRepository.save(utilisateur);

            // Mettre à jour l'étudiant avec l'utilisateur associé
            de.setUtilisateur(utilisateur);
            directEtudRepository.save(de);
        }
    }

    private String genererNomUtilisateur(Professeur prof) {
        String idPart = String.format("%04d", prof.getProfesseurId());
        String nomPart = prof.getNom().substring(0, Math.min(3, prof.getNom().length()));
        String prenomPart = prof.getPrenom().substring(0, Math.min(3, prof.getPrenom().length()));
        return idPart + nomPart + prenomPart;
    }

    private String genererNomUtilisateur(OtherUser etudiant) {
        String idPart = String.format("%04d", etudiant.getOtherUserId());
        String nomPart = etudiant.getNom().substring(0, Math.min(3, etudiant.getNom().length()));
        String prenomPart = etudiant.getPrenom().substring(0, Math.min(3, etudiant.getPrenom().length()));
        return idPart + nomPart + prenomPart;
    }

    // Générer un nom d'utilisateur unique
    private String genererNomUtilisateur(Etudiant etudiant) {
        String idPart = String.format("%04d", etudiant.getEtudiantId());
        String nomPart = etudiant.getNom().substring(0, Math.min(3, etudiant.getNom().length()));
        String prenomPart = etudiant.getPrenom().substring(0, Math.min(3, etudiant.getPrenom().length()));
        return idPart + nomPart + prenomPart;
    }

    private String genererNomUtilisateur(DirecteurEtude etudiant) {
        String idPart = String.format("%04d", etudiant.getDirecteurEtudeId());
        String nomPart = etudiant.getDirecteurEtudeNom().substring(0, Math.min(3, etudiant.getDirecteurEtudeNom().length()));
        String prenomPart = etudiant.getDirecteurEtudePrenom().substring(0, Math.min(3, etudiant.getDirecteurEtudePrenom().length()));
        return idPart + nomPart + prenomPart;
    }

    // Générer un mot de passe aléatoire
    private String genererMotDePasseAleatoire() {
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789èé$£/!?";
        StringBuilder motDePasse = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            int randomIndex = (int) (Math.random() * charset.length());
            motDePasse.append(charset.charAt(randomIndex));
        }
        return motDePasse.toString();
    }

    public Integer countByUserRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Le rôle ne peut pas être null");
        }
        return utilisateurRepository.countByRole(role);
    }

    public void updateUserPrint(UserDto userDto, String print) {
        Utilisateur utilisateur  = utilisateurRepository.findByIdUser(userDto.getIdUser());
        String passwordEncode = passwordEncoder.encode(utilisateur.getPassword());
        utilisateur.setPassword(passwordEncode);
        utilisateur.setPrint(print);
        utilisateurRepository.save(utilisateur);
    }

    public EtudiantDto getEtudiantByUserId(Long idUser) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (utilisateur.getEtudiant() != null) {
            return getEtudiantById(utilisateur.getEtudiant().getEtudiantId());
        }
        return null;
    }
    public EtudiantDto getEtudiantById(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));

        EtudiantDto etudiantDto = new EtudiantDto();
        etudiantDto.setEtudiantId(etudiant.getEtudiantId());
        etudiantDto.setNom(etudiant.getNom());
        etudiantDto.setPrenom(etudiant.getPrenom());
        etudiantDto.setEmail(etudiant.getEmail());
        // Set other fields as needed
        return etudiantDto;
    }



    public ProfesseurDto getProfesseurByUserId(Long idUser) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (utilisateur.getProfesseur() != null) {
            return professeurService.getProfesseurdtoById(utilisateur.getProfesseur().getProfesseurId());
        }
        return null;
    }

    public DirecteurEtudeDto getDirecteurEtudeByUserId(Long idUser) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (utilisateur.getDirecteurEtude() != null) {
            return directEtudeService.getDirecteurEtudeById(utilisateur.getDirecteurEtude().getDirecteurEtudeId());
        }
        return null;
    }


    public UtilisateurCoDto getUserCoDto(Long idUser) {
        Utilisateur user = utilisateurRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UtilisateurCoDto userCoDto = new UtilisateurCoDto();

        if ("DE".equals(user.getRole().name())) {
            // Vérification pour éviter NullPointerException
            if (user.getDirecteurEtude() != null) {
                userCoDto.setId(user.getDirecteurEtude().getDirecteurEtudeId());
                userCoDto.setNom(user.getDirecteurEtude().getDirecteurEtudeNom());
                userCoDto.setPrenom(user.getDirecteurEtude().getDirecteurEtudePrenom());
            }
        } else if ("PROFESSEUR".equals(user.getRole().name())){
            // Vérification pour éviter NullPointerException
            if (user.getProfesseur() != null) {
                userCoDto.setId(user.getProfesseur().getProfesseurId());
                userCoDto.setNom(user.getProfesseur().getNom());
                userCoDto.setPrenom(user.getProfesseur().getPrenom());
            }
        }

        return userCoDto;
    }


}
