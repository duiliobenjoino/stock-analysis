package com.db.stockAnalysis.infrastructure.apis.brapi.dtos;

import com.db.stockAnalysis.domain.exceptions.CustomErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.db.stockAnalysis.helpers.StockDetailResponseMockBuilder.mockHistoricalDataPrice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StockDetailResponseTest {

    @Test
    void shouldToConvertPriceByDayToPriceByMonth() {

        final var result = new StockDetailResponse.Result();
        final var dateBase = LocalDate.of(2025, 1, 25);
        result.setHistoricalDataPrice(
                List.of(
                    mockHistoricalDataPrice(dateBase, BigDecimal.TEN),
                    mockHistoricalDataPrice(dateBase.minusDays(1), BigDecimal.valueOf(9)),
                    mockHistoricalDataPrice(dateBase.minusDays(2), BigDecimal.valueOf(9)),
                    mockHistoricalDataPrice(dateBase.minusDays(3), BigDecimal.valueOf(9)),
                    mockHistoricalDataPrice(dateBase.minusMonths(1), BigDecimal.TEN),
                    mockHistoricalDataPrice(dateBase.minusDays(33), BigDecimal.valueOf(9)),
                    mockHistoricalDataPrice(dateBase.minusDays(34), BigDecimal.valueOf(9)),
                    mockHistoricalDataPrice(dateBase.minusDays(35), BigDecimal.valueOf(9)),
                    mockHistoricalDataPrice(dateBase.minusMonths(2), BigDecimal.TEN),
                    mockHistoricalDataPrice(dateBase.minusDays(63), BigDecimal.valueOf(9)),
                    mockHistoricalDataPrice(dateBase.minusDays(64), BigDecimal.valueOf(9)),
                    mockHistoricalDataPrice(dateBase.minusDays(65), BigDecimal.valueOf(9))
                )
        );

        final var pricesByMonth = new StockDetailResponse(List.of(result)).getPricesByMonth();
        final var allValuesAreTen = pricesByMonth.values().stream().anyMatch(value -> value.compareTo(BigDecimal.TEN) == 0);

        assertEquals(3, pricesByMonth.size());
        assertTrue(allValuesAreTen);
    }

    @ParameterizedTest
    @MethodSource("provideStockDetailResponseParam")
    void shouldThrowExceptionWhenReturnIsEmpty(StockDetailResponse stockDetailResponse) {
        final var customErrorException = assertThrows(CustomErrorException.class, () -> {
            stockDetailResponse.getCurrentPrice();
            stockDetailResponse.getEarningsPerShare();
            stockDetailResponse.getPriceEarnings();
            stockDetailResponse.getPricesByMonth();
        });

        assertEquals(StockDetailResponse.EMPTY_ERROR_MESSAGE, customErrorException.getMessage());
        assertThrows(CustomErrorException.class, stockDetailResponse::getPricesByMonth);

    }

    private static Stream<Arguments> provideStockDetailResponseParam() {

        return Stream.of(
                Arguments.of(new StockDetailResponse()),
                Arguments.of(new StockDetailResponse(Collections.emptyList())),
                Arguments.of(new StockDetailResponse(List.of(new StockDetailResponse.Result(BigDecimal.TEN, null, BigDecimal.TEN, null)))),
                Arguments.of(new StockDetailResponse(List.of(new StockDetailResponse.Result(BigDecimal.TEN, BigDecimal.TEN, null, null)))),
                Arguments.of(new StockDetailResponse(List.of(new StockDetailResponse.Result(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, Collections.emptyList()))))
        );
    }
}
