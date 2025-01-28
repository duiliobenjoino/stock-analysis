package com.db.stockAnalysis.domain.services;

import com.db.stockAnalysis.domain.dtos.StockRequest;
import com.db.stockAnalysis.domain.models.Recommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockAnalysisService {

    private final StockDataSearchEngine stockDataSearchEngine;
    private final RecommendationSearchEngine recommendationSearchEngine;
    private final PromptBuilder promptBuilder;

    public Recommendation execute(final StockRequest stockRequest) {
        final var stock = stockDataSearchEngine.execute(stockRequest);
        final var prompt = promptBuilder.build(stock);
        final var recommendation = recommendationSearchEngine.execute(prompt);
        recommendation.setStock(stock);
        return recommendation;
    }
}
