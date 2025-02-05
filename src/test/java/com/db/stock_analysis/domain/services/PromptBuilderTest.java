package com.db.stock_analysis.domain.services;

import com.db.stock_analysis.domain.models.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PromptBuilderTest {

    @Autowired
    private PromptBuilder promptBuilder;

    @Test
    void buildTest() {
        final var stock = Stock.builder()
                .ticker("TICKER")
                .currentPrice(BigDecimal.TEN)
                .avgPrice(BigDecimal.valueOf(9))
                .earningsPerShare(BigDecimal.TEN)
                .priceEarnings(BigDecimal.TEN)
                .minVariation(BigDecimal.valueOf(-1))
                .avgVariation(BigDecimal.valueOf(1))
                .maxVariation(BigDecimal.valueOf(2))
                .build();

        final var prompt = promptBuilder.build(stock);
        assertEquals(expectedPromptResult().trim(), prompt);
    }

    private String expectedPromptResult() {
        return """
                Você é um assistente de investimento que emite recomendações.
                Eu tenho ações de TICKER ao preço médio de R$ 9,00.
                Considere as seguintes informações sobre este ativo:
                    Preço atual: R$ 10,00
                    Preço / Lucro: 10
                    Preço / Lucro: 10
                Considere que a variação de preços mensal deste ativo nos últimos meses é:
                    Média: 1
                    Máxima: 2
                    Mínima: -1
                Busque as 5 principais recomendações públicas de compra/venda do ativo.
                Elabore uma resposta estritamente em Json utilizando o seguinte layout:
                {
                    publicRecommendations: [
                        {
                            source: [Dados sobre a fonte pesquisada],
                            date: [Data da recomendação],
                            recommendation: [Dados sobre a recomendação],
                            priceTarget: [Preço alvo para manutenção do ativo]
                        },
                        ...
                    ],
                    saleCall: [Sugestão de percentual afastamento do dinheiro de Venda de Call considerando as informações obtidas]]
                    salePut: [Sugestão de percentual afastamento do dinheiro de Venda de Call considerando as informações obtidas]],
                    resume: [Texto livre informando a ação que foi realizada e ponderações]
                }
                """;
    }
}