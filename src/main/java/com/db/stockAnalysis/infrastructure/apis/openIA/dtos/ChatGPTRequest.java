package com.db.stockAnalysis.infrastructure.apis.openIA.dtos;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
public class ChatGPTRequest {
    private final String model;
    private final List<Message> messages;

    private ChatGPTRequest(final String model, final List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public static ChatGPTRequest create(final String content) {
        return new ChatGPTRequest("chatgpt-4o-latest", List.of(new ChatGPTRequest.Message("user", content)));
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Message {
        private final String role;
        private final String content;
    }
}
