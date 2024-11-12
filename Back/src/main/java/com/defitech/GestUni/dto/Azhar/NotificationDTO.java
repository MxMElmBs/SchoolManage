package com.defitech.GestUni.dto.Azhar;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private String sendTo;
    private LocalDateTime sentAt;
    private boolean read;
}