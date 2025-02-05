package com.db.stock_analysis.domain.services;

import com.db.stock_analysis.domain.models.Recommendation;

public interface RecommendationSearchEngine {
    Recommendation execute(String prompt);
}
