package com.db.stock_analysis.infrastructure.apis.open_ia;

import com.db.stock_analysis.infrastructure.apis.open_ia.dtos.ChatGPTRequest;
import com.db.stock_analysis.infrastructure.apis.open_ia.dtos.ChatGPTResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "chatGPTClient", url = "${openai.api.base-url}")
public interface ChatGPTClient {

    @PostMapping
    ChatGPTResponse sendMessage(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ChatGPTRequest request
    );
}
