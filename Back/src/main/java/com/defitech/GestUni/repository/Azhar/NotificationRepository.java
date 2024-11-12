package com.defitech.GestUni.repository.Azhar;

import com.defitech.GestUni.models.Azhar.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUtilisateur_IdUser(Long utilisateurId);}
