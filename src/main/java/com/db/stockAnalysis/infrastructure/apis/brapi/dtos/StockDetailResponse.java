package com.db.stockAnalysis.infrastructure.apis.brapi.dtos;

import com.db.stockAnalysis.domain.exceptions.CustomErrorException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import static java.util.Objects.isNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class StockDetailResponse {
    static final String EMPTY_ERROR_MESSAGE = "[BRAPI] Stock Detail not found or incomplete";
    private List<Result> results;

    public BigDecimal getCurrentPrice() {
        validateIfEmpty();
        return results.getFirst().getRegularMarketPrice();
    }

    public BigDecimal getPriceEarnings() {
        validateIfEmpty();
        return results.getFirst().getPriceEarnings();
    }

    public BigDecimal getEarningsPerShare() {
        validateIfEmpty();
        return results.getFirst().getPriceEarnings();
    }

    private void validateIfEmpty() {
        if(isNull(results) || results.isEmpty()) {
            throw CustomErrorException.of(EMPTY_ERROR_MESSAGE, 404);
        }
    }

    public TreeMap<LocalDate, BigDecimal> getPricesByMonth() {
        validateIfEmpty();
        final var pricesByPeriod = new TreeMap<LocalDate, BigDecimal>();
        if (isNull(results.getFirst().historicalDataPrice) || results.getFirst().historicalDataPrice.isEmpty()) {
            throw CustomErrorException.of(EMPTY_ERROR_MESSAGE, 404);
        }
        final var historicalDataPrices = results.getFirst().historicalDataPrice.stream()
                .sorted(Comparator.comparing(HistoricalDataPrice::getDate).reversed())
                .toList();
        final var lastPrice = results.getFirst().historicalDataPrice.getFirst();
        pricesByPeriod.put(lastPrice.getDate(), lastPrice.price);
        var monthCount = 1;
        for(HistoricalDataPrice historicalDataPrice : historicalDataPrices) {
            final var dateLoop = historicalDataPrice.getDate();
            final var dateTarget = lastPrice.getDate().minusMonths(monthCount);
            if(dateLoop.isEqual(dateTarget) || dateLoop.isBefore(dateTarget)) {
                pricesByPeriod.put(dateLoop, historicalDataPrice.price);
                monthCount++;
            }
        }
        return pricesByPeriod;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Result {
        private BigDecimal regularMarketPrice;
        private BigDecimal priceEarnings;
        private BigDecimal earningsPerShare;
        private List<HistoricalDataPrice> historicalDataPrice;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class HistoricalDataPrice {
        @JsonProperty("date")
        private long timestampDate;
        @JsonProperty("close")
        private BigDecimal price;

        LocalDate getDate() {
            return Instant.ofEpochSecond(timestampDate)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
    }

}
