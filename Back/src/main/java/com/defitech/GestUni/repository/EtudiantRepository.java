package com.defitech.GestUni.repository;

import com.defitech.GestUni.enums.NiveauEtude;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.Filiere;
import com.defitech.GestUni.models.Bases.Parcours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    @Query("SELECT e FROM Etudiant e WHERE e.utilisateur.idUser = :idUser")
    Optional<Etudiant> findByUtilisateurIdUser(@Param("idUser") Long idUser);

    @Query("SELECT e FROM Etudiant e WHERE e.utilisateur IS NULL")
    List<Etudiant> findEtudiantsSansUtilisateur();

    List<Etudiant> findByNomAndPrenom(String nom, String prenom);
    List<Etudiant> findByParcoursNomParcoursAndNiveauEtude(String nomParcours, NiveauEtude niveau);

    Optional<Etudiant> findFirstByNomAndPrenom(String nom, String prenom);


    @Query("SELECT COUNT(e) FROM Etudiant e")
    long countTotalEtudiants();

    @Query("SELECT e FROM Etudiant e WHERE e.filiere.nomFiliere = :nomFiliere AND e.niveauEtude = :niveauEtude")
    List<Etudiant> findAllByFiliereAndNiveauEtude(@Param("nomFiliere") String nomFiliere, @Param("niveauEtude") NiveauEtude niveauEtude);

    long countByParcours(Parcours parcours);

    long countByParcoursAndSexe(Parcours parcours, String sexe);

    List<Etudiant> findByFiliereAndNiveauEtude(Filiere filiere, NiveauEtude niveauEtude);

    @Query("SELECT e FROM Etudiant e JOIN FETCH e.filiere f WHERE f.nomFiliere = :nomFiliere AND e.niveauEtude = :niveauEtude")
    List<Etudiant> findEtudiantsByFiliereAndNiveau(@Param("nomFiliere") String nomFiliere, @Param("niveauEtude") NiveauEtude niveauEtude);

    List<Etudiant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    Optional<Etudiant> findByMatricule(String matricule);

    Optional<Etudiant> findByEtudiantId(Long id);


    List<Etudiant> findByNomStartingWithOrPrenomStartingWith(String nom, String prenom);

    List<Etudiant> findByFiliereNomFiliereAndNiveauEtude(String nomFiliere, NiveauEtude niveauEtude);


    long countByFiliereAndNiveauEtudeAndParcours(Filiere filiere, NiveauEtude niveauEtude, Parcours parcours);


    long countByFiliereAndNiveauEtudeAndParcoursAndSexe(Filiere filiere, NiveauEtude niveauEtude, Parcours parcours, String sexe);




}
