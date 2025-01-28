package com.db.stockAnalysis.helpers;

import com.db.stockAnalysis.infrastructure.apis.brapi.dtos.StockDetailResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

public class StockDetailResponseMockBuilder {

    public static StockDetailResponse.HistoricalDataPrice mockHistoricalDataPrice(final LocalDate localDate, final BigDecimal price) {
        final var timestamp = localDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toEpochSecond();
        return new StockDetailResponse.HistoricalDataPrice(timestamp, price);
    }
}
