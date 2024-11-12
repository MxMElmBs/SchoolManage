package com.defitech.GestUni.repository;

import com.defitech.GestUni.enums.UserRole;
import com.defitech.GestUni.models.Bases.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    List<Utilisateur> findByRole(UserRole userRole);

    boolean existsByEmail(String email);

    Optional<Utilisateur> findByUsername(String username);


    Integer countByRole(UserRole userRole);

    Utilisateur findByIdUser(Long id);




    //    @Modifying
//    @Transactional
//    @Query(value = "CALL CreerComptesEtudiants()", nativeQuery = true)
//    void creerComptesUserEtudiant();
//
//    @Modifying
//    @Transactional
//    @Query(value = "CALL CreerComptesProfesseurs()", nativeQuery = true)
//    void creerComptesUserProfs();
//
//    @Modifying
//    @Transactional
//    @Query(value = "CALL CreerComptesDirecteurs()", nativeQuery = true)
//    void creerComptesUserDE();
//
//    @Modifying
//    @Transactional
//    @Query(value = "CALL CreerComptesComptables()", nativeQuery = true)
//    void creerComptesUserComptable();
//
//    @Modifying
//    @Transactional
//    @Query(value = "CALL CreerComptesSecretaire()", nativeQuery = true)
//    void creerComptesUserSecretaire();
//
//    @Query("SELECT new com.defitech.projetmemoire2.dto.UtilisateurDTO(u.idDe,u.direcEtude.nom, u.direcEtude.prenom, u.username, u.password, u.direcEtude.emailDe, u.actif) " +
//            "FROM Utilisateur u " +
//            "WHERE u.direcEtude IS NOT NULL")
//    List<UtilisateurDTO> findUserDetailsByDirecEtude();
//
//    @Query("SELECT new com.defitech.projetmemoire2.dto.UtilisateurDTO(u.idEtudiant,u.etudiant.nom, u.etudiant.prenom, u.username, u.password, u.etudiant.emailS, u.actif) " +
//            "FROM Utilisateur u " +
//            "WHERE u.etudiant IS NOT NULL")
//    List<UtilisateurDTO> findUserDetailsByEtudiant();
//
//    @Query("SELECT new com.defitech.projetmemoire2.dto.UtilisateurDTO(u.idProfesseur,u.professeur.nom, u.professeur.prenom, u.username, u.password, u.professeur.emailPro, u.actif) " +
//            "FROM Utilisateur u " +
//            "WHERE u.professeur IS NOT NULL")
//    List<UtilisateurDTO> findUserDetailsByProfesseur();
//
//    @Query("SELECT new com.defitech.projetmemoire2.dto.UtilisateurDTO(u.idOther,u.otherUser.nomUser, u.otherUser.prenomUser, u.username, u.password, u.otherUser.emailUser, u.actif) " +
//            "FROM Utilisateur u " +
//            "WHERE u.otherUser IS NOT NULL AND u.role = 'Secrétaire'")
//    List<UtilisateurDTO> findUserDetailsBySecretaire();
//
//    @Query("SELECT new com.defitech.projetmemoire2.dto.UtilisateurDTO(u.idOther,u.otherUser.nomUser, u.otherUser.prenomUser, u.username, u.password, u.otherUser.emailUser, u.actif) " +
//            "FROM Utilisateur u " +
//            "WHERE u.otherUser IS NOT NULL AND u.role = 'Comptable'")
//    List<UtilisateurDTO> findUserDetailsByComptable();
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE Utilisateur u SET u.username = :username, u.password = :password, u.actif = :actif " +
//            "WHERE u.idEtudiant = :idUser")
//    void updateUserByIdEtudiant(@Param("username") String username,
//                                @Param("password") String password,
//                                @Param("actif") String actif,
//                                @Param("idUser") Long idUser);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE Utilisateur u SET u.username = :username, u.password = :password, u.actif = :actif " +
//            "WHERE u.idDe = :idUser")
//    void updateUserByIdDE(@Param("username") String username,
//                          @Param("password") String password,
//                          @Param("actif") String actif,
//                          @Param("idUser") Long idUser);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE Utilisateur u SET u.username = :username, u.password = :password, u.actif = :actif " +
//            "WHERE u.idOther = :idUser")
//    void updateUserByIdOU(@Param("username") String username,
//                          @Param("password") String password,
//                          @Param("actif") String actif,
//                          @Param("idUser") Long idUser);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE Utilisateur u SET u.username = :username, u.password = :password, u.actif = :actif " +
//            "WHERE u.idProfesseur = :idUser")
//    void updateUserByIdPro(@Param("username") String username,
//                           @Param("password") String password,
//                           @Param("actif") String actif,
//                           @Param("idUser") Long idUser);

    @Modifying
    @Transactional
    @Query("UPDATE Utilisateur u SET u.print = :print  " +
            "WHERE u.username = :username AND u.password = :password")
    void updateUserPrint(String username, String password, String print);

    Integer countByRole(String role);
}
