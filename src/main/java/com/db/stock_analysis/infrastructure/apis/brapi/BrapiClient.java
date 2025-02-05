package com.db.stock_analysis.infrastructure.apis.brapi;

import com.db.stock_analysis.infrastructure.apis.brapi.dtos.StockDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "brapiClient", url = "${brapi.api.base-url}")
public interface BrapiClient {

    @GetMapping("/quote/{tickers}")
    StockDetailResponse getDetails(
            @PathVariable String tickers, @RequestParam String range,
            @RequestParam String interval, @RequestParam String token);

    default StockDetailResponse getDetails(final String tickers, final String token) {
        return getDetails(tickers, "3mo", "1d", token);
    }
}
