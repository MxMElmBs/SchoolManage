package com.defitech.GestUni.service.Max;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class AdminMailServices {

    private static final Logger logger = LoggerFactory.getLogger(AdminMailServices.class);

    private final JavaMailSender mailSender;

    // Inject username from properties
    @Value("${spring.mail.username}")
    private String fromEmail;

    // Constructor to inject JavaMailSender
    public AdminMailServices(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendCredentials(String toEmail, String username, String password) {
        try {
            // Create the MimeMessage
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Set email details
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Compte d'accès à l'Intranet Defitech");

            String htmlMsg = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;'>" +
                    "<div style='text-align: center; margin-bottom: 20px;'>" +
                    "<img src='https://defitech.tg/files/logos/13102020-100413_72364111_971610626506670_2157636662924935168_o.jpg' alt='Company Logo' style='max-width: 150px;' />" +
                    "</div>" +
                    "<h2 style='color: #4CAF50;'>Bienvenue ! Voici votre identifiant et mot de passe de connexion :</h2>" +
                    "<h2 style='color: #4CAF50;'>Votre username est : " + username + " </h2>" +
                    "<h2 style='color: #4CAF50;'>Votre mot de passe est : " + password + " </h2>" +
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

            // Send the email
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de bienvenue à {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Erreur lors de l'envoi de l'email de bienvenue.", e);
        }
    }


// Méthode principale pour envoyer un email avec les identifiants
    /*public void sendCredentialsEmail(String toEmail, String username, String password) throws MessagingException, javax.mail.MessagingException {
        String subject = "Création de Compte Utilisateur";
        String body = "Bonjour Mo.nsieur/Madame,\n\n" +
                "Votre compte en tant que membre de l'institut " +
                "polytechnique de DEFITECH a bien été créé. \n\n" +
                "Voici vos identifiants : \n\n" +
                "Username: " + username + "\n" +
                "Password: " + password + "\n\n" +
                "Merci de ne pas partager vos identifiants.\n\n" +
                "Institut Polytechnique DEFITECH";

        // Appel à la méthode d'envoi d'e-mail
        emailJ.sendEmail(toEmail, subject, body, null);
    }*/

    // Méthode d'envoi d'e-mail (texte brut, sans HTML)
   /* public void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true pour permettre les pièces jointes

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body); // Texte brut, pas de HTML

        // Utilisation de l'email expéditeur configuré dans properties
        helper.setFrom(fromEmail);

        // Envoi du message
        mailSender.send(message);
    }*/

}
