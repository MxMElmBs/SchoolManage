package com.defitech.GestUni.models.BAKA.JeffService;

import com.defitech.GestUni.enums.TypeSemestre;
import com.defitech.GestUni.models.Bases.Etudiant;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportService {
    @Autowired
    private EtudiantjeffService etudiantjeffService;

    public void exporterBulletinExcel(Long etudiantId, HttpServletResponse response) throws IOException {
        // Récupérer les données de l'étudiant
        Etudiant etudiant = etudiantjeffService.getEtudiantById(etudiantId);
        Map<String, Object> bulletinData = etudiantjeffService.getUeDetailsParSemestre(etudiantId);

        String nomFichier = "Bulletin_" + etudiant.getNom() + "_" + etudiant.getMatricule() + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + nomFichier);

        // Créer le fichier Excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Bulletin de Notes");

        // Styles pour le fichier Excel
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);

        int rowNum = 0;

        // Titre principal
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Bulletin de Notes");
        titleCell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        // Informations sur l'étudiant
        Row infoRow = sheet.createRow(rowNum++);
        infoRow.createCell(0).setCellValue("Nom : " + etudiant.getNom());
        infoRow.createCell(1).setCellValue("Prénom : " + etudiant.getPrenom());
        infoRow.createCell(2).setCellValue("Matricule : " + etudiant.getMatricule());
        infoRow.createCell(3).setCellValue("Niveau d'étude : " + etudiant.getNiveauEtude().name());

        // Récupérer les détails par semestre
        Map<TypeSemestre, Map<String, Object>> ueDetailsBySemestre = (Map<TypeSemestre, Map<String, Object>>) bulletinData.get("ueDetailsBySemestre");

        for (Map.Entry<TypeSemestre, Map<String, Object>> entry : ueDetailsBySemestre.entrySet()) {
            TypeSemestre semestre = entry.getKey();
            Map<String, Object> semestreDetails = entry.getValue();
            List<Map<String, Object>> ueDetailsList = (List<Map<String, Object>>) semestreDetails.get("ueDetails");

            // Titre du semestre
            Row semestreRow = sheet.createRow(rowNum++);
            Cell semestreCell = semestreRow.createCell(0);
            semestreCell.setCellValue("Semestre : " + semestre.name());
            semestreCell.setCellStyle(headerStyle);

            // Ajouter les en-têtes de colonnes
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("Code UE");
            headerRow.createCell(1).setCellValue("Unité d'Enseignement");
            headerRow.createCell(2).setCellValue("Type");
            headerRow.createCell(3).setCellValue("Crédit");
            headerRow.createCell(4).setCellValue("Note");
            headerRow.createCell(5).setCellValue("Validée");
            headerRow.createCell(6).setCellValue("Appréciation");

            // Ajouter les UE au fichier Excel
            for (Map<String, Object> ueDetails : ueDetailsList) {
                Row ueRow = sheet.createRow(rowNum++);
                ueRow.createCell(0).setCellValue((String) ueDetails.get("codeUE"));
                ueRow.createCell(1).setCellValue((String) ueDetails.get("libelle"));
                ueRow.createCell(2).setCellValue((String) ueDetails.get("type"));
                ueRow.createCell(3).setCellValue(String.valueOf(ueDetails.get("credit")));
                ueRow.createCell(4).setCellValue(String.valueOf(ueDetails.get("note")));
                ueRow.createCell(5).setCellValue((String) ueDetails.get("valide"));
                ueRow.createCell(6).setCellValue((String) ueDetails.get("appreciation"));
            }

            // Afficher les moyennes et crédits
            Row moyenneRow = sheet.createRow(rowNum++);
            moyenneRow.createCell(0).setCellValue("Moyenne Semestre : " + semestreDetails.get("moyenneSemestre"));
            moyenneRow.createCell(1).setCellValue("Crédits Validés : " + semestreDetails.get("creditsValides"));
            moyenneRow.createCell(2).setCellValue("Total Crédits : " + semestreDetails.get("totalCredits"));
        }

        // Moyenne du niveau et mention
        Row niveauRow = sheet.createRow(rowNum++);
        niveauRow.createCell(0).setCellValue("Moyenne du Niveau : " + bulletinData.get("moyenneNiveau"));
        niveauRow.createCell(1).setCellValue("Mention : " + bulletinData.get("mention"));

        // Auto-sizing the columns
        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ecrire le fichier Excel
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
