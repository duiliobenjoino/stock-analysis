package com.db.stock_analysis.domain.services;

import com.db.stock_analysis.domain.dtos.StockRequest;
import com.db.stock_analysis.domain.models.Stock;

public interface StockDataSearchEngine {
    Stock execute(StockRequest stockRequest);
}
