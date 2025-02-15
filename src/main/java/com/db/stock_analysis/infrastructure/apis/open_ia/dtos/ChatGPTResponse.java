package com.db.stock_analysis.infrastructure.apis.open_ia.dtos;

import com.db.stock_analysis.domain.exceptions.CustomErrorException;
import com.db.stock_analysis.domain.models.Engine;
import com.db.stock_analysis.domain.models.Recommendation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class ChatGPTResponse {

    private List<Choice> choices;

    public Recommendation toRecommendation(final ObjectMapper objectMapper){
        try {
            final var output = this.choices.stream().findFirst().orElseThrow(() -> new RuntimeException("Recommendation not found"));
            final var content = output.message.content.replace("```", "").replace("json","");
            final var recommendation = objectMapper.readValue(content, Recommendation.class);
            recommendation.setEngine(Engine.CHAT_GPT);
            return recommendation;
        } catch (JsonProcessingException ex) {
            throw CustomErrorException.of("[ChatGPT]: Error deserializing return", 500, ex);
        }
    }

    @Data
    public static class Choice {
        private Message message;
    }

    @AllArgsConstructor
    @Data
    public static class Message {
        private String role;
        private String content;
    }
}

