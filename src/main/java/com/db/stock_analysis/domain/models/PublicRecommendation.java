package com.db.stock_analysis.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PublicRecommendation {
    private String source;
    private LocalDate date;
    private String recommendation;
    private String priceTarget;
}
