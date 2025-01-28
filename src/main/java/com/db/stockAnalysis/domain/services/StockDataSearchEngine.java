package com.db.stockAnalysis.domain.services;

import com.db.stockAnalysis.domain.dtos.StockRequest;
import com.db.stockAnalysis.domain.models.Stock;

public interface StockDataSearchEngine {
    Stock execute(StockRequest stockRequest);
}
