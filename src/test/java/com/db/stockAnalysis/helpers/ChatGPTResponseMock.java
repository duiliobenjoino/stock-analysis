package com.db.stockAnalysis.helpers;

import com.db.stockAnalysis.infrastructure.apis.openIA.dtos.ChatGPTResponse;
import lombok.Builder;

import java.util.List;

@Builder
public class ChatGPTResponseMock {

    private String saleCall;
    private String salePut;
    private String resume;

    public ChatGPTResponse mock() {
        var content = """
                ```json
                {
                    "publicRecommendations": [
                        {
                            "source": "Fonte A",
                            "date": "2025-01-01",
                            "recommendation": "Compra",
                            "priceTarget": "R$ 14,50"
                        },
                        {
                            "source": "Fonte B ",
                            "date": "2024-12-10",
                            "recommendation": "Compra",
                            "priceTarget": "R$ 15,00"
                        }
                    ],
                    "saleCall": "#saleCall",
                    "salePut": "#salePut",
                    "resume": "#resume"
                }
                ```
               """.replace("#saleCall", saleCall)
                  .replace("#salePut", salePut)
                  .replace("#resume", resume);

        final var response = new ChatGPTResponse();
        final var choice = new ChatGPTResponse.Choice();
        final var message = new ChatGPTResponse.Message("user", content);
        choice.setMessage(message);
        response.setChoices(List.of(choice));
        return response;
    }
}
