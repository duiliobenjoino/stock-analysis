package com.db.stock_analysis.domain.services;

import com.db.stock_analysis.domain.exceptions.CustomErrorException;
import com.db.stock_analysis.domain.models.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class PromptBuilder {

    private final ResourceLoader resourceLoader;

    public String build(final Stock stock) {
        try {
            var resource = resourceLoader.getResource("classpath:static/prompt-base.txt");
            var promptBase = Files.readString(Path.of(resource.getURI()));
            return promptBase.replace("#stock", stock.getTicker())
                    .replace("#avgPricing", moneyFormatted(stock.getAvgPrice()))
                    .replace("#actualPricing", moneyFormatted(stock.getCurrentPrice()))
                    .replace("#priceEarnings", stock.getCurrentPrice().toString())
                    .replace("#earningsPerShare", stock.getCurrentPrice().toString())
                    .replace("#avgVariation", stock.getAvgVariation().toString())
                    .replace("#minVariation", stock.getMinVariation().toString())
                    .replace("#maxVariation", stock.getMaxVariation().toString());
        } catch (IOException ex) {
            throw CustomErrorException.of("ChatGPT: Error reading file with prompt", 500, ex);
        }
    }

    private String moneyFormatted(final BigDecimal value) {
        var symbols = new DecimalFormatSymbols(Locale.of("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        return new DecimalFormat("###,##0.00", symbols).format(value);
    }
}
