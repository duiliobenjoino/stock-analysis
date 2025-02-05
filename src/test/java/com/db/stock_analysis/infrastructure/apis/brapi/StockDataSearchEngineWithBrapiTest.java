package com.db.stock_analysis.infrastructure.apis.brapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

class StockDataSearchEngineWithBrapiTest {

    @Mock
    private BrapiClient brapiClient;

    @Test
    void testGetHistoryPriceVariation() {
        final var prices = new TreeMap<>(Map.of(
                LocalDate.now(), BigDecimal.valueOf(12.50),
                LocalDate.now().minusMonths(1), BigDecimal.valueOf(11.50),
                LocalDate.now().minusMonths(2), BigDecimal.valueOf(12),
                LocalDate.now().minusMonths(3), BigDecimal.valueOf(10)
        ));

        final var historyPriceVariation = new StockDataSearchEngineWithBrapi(brapiClient).getHistoryPriceVariation(prices);

        Assertions.assertEquals(0, BigDecimal.valueOf(2).compareTo(historyPriceVariation.getFirst()));
        Assertions.assertEquals(0, BigDecimal.valueOf(-0.5).compareTo(historyPriceVariation.get(1)));
        Assertions.assertEquals(0, BigDecimal.valueOf(1).compareTo(historyPriceVariation.get(2)));
    }
}
