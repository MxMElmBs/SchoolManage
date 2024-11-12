package com.defitech.GestUni.models.BAKA.JeffService;

import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.Bases.Etudiant;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import com.defitech.GestUni.models.BAKA.JeffService.EtudiantjeffService;
import org.springframework.beans.factory.annotation.Autowired;
import com.defitech.GestUni.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Service
public class BulletinPdfService {

    @Autowired
    private EtudiantjeffService etudiantjeffService;
    @Autowired
    private JeffEmailService emailService;

    public void genererBulletinPDF(Long etudiantId, HttpServletResponse response) throws IOException {
        // Récupérer les données de l'étudiant
        Etudiant etudiant = etudiantjeffService.getEtudiantById(etudiantId);
        Map<String, Object> bulletinData = etudiantjeffService.getUeDetailsParSemestre(etudiantId);

        String nomFichier = "Bulletin_" + etudiant.getNom() + "_" + etudiant.getMatricule() + ".pdf";
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + nomFichier);

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Titre principal
        document.add(new Paragraph("RELEVE DE NOTES")
                .setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));

        // Informations de l'étudiant
        document.add(new Paragraph("Nom : " + etudiant.getNom()));
        document.add(new Paragraph("Prénom : " + etudiant.getPrenom()));
        document.add(new Paragraph("Matricule : " + etudiant.getMatricule()));
        document.add(new Paragraph("Date de naissance : " + etudiant.getDateNaiss()));
        document.add(new Paragraph("Niveau d'étude : " + etudiant.getNiveauEtude().name()));
        document.add(new Paragraph("\n"));  // Saut de ligne


        // Récupérer les détails par semestre
        Map<TypeSemestre, Map<String, Object>> ueDetailsBySemestre = (Map<TypeSemestre, Map<String, Object>>) bulletinData.get("ueDetailsBySemestre");

        for (Map.Entry<TypeSemestre, Map<String, Object>> entry : ueDetailsBySemestre.entrySet()) {
            TypeSemestre semestre = entry.getKey();
            Map<String, Object> semestreDetails = entry.getValue();
            List<Map<String, Object>> ueDetailsList = (List<Map<String, Object>>) semestreDetails.get("ueDetails");

            // Titre du semestre
            document.add(new Paragraph(semestre.name())
                    .setBold().setFontSize(10).setTextAlignment(TextAlignment.CENTER));

            // Tableau des UE
            Table table = new Table(new float[]{1, 3, 2, 1, 1, 1, 2});
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell(new Cell().add(new Paragraph("Code UE")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Unité d'Enseignement")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Type")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Crédit")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Note")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Validé")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Appréciation")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

            // Ajouter les UE au tableau
            for (Map<String, Object> ueDetails : ueDetailsList) {
                table.addCell(new Cell().add(new Paragraph((String) ueDetails.get("codeUE"))));
                table.addCell(new Cell().add(new Paragraph((String) ueDetails.get("libelle"))));
                table.addCell(new Cell().add(new Paragraph((String) ueDetails.get("type"))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(ueDetails.get("credit")))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(ueDetails.get("note")))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(ueDetails.get("valide")))));
                table.addCell(new Cell().add(new Paragraph((String) ueDetails.get("appreciation"))));
            }
            document.add(table);

            // Afficher les moyennes et crédits
            Table table2 = new Table(3);  // 3 colonnes pour afficher les informations
            table2.setWidth(UnitValue.createPercentValue(100));
// Ajouter la moyenne du semestre (à gauche)
            Cell moyenneCell = new Cell().add(new Paragraph(" "));
            moyenneCell.setBorder(Border.NO_BORDER);  // Supprime les bordures
            table2.addCell(moyenneCell);

// Ajouter le total des crédits (au centre)
            Cell totalCreditsCell = new Cell().add(new Paragraph("Moyenne Semestre : " + semestreDetails.get("moyenneSemestre")+"                       TOTAL CREDIT : " + semestreDetails.get("totalCredits")));
            totalCreditsCell.setFontSize(10).setTextAlignment(TextAlignment.CENTER);  // Alignement au centre
            totalCreditsCell.setBorder(Border.NO_BORDER);
            moyenneCell.setPadding(10f);
            table2.addCell(totalCreditsCell);

// Ajouter les crédits validés (à droite)
            Cell creditsValidesCell = new Cell().add(new Paragraph("TOTAL CREDIT VALIDE : " + semestreDetails.get("creditsValides")));
            creditsValidesCell.setFontSize(10).setTextAlignment(TextAlignment.RIGHT);  // Alignement à droite
            creditsValidesCell.setBorder(Border.NO_BORDER);
            moyenneCell.setPadding(10f);
            table2.addCell(creditsValidesCell);

