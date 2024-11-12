package com.defitech.GestUni.repository;

import com.defitech.GestUni.models.Chahib.Document;
import com.defitech.GestUni.models.Chahib.TypeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByThemeContainingIgnoreCase(String theme);

    // Utilisation de la syntaxe correcte pour vérifier l'existence par l'ID de l'étudiant et le type de document
    boolean existsByEtudiant_EtudiantIdAndTypeDocument(Long etudiantId, TypeDocument typeDocument);


    // Requête personnalisée pour rechercher des documents par thème, type de document, et nom du parcours
    @Query("SELECT d FROM Document d WHERE " +
            "LOWER(d.theme) LIKE LOWER(CONCAT('%', :theme, '%')) OR " +
            "LOWER(d.parcours.nomParcours) LIKE LOWER(CONCAT('%', :theme, '%')) OR "+
            "LOWER(d.etudiant.nom) LIKE LOWER(CONCAT('%', :theme, '%')) OR "+
            "LOWER(d.etudiant.prenom) LIKE LOWER(CONCAT('%', :theme, '%'))")
    List<Document> findByThemeContainingIgnoreCaseAndTypeDocumentAndNomParcours(
            @Param("theme") String theme);


    List<Document> findByEtudiant_EtudiantId(Long etudiantId);

    // Vérifier si un document existe avec un thème spécifique
    boolean existsByTheme(String theme);

    // Repository : Vérifier si un document existe pour un étudiant et un type de document
    boolean existsByEtudiantEtudiantIdAndTypeDocument(Long etudiantId, TypeDocument typeDocument);

    @Query("SELECT d FROM Document d WHERE d.professeur.professeurId = :professeurId")
    List<Document> findByProfesseurProfesseurId(Long professeurId);

}
