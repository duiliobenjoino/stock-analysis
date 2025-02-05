package com.db.stock_analysis.domain.models;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class Stock {
    private String ticker;
    private BigDecimal currentPrice;
    private BigDecimal avgPrice;
    private BigDecimal minVariation;
    private BigDecimal maxVariation;
    private BigDecimal avgVariation;
    private BigDecimal priceEarnings;
    private BigDecimal earningsPerShare;
}
