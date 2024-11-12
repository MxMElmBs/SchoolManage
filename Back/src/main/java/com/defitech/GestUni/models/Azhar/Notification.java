package com.defitech.GestUni.models.Azhar;

import com.defitech.GestUni.models.Bases.Utilisateur;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(name = "message", nullable = false)
    private String message;
    private String SendTo;
    private LocalDateTime sentAt;

    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @Column(name = "is_read", nullable = false)
    private boolean is_read;

    public Notification() {
        this.timestamp = System.currentTimeMillis();
        this.is_read = false;
    }

}
