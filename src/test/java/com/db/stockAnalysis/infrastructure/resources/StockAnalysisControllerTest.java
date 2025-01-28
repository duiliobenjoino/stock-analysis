package com.db.stockAnalysis.infrastructure.resources;

import com.db.stockAnalysis.domain.models.Engine;
import com.db.stockAnalysis.helpers.ChatGPTResponseMock;
import com.db.stockAnalysis.infrastructure.apis.brapi.BrapiClient;
import com.db.stockAnalysis.infrastructure.apis.brapi.dtos.StockDetailResponse;
import com.db.stockAnalysis.infrastructure.apis.openIA.ChatGPTClient;
import com.db.stockAnalysis.infrastructure.apis.openIA.dtos.ChatGPTResponse;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static com.db.stockAnalysis.helpers.StockDetailResponseMockBuilder.mockHistoricalDataPrice;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StockAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChatGPTClient chatGPTClient;

    @MockitoBean
    private BrapiClient brapiClient;

    @Test
    void analysisSuccessTest() throws Exception {
        when(brapiClient.getDetails(any(), any()))
                .thenReturn(mockStockDetailResponse());

        when(chatGPTClient.sendMessage(any(), any()))
                .thenReturn(mockChatGPTResponse());

        mockMvc.perform(get("/stock/analysis?ticker=ABEV3&avgPrice=9.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.engine").value(Engine.CHAT_GPT.name()))
                .andExpect(jsonPath("$.stock.ticker").value("ABEV3"))
                .andExpect(jsonPath("$.stock.currentPrice").value("10"))
                .andExpect(jsonPath("$.saleCall").value("11"))
                .andExpect(jsonPath("$.salePut").value("8"))
                .andExpect(jsonPath("$.resume").value("Resumo teste..."));
    }

    @Test
    void analysisWithoutStockInformationResponse() throws Exception {
        when(brapiClient.getDetails(any(), any()))
                .thenThrow(mockFeignException(404));

        mockMvc.perform(get("/stock/analysis?ticker=ITUB4&avgPrice=10.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message")
                        .value("[Brapi Integration] Stock not found"));
    }

    @Test
    void analysisWithoutIAResponse() throws Exception {
        when(brapiClient.getDetails(any(), any()))
                .thenReturn(mockStockDetailResponse());

        when(chatGPTClient.sendMessage(any(), any()))
                .thenThrow(mockFeignException(401));

        mockMvc.perform(get("/stock/analysis?ticker=ITUB4&avgPrice=10.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message")
                        .value("[ChatGPT] Invalid token"));
    }

    private FeignException mockFeignException(int status) {
        var request = Request.create(
                Request.HttpMethod.GET,
                "/test",
                new HashMap<>(),
                null,
                new RequestTemplate());

        return FeignException.errorStatus(
                "GET /test",
                Response.builder()
                        .status(status)
                        .request(request)
                        .body("{\"message\":\"Error\"}", StandardCharsets.UTF_8)
                        .build()
        );
    }

    private StockDetailResponse mockStockDetailResponse() {
        final var dateBase = LocalDate.now();
        return new StockDetailResponse(List.of(
                new StockDetailResponse.Result(
                        BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
                        List.of(
                                mockHistoricalDataPrice(dateBase, BigDecimal.TEN),
                                mockHistoricalDataPrice(dateBase.minusMonths(1), BigDecimal.valueOf(9)),
                                mockHistoricalDataPrice(dateBase.minusMonths(2), BigDecimal.valueOf(8))
                        )
                )
        ));
    }

    private ChatGPTResponse mockChatGPTResponse() {
        return ChatGPTResponseMock.builder()
                .saleCall("11")
                .salePut("8")
                .resume("Resumo teste...")
                .build()
                .mock();
    }
}