package com.db.stock_analysis.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class StockRequest {
    private String ticker;
    private BigDecimal avgPrice;
}
