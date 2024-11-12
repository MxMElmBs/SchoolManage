package com.defitech.GestUni.dto.Azhar;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateSeanceRequest {
    private Long coursId;
    private LocalDate date;
    private LocalTime startTime;
}
