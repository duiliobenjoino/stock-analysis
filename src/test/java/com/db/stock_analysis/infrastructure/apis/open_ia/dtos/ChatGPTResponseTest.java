package com.db.stock_analysis.infrastructure.apis.open_ia.dtos;

import com.db.stock_analysis.domain.models.Engine;
import com.db.stock_analysis.helpers.ChatGPTResponseMock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ChatGPTResponseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testToRecommendation() {

        final var response = ChatGPTResponseMock.builder()
                .saleCall("12")
                .salePut("8")
                .resume("Com base na cotação atual abaixo do preço médio de compra...")
                .build()
                .mock();

        final var recommendation = response.toRecommendation(objectMapper);

        assertEquals(Engine.CHAT_GPT, recommendation.getEngine());
        assertEquals(2, recommendation.getPublicRecommendations().size());
        assertEquals("12", recommendation.getSaleCall());
        assertEquals("8", recommendation.getSalePut());
        assertEquals("Com base na cotação atual abaixo do preço médio de compra...", recommendation.getResume());
    }



}