package com.defitech.GestUni.service.Azhar;

import com.defitech.GestUni.controller.EtudiantController;
import com.defitech.GestUni.models.Azhar.Permission;
import com.defitech.GestUni.models.Azhar.Presence;
import com.defitech.GestUni.models.Bases.Professeur;
import com.defitech.GestUni.models.Bases.UE;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service("azharEmailService")
public class azharEmailService {

    private static final Logger logger = LoggerFactory.getLogger(azharEmailService.class);
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.password}")
    private String emailPassword;

    private Session createEmailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });
    }

    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        if (toEmail == null || toEmail.trim().isEmpty()) {
            logger.error("L'adresse e-mail du destinataire est null ou vide, l'e-mail ne peut pas être envoyé.");
            throw new IllegalArgumentException("L'adresse e-mail du destinataire est null ou vide.");
        }

        Session session = createEmailSession();

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(subject);

        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(body, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);

        message.setContent(multipart);

        Transport.send(message);
        logger.info("Email sent to {}", toEmail);
    }

    public void sendWelcomeEmail(String toEmail, String username) {
        String subject = "Bienvenue chez Citizen";
        String body = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                "<div style='text-align: center; margin-bottom: 20px;'>" +
                "<img src='https://example.com/logo.png' alt='Company Logo' style='max-width: 150px;' />" +
                "</div>" +
                "<h2 style='color: #4CAF50;'>Bienvenue, " + username + " !</h2>" +
                "<p style='color: #2196F3;'>Nous sommes ravis de vous accueillir dans notre communauté.</p>" +
                "<p>Pour commencer, nous vous encourageons à explorer votre compte et à découvrir toutes les fonctionnalités disponibles.</p>" +
                "<p style='color: #D32F2F;'>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                "<p>Cordialement,<br> L'équipe Citizen</p>" +
                "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                "<p>&copy; 2024 Citizen. Tous droits réservés.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        try {
            sendEmail(toEmail, subject, body);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de bienvenue à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de bienvenue.", e);
        }
    }

    public void sendDemandeReceptionEmail(String toEmail, Permission permission) {
        String subject = "Réception de Votre Demande de permission";
        String body = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                "<h2 style='color: #2196F3;'>Réception de votre demande !</h2>" +
                "<p>Bonjour <strong>" + permission.getEtudiant().getNom() + " " + permission.getEtudiant().getPrenom() + "</strong>,</p>" +
                "<p>Nous avons bien reçu votre demande de permission pour la raison suivante :</p>" +
                "<p style='color: #D32F2F;'><strong>Raison :</strong> " + permission.getRaison() + "</p>" +
                "<p>Votre demande sera étudiée dans les plus brefs délais. Nous vous tiendrons informé(e).</p>" +
                "<p>Cordialement,<br> L'administration DEFITECH</p>" +
                "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        try {
            sendEmail(toEmail, subject, body);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de réception de demande à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de réception de demande.", e);
        }
    }

    public void sendDemandeAppouverEmail(String toEmail, Permission permission) {
        String subject = "Approbation de votre permission";
        String body = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                "<h2 style='color: #4CAF50;'>Approbation de votre permission !</h2>" +
                "<p>Bonjour,</p>" +
                "<p>Nous avons le plaisir de vous informer que votre demande de permission du <strong>" + permission.getDateDemande() + "</strong> a été approuvée.</p>" +
                "<p style='color: #D32F2F;'>Raison de la permission : <strong>" + permission.getRaison() + "</strong></p>" +
                "<p>Nous vous remercions pour votre patience.</p>" +
                "<p>Cordialement,<br> L'administration DEFITECH</p>" +
                "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        try {
            sendEmail(toEmail, subject, body);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email d'approbation de permission à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email d'approbation de permission.", e);
        }
    }

    public void sendDemandeRejetEmail(String toEmail, Permission permission) {
        String subject = "Rejet de demande de permission";
        String body = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                "<h2 style='color: #FFA500;'>Rejet de votre demande de permission</h2>" +
                "<p>Bonjour,</p>" +
                "<p>Nous sommes au regret de vous informer que votre demande de permission du <strong>" + permission.getDateDemande() + "</strong> a été rejetée pour la raison suivante :</p>" +
                "<p style='color: #D32F2F;'>Raison : <strong>" + permission.getRaisonR() + "</strong></p>" +
                "<p>Si aucune raison n'est fournie, vous pouvez contacter le directeur d'études de votre parcours.</p>" +
                "<p>Cordialement,<br> L'administration DEFITECH</p>" +
                "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        try {
            sendEmail(toEmail, subject, body);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de rejet de permission à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de rejet de permission.", e);
        }
    }
    public void sendRetardNotification(UE ue) {
        String toEmail = ue.getProfesseur().getUtilisateur().getEmail();
        String subject = "Retard sur le planning de votre cours";
        String body = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                "<h2 style='color: #FFA500;'>Retard sur le planning de votre cours</h2>" +
                "<p>Bonjour M/Mme <strong>" + ue.getProfesseur().getNom() + " " + ue.getProfesseur().getPrenom() + "</strong>,</p>" +
                "<p>Nous vous informons que votre cours de <strong>" + ue.getLibelle() + "</strong> est en retard par rapport au planning prévu.</p>" +
                "<p style='color: #D32F2F;'>Nous vous invitons à prendre les mesures nécessaires pour rattraper ce retard.</p>" +
                "<p>Cordialement,<br> L'administration DEFITECH</p>" +
                "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        try {
            sendEmail(toEmail, subject, body);
        } catch (MessagingException e) {
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de retard de cours.", e);
        }
    }
    public void sendCoursRetardEmail(String toEmail, Professeur professeur, String libelleCours) {
        String subject = "Cours en retard sur le planning";
        String body = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                "<h2 style='color: #FFA500;'>Retard sur le planning de votre cours</h2>" +
                "<p>Bonjour Professeur <strong>" + professeur.getNom() + " " + professeur.getPrenom() + "</strong>,</p>" +
                "<p>Nous vous informons que votre cours de <strong>" + libelleCours + "</strong> est en retard par rapport au planning prévu.</p>" +
                "<p style='color: #D32F2F;'>Nous vous invitons à prendre les mesures nécessaires pour rattraper ce retard.</p>" +
                "<p>Cordialement,<br> L'administration DEFITECH</p>" +
                "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        try {
            sendEmail(toEmail, subject, body);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de retard de cours à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de retard de cours.", e);
        }
    }

    public void sendTropAbsencesEtudiantEmail(String toEmail, String etudiantNom, int nombreAbsences) {
        String subject = "Avis d'Absences Répétées";
        String body = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                "<h2 style='color: #D32F2F;'>Avis d'Absences Répétées</h2>" +
                "<p>Bonjour <strong>" + etudiantNom + "</strong>,</p>" +
                "<p>Nous tenons à vous informer que nous avons constaté <strong>" + nombreAbsences + " absences</strong> lors de vos séances de cours.</p>" +
                "<p style='color: #2196F3;'>La présence en classe est un facteur clé de réussite académique.</p>" +
                "<p>Nous vous encourageons vivement à régulariser votre situation et à faire preuve d'assiduité.</p>" +
                "<p>Cordialement,<br> L'administration DEFITECH</p>" +
                "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        try {
            sendEmail(toEmail, subject, body);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email d'absence répétée.", e);
        }
    }

    public void sendAbsencesEnfantEmail(String toEmail, String tuteurNom, String enfantNom, int nombreAbsences, List<Presence> absences) {
        String subject = "Notification d'Absences Répétées de Votre Enfant";
        StringBuilder historiqueTable = new StringBuilder();
        historiqueTable.append("<table style='border-collapse: collapse; width: 100%; border: 1px solid #dddddd;'>")
                .append("<thead><tr style='background-color: #f2f2f2;'><th style='border: 1px solid #dddddd; padding: 8px;'>Date</th><th style='border: 1px solid #dddddd; padding: 8px;'>Motif</th></tr></thead><tbody>");

        for (Presence absence : absences) {
            historiqueTable.append("<tr><td style='border: 1px solid #dddddd; padding: 8px;'>")
                    .append(absence.getSeance().getDate())
                    .append("</td><td style='border: 1px solid #dddddd; padding: 8px;'>Absence injustifiée</td></tr>");
        }

        historiqueTable.append("</tbody></table>");

        String body = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                "<h2 style='color: #D32F2F;'>Avis d'Absences Répétées de Votre Enfant</h2>" +
                "<p>Bonjour <strong>" + tuteurNom + "</strong>,</p>" +
                "<p>Nous souhaitons vous informer que votre enfant, <strong>" + enfantNom + "</strong>, a été absent à <strong>" + nombreAbsences + " reprises</strong> lors des séances de cours.</p>" +
                "<p>Veuillez trouver ci-dessous un récapitulatif de ses absences :</p>" +
                historiqueTable.toString() +
                "<p style='color: #2196F3;'>Nous vous encourageons à motiver votre enfant à assister régulièrement aux cours.</p>" +
                "<p>Cordialement,<br> L'administration DEFITECH</p>" +
                "<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #777;'>" +
                "<p>&copy; 2024 DEFITECH. Tous droits réservés.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        try {
            sendEmail(toEmail, subject, body);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email au tuteur {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email au tuteur.", e);
        }
    }
}
