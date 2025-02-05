package com.db.stock_analysis.infrastructure.apis.open_ia;

import com.db.stock_analysis.domain.exceptions.CustomErrorException;
import com.db.stock_analysis.domain.models.Recommendation;
import com.db.stock_analysis.domain.services.RecommendationSearchEngine;
import com.db.stock_analysis.infrastructure.apis.open_ia.dtos.ChatGPTRequest;
import com.db.stock_analysis.infrastructure.apis.open_ia.dtos.ChatGPTResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecommendationSearchEngineWithOpenIA implements RecommendationSearchEngine {

    private final ChatGPTClient chatGPTClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String apiKey;

    @Override
    public Recommendation execute(final String prompt) {
        final var request = ChatGPTRequest.create(prompt);
        final var response = sendToChatGpt(request);
        return response.toRecommendation(objectMapper);
    }

    private ChatGPTResponse sendToChatGpt(final ChatGPTRequest request) {
        try {
            return chatGPTClient.sendMessage(apiKey, request);
        } catch (FeignException ex) {
            throw switch (ex.status()) {
                case 401 -> CustomErrorException.of("[ChatGPT] Invalid token", ex.status(), ex);
                case 429 -> CustomErrorException.of("[ChatGPT] Balance expired", ex.status(),  ex);
                default -> CustomErrorException.of("[ChatGPT] Internal server Error", 500, ex);
            };
        }
    }

}
