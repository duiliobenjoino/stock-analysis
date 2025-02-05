package com.db.stock_analysis.infrastructure.apis.brapi;

import com.db.stock_analysis.domain.dtos.StockRequest;
import com.db.stock_analysis.domain.exceptions.CustomErrorException;
import com.db.stock_analysis.domain.models.Stock;
import com.db.stock_analysis.domain.services.StockDataSearchEngine;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class StockDataSearchEngineWithBrapi implements StockDataSearchEngine {

    private final BrapiClient brapiClient;

    @Value("${brapi.api.key}")
    private String apiKey;

    @Override
    public Stock execute(final StockRequest stockRequest) {
        try{
            final var stockDetailResponse = brapiClient.getDetails(stockRequest.getTicker(), apiKey);

            final var historyPriceVariation = getHistoryPriceVariation(stockDetailResponse.getPricesByMonth());
            final var minVariation = historyPriceVariation.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            final var maxVariation = historyPriceVariation.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            final var avgVariation = BigDecimal.valueOf(historyPriceVariation.stream().mapToDouble(BigDecimal::doubleValue).average().orElse(0.0));

            return Stock.builder()
                    .ticker(stockRequest.getTicker())
                    .currentPrice(stockDetailResponse.getCurrentPrice())
                    .avgPrice(stockRequest.getAvgPrice())
                    .minVariation(minVariation)
                    .maxVariation(maxVariation)
                    .avgVariation(avgVariation)
                    .priceEarnings(stockDetailResponse.getPriceEarnings())
                    .earningsPerShare(stockDetailResponse.getEarningsPerShare())
                    .build();

        } catch (FeignException ex) {
            throw handleFeignException(ex);
        }
    }

    private CustomErrorException handleFeignException(final FeignException ex) {
        return switch (ex.status()) {
            case 400 -> CustomErrorException.of("[Brapi Integration] Invalid shipping data", 400, ex);
            case 401 -> CustomErrorException.of("[Brapi Integration] Invalid token", 401, ex);
            case 402 -> CustomErrorException.of("[Brapi Integration] Balance expired", 429, ex);
            case 404 -> CustomErrorException.of("[Brapi Integration] Stock not found", 404, ex);
            default -> CustomErrorException.of("[Brapi Integration] Internal server Error", 500, ex);
        };
    }

    List<BigDecimal> getHistoryPriceVariation(final SortedMap<LocalDate, BigDecimal> priceHistory) {
        final var prices = priceHistory.values().stream().toList();
        return IntStream.range(1, prices.size())
                .mapToObj(i -> {
                    var previous = prices.get(i - 1);
                    var current = prices.get(i);
                    return current.subtract(previous);
                }).toList();
    }

}
