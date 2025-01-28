package com.db.stockAnalysis.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Recommendation {
    private Engine engine;
    private Stock stock;
    private List<PublicRecommendation> publicRecommendations;
    private String saleCall;
    private String salePut;
    private String resume;
}
