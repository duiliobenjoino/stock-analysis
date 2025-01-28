package com.db.stockAnalysis.infrastructure.resources;

import com.db.stockAnalysis.domain.dtos.StockRequest;
import com.db.stockAnalysis.domain.models.Recommendation;
import com.db.stockAnalysis.domain.services.StockAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/stock/analysis")
public class StockAnalysisController {

    @Autowired
    private StockAnalysisService stockAnalysisService;

    @GetMapping
    public Recommendation analysis(@RequestParam String ticker, @RequestParam BigDecimal avgPrice) {
        return stockAnalysisService.execute(new StockRequest(ticker, avgPrice));
    }
}
