package com.db.stock_analysis.infrastructure.resources.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiError {
    private String message;
    private LocalDate timeStamp;
    private int status;
}