// Ajouter le tableau au document
            document.add(table2);
        }

        document.add(new Paragraph("\n"));
        // Moyenne du niveau
        document.add(new Paragraph("MOYENNE ANNUELLE : " + bulletinData.get("moyenneNiveau")).setBold());
        document.add(new Paragraph("MENTION : " + bulletinData.get("mention")).setBold());

        // Pied de page
        document.add(new Paragraph("Le Directeur Général")
                .setBold().setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph("Lomé, le " + LocalDate.now()));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("AMOUZOU Kossi Ali")
                .setBold().setTextAlignment(TextAlignment.RIGHT));
        document.close();
    }

    public void genererEtEnvoyerBulletinPDF(Long etudiantId) throws IOException, MessagingException {
        // Récupérer les données de l'étudiant
        Etudiant etudiant = etudiantjeffService.getEtudiantById(etudiantId);
        Map<String, Object> bulletinData = etudiantjeffService.getUeDetailsParSemestre(etudiantId);

        // Créer le nom du fichier PDF
        String nomFichier = etudiant.getNom() + "_" + etudiant.getMatricule() + ".pdf";

        // Générer le PDF
        Path tempFile = Files.createTempFile(nomFichier, ".pdf");
        try (OutputStream outputStream = Files.newOutputStream(tempFile);
             PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // Ajoutez le contenu du PDF comme dans votre méthode existante
            document.add(new Paragraph("Bulletin de Notes")
                    .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Nom : " + etudiant.getNom()));
            document.add(new Paragraph("Prénom : " + etudiant.getPrenom()));
            document.add(new Paragraph("Matricule : " + etudiant.getMatricule()));
            document.add(new Paragraph("Date de naissance : " + etudiant.getDateNaiss()));
            document.add(new Paragraph("Niveau d'étude : " + etudiant.getNiveauEtude().name()));
            document.add(new Paragraph("\n"));

            Map<TypeSemestre, Map<String, Object>> ueDetailsBySemestre = (Map<TypeSemestre, Map<String, Object>>) bulletinData.get("ueDetailsBySemestre");

            for (Map.Entry<TypeSemestre, Map<String, Object>> entry : ueDetailsBySemestre.entrySet()) {
                TypeSemestre semestre = entry.getKey();
                Map<String, Object> semestreDetails = entry.getValue();
                List<Map<String, Object>> ueDetailsList = (List<Map<String, Object>>) semestreDetails.get("ueDetails");

                document.add(new Paragraph("Semestre : " + semestre.name())
                        .setBold().setFontSize(14).setTextAlignment(TextAlignment.LEFT));

                Table table = new Table(new float[]{1, 3, 2, 1, 1, 1, 2});
                table.setWidth(UnitValue.createPercentValue(100));
                table.addHeaderCell(new Cell().add(new Paragraph("Code UE")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Unité d'Enseignement")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Type")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Crédit")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Note")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Validé")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Appréciation")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

                for (Map<String, Object> ueDetails : ueDetailsList) {
                    table.addCell(new Cell().add(new Paragraph((String) ueDetails.get("codeUE"))));
                    table.addCell(new Cell().add(new Paragraph((String) ueDetails.get("libelle"))));
                    table.addCell(new Cell().add(new Paragraph((String) ueDetails.get("type"))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(ueDetails.get("credit")))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(ueDetails.get("note")))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(ueDetails.get("valide")))));
                    table.addCell(new Cell().add(new Paragraph((String) ueDetails.get("appreciation"))));
                }
                document.add(table);

                document.add(new Paragraph("Moyenne Semestre : " + semestreDetails.get("moyenneSemestre")));
                document.add(new Paragraph("Crédits Validés : " + semestreDetails.get("creditsValides")));
                document.add(new Paragraph("Total Crédits : " + semestreDetails.get("totalCredits")));
                document.add(new Paragraph("\n"));
            }

            document.add(new Paragraph("Moyenne du Niveau : " + bulletinData.get("moyenneNiveau")).setBold());
            document.add(new Paragraph("Mention : " + bulletinData.get("mention")).setBold());
        }

        String mailetudiant = etudiant.getEmail();
        // Envoyer l'email avec le PDF en pièce jointe
        emailService.sendEmail(mailetudiant, "Votre bulletin", "Veuillez trouver en pièce jointe votre bulletin.", tempFile.toString());

        // Supprimer le fichier temporaire après l'envoi
        Files.deleteIfExists(tempFile);
    }
}
