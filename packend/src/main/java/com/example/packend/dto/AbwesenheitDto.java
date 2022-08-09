package com.example.packend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbwesenheitDto {
    Long id;
    LocalDate startDatum;
    LocalDate endDatum;
    String mitarbeiterId;
}
