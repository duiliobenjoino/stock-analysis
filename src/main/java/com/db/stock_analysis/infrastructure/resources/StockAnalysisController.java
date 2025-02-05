package com.db.stock_analysis.infrastructure.resources;

import com.db.stock_analysis.domain.dtos.StockRequest;
import com.db.stock_analysis.domain.models.Recommendation;
import com.db.stock_analysis.domain.services.StockAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/stock/analysis")
@RequiredArgsConstructor
public class StockAnalysisController {

    private final StockAnalysisService stockAnalysisService;

    @GetMapping
    public Recommendation analysis(@RequestParam String ticker, @RequestParam BigDecimal avgPrice) {
        return stockAnalysisService.execute(new StockRequest(ticker, avgPrice));
    }
}
