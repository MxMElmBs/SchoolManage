package com.defitech.GestUni.service.Azhar;

import com.defitech.GestUni.dto.Azhar.NotificationDTO;
import com.defitech.GestUni.models.Azhar.Notification;
import com.defitech.GestUni.models.Bases.Utilisateur;
import com.defitech.GestUni.repository.Azhar.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationsServices {

    @Autowired
    private NotificationRepository notificationRepository;

    private static final Logger logger = LoggerFactory.getLogger(NotificationsServices.class);


    @Autowired
    private JavaMailSender mailSender;

    // Méthode pour créer une notification pour le directeur d'études

    public void sendNotification(Utilisateur utilisateur, String message) {
        Notification notification = new Notification();
        notification.setUtilisateur(utilisateur);
        notification.setMessage(message);
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);

//        // Envoi d'un e-mail si l'utilisateur a un e-mail enregistré
//        if (utilisateur.getEmail() != null && !utilisateur.getEmail().isEmpty()) {
//            sendEmail(utilisateur.getEmail(), "Notification", message);
//        }
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getNotificationId());
        dto.setMessage(notification.getMessage());
        dto.setSendTo(notification.getSendTo());
        dto.setSentAt(notification.getSentAt());
        dto.setRead(notification.is_read());
        return dto;
    }
    public Optional<Notification> markNotificationAsRead(Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            notification.set_read(true);
            notificationRepository.save(notification);
        }
        return notificationOptional;
    }
    public void markAllNotificationsAsRead(Long utilisaturId) {
        List<Notification> notifications = notificationRepository.findByUtilisateur_IdUser(utilisaturId);
        for (Notification notification : notifications) {
            notification.set_read(true);
        }
        notificationRepository.saveAll(notifications);
    }

}
