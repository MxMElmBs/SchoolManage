package com.defitech.GestUni.service.Chahib;

import com.defitech.GestUni.models.Bases.Professeur;
import com.defitech.GestUni.models.Chahib.Document;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("chahibEmailService")
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Bienvenue chez Citizen");

            String htmlMsg = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                    "<div style='text-align: center; margin-bottom: 20px;'>" +
                    "<img src='https://example.com/logo.png' alt='Company Logo' style='max-width: 150px;' />" +
                    "</div>" +
                    "<h2 style='color: #4CAF50;'>Bienvenue, " + username + " !</h2>" +
                    "<p>Nous sommes ravis de vous accueillir dans notre communauté.</p>" +
                    "<p style='margin-top: 20px;'>Pour commencer, nous vous encourageons à explorer votre compte.</p>" +

                    "<p>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                    "<p>Cordialement,<br> </p>" +
                    "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                    "<p>&copy; 2024 . Tous droits réservés.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de bienvenue à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de bienvenue.", e);
        }
    }

    ///////////////////////////////////////// Document ////////////////////////////////

    public void sendCreationDocumentProf(String toEmail, Document document) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Notification de dépôt d'un document – Direction de mémoire");

            String htmlMsg = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                    "<h2 style='color: #4CAF50; text-align: center;'>Dépôt d'un document</h2>" +
                    "<p>Madame/Monsieur <strong>" + document.getProfesseur().getNom() + " " + document.getProfesseur().getPrenom() + "</strong>,</p>" +
                    "<p>Nous avons le plaisir de vous informer qu’un étudiant(e) vous a choisi en tant que directeur(trice) de mémoire pour son projet. Voici les détails :</p>" +
                    "<ul>" +
                    "<li><strong>Étudiant(e) :</strong> " + document.getEtudiant().getNom() + " " + document.getEtudiant().getPrenom() + "</li>" +
                    "<li><strong>Type de document :</strong> " + document.getTypeDocument() + "</li>" +
                    "<li><strong>Thème du document :</strong> « " + document.getTheme() + " »</li>" +
                    "</ul>" +
                    "<p>Nous vous encourageons à entrer en contact avec l’étudiant(e) pour la planification des prochaines étapes académiques. Votre rôle en tant que directeur(trice) de mémoire est essentiel à la réussite de ce projet.</p>" +
                    "<p>Si vous avez des questions ou si vous souhaitez plus de précisions, n’hésitez pas à nous contacter. Nous restons à votre disposition pour toute information complémentaire.</p>" +
                    "<p>Bien cordialement,<br>L'administration DEFITECH</p>" +
                    "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                    "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de dépôt de document à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de dépôt de document.", e);
        }
    }
    public void sendConfirmationDocumentEtudiant(String toEmail, Document document) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Confirmation de dépôt de votre document");

            String htmlMsg = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                    "<h2 style='color: #4CAF50; text-align: center;'>Confirmation de dépôt de document</h2>" +
                    "<p>Cher(e) <strong>" + document.getEtudiant().getPrenom() + " " + document.getEtudiant().getNom() + "</strong>,</p>" +
                    "<p>Nous vous confirmons la bonne réception de votre document intitulé <strong>« " + document.getTheme() + " »</strong>, dans la catégorie <strong>" + document.getTypeDocument() + "</strong>.</p>" +
                    "<p>Votre directeur(trice) de mémoire, <strong>Madame/Monsieur " + document.getProfesseur().getNom() + " " + document.getProfesseur().getPrenom() + "</strong>, a été informé(e) du dépôt de ce document et prendra prochainement contact avec vous pour planifier les prochaines étapes.</p>" +
                    "<p>Nous vous souhaitons plein succès dans la réalisation de votre mémoire. Si vous avez des questions ou si vous avez besoin d'une assistance supplémentaire, n'hésitez pas à nous contacter. Nous sommes à votre disposition pour vous accompagner tout au long de ce processus.</p>" +
                    "<p>Bien cordialement,<br>L'administration DEFITECH</p>" +
                    "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                    "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de confirmation de dépôt à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de confirmation de dépôt.", e);
        }
    }
    public void sendDocumentUpdateProf(String toEmail, Document document) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Notification de mise à jour du document");

            String htmlMsg = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                    "<h2 style='color: #4CAF50; text-align: center;'>Mise à jour du document de l'étudiant</h2>" +
                    "<p>Madame/Monsieur <strong>" + document.getProfesseur().getNom() + " " + document.getProfesseur().getPrenom() + "</strong>,</p>" +
                    "<p>Nous vous informons que l'étudiant(e) <strong>" + document.getEtudiant().getNom() + " " + document.getEtudiant().getPrenom() + "</strong> a mis à jour son document intitulé <strong>« " + document.getTheme() + " »</strong> dans la catégorie <strong>" + document.getTypeDocument() + "</strong>.</p>" +
                    "<p>Nous vous invitons à consulter la nouvelle version de ce document et à fournir vos remarques à l'étudiant(e) si nécessaire. Votre rôle en tant que directeur(trice) de mémoire est crucial pour guider l'étudiant(e) à travers ce processus.</p>" +
                    "<p>Si vous avez des questions ou si vous souhaitez des précisions, n'hésitez pas à nous contacter.</p>" +
                    "<p>Respectueusement,<br>L'administration DEFITECH</p>" +
                    "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                    "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de mise à jour à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de mise à jour.", e);
        }
    }
    public void sendDocumentUpdateEtudiant(String toEmail, Document document) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Confirmation de la mise à jour de votre document");

            String htmlMsg = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                    "<h2 style='color: #4CAF50; text-align: center;'>Mise à jour de votre document</h2>" +
                    "<p>Cher(e) <strong>" + document.getEtudiant().getPrenom() + " " + document.getEtudiant().getNom() + "</strong>,</p>" +
                    "<p>Nous vous confirmons que votre document intitulé <strong>« " + document.getTheme() + " »</strong> a été mis à jour avec succès.</p>" +
                    "<p>Votre directeur(trice) de mémoire, <strong>Madame/Monsieur " + document.getProfesseur().getNom() + " " + document.getProfesseur().getPrenom() + "</strong>, a été informé(e) de cette mise à jour.</p>" +
                    "<p>Nous vous souhaitons bonne continuation dans la réalisation de votre projet. N'hésitez pas à contacter votre professeur pour toute question ou clarification.</p>" +
                    "<p>Bien cordialement,<br>L'administration DEFITECH</p>" +
                    "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                    "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de mise à jour à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de mise à jour.", e);
        }
    }
    public void sendFirstVersionProf(String toEmail, Document document) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Notification du téléversement de la première version du document");

            String htmlMsg = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                    "<h2 style='color: #4CAF50; text-align: center;'>Première version du document</h2>" +
                    "<p>Madame/Monsieur <strong>" + document.getProfesseur().getNom() + " " + document.getProfesseur().getPrenom() + "</strong>,</p>" +
                    "<p>Nous vous informons que l'étudiant(e) <strong>" + document.getEtudiant().getNom() + " " + document.getEtudiant().getPrenom() + "</strong> a téléversé la première version de son document intitulé <strong>« " + document.getTheme() + " »</strong> dans la catégorie <strong>" + document.getTypeDocument() + "</strong>.</p>" +
                    "<p>Nous vous invitons à examiner cette version et à fournir vos remarques et retours à l’étudiant(e) afin de l’aider à améliorer son travail.</p>" +
                    "<p>Si vous avez des questions ou des préoccupations, n'hésitez pas à nous contacter.</p>" +
                    "<p>Respectueusement,<br>L'administration DEFITECH</p>" +
                    "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                    "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de téléversement à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de téléversement.", e);
        }
    }
    public void sendFirstVersionEtudiant(String toEmail, Document document) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Confirmation du téléversement de la première version de votre document");

            String htmlMsg = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                    "<h2 style='color: #4CAF50;'>Première version téléversée</h2>" +
                    "<p>Cher(e) " + document.getEtudiant().getPrenom() + " " + document.getEtudiant().getNom() + ",</p>" +
                    "<p>Nous vous confirmons que  votre document intitulé <strong>« " + document.getTheme() + " »</strong> a été téléversée avec succès dans la catégorie <strong>" + document.getTypeDocument() + "</strong>.</p>" +
                    "<p>Un email a été envoyé à votre directeur(trice) de mémoire, <strong>Madame/Monsieur " + document.getProfesseur().getNom() + " " + document.getProfesseur().getPrenom() + "</strong>, pour l'informer du dépôt de ce document.</p>" +
                    "<p>Nous vous souhaitons bonne chance pour la suite de votre travail. N'hésitez pas à contacter votre professeur pour toute question ou remarque concernant cette version.</p>" +
                    "<p>Bien cordialement,<br>L'administration DEFITECH</p>" +
                    "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                    "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de téléversement à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de téléversement.", e);
        }
    }
    public void sendThemeChangeProf(String toEmail, Document document, String nouveauTheme) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Changement de thème de l'étudiant");

            String htmlMsg = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                    "<h2 style='color: #4CAF50;'>Changement de thème</h2>" +
                    "<p>Madame/Monsieur " + document.getProfesseur().getNom() + " " + document.getProfesseur().getPrenom() + ",</p>" +
                    "<p>L'étudiant(e) <strong>" + document.getEtudiant().getNom() + " " + document.getEtudiant().getPrenom() + "</strong> a décidé de changer de thème pour son document initialement intitulé <strong>« " + document.getTheme() + " »</strong>.</p>" +
                    "<p>Le nouveau thème est désormais <strong>« " + nouveauTheme + " »</strong>.</p>" +
                    "<p>Nous vous invitons à prendre connaissance de ce changement et à ajuster vos conseils et retours en conséquence.</p>" +
                    "<p>Si vous avez des questions ou des préoccupations concernant ce changement, n'hésitez pas à nous contacter.</p>" +
                    "<p>Respectueusement,<br>L'administration DEFITECH</p>" +
                    "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                    "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de changement de thème à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de changement de thème.", e);
        }
    }


}
