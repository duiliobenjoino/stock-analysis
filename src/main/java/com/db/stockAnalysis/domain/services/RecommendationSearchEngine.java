package com.db.stockAnalysis.domain.services;

import com.db.stockAnalysis.domain.models.Recommendation;

public interface RecommendationSearchEngine {
    Recommendation execute(String prompt);
}
