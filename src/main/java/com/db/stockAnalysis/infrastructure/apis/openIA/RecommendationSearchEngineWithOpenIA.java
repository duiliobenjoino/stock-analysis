package com.db.stockAnalysis.infrastructure.apis.openIA;

import com.db.stockAnalysis.domain.exceptions.CustomErrorException;
import com.db.stockAnalysis.domain.models.Recommendation;
import com.db.stockAnalysis.domain.services.RecommendationSearchEngine;
import com.db.stockAnalysis.infrastructure.apis.openIA.dtos.ChatGPTRequest;
import com.db.stockAnalysis.infrastructure.apis.openIA.dtos.ChatGPTResponse;
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
    private final ResourceLoader resourceLoader;

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
