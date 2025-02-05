# Análise de ativo da bolsa brasileira

Esse é o **Stock Analysis**, uma api que combina dados obtidos através da [BRAPI API](https://brapi.dev/) com a análise de linguagem natural fornecida pelo [ChatGPT](https://chatgpt.com/). A intenção deste projeto foi submeter à análise de IA, informações sobre um ativo e obter recomendações de compra ou venda via venda de opções.

A recomendação obtida **NÃO** deveria ser utilizada, uma vez que se trata apenas de um experimento.

---

## 📂 Estrutura do Projeto

```plaintext
.
├── src/                  # Código-fonte do projeto
│   ├── main/             # Código principal da aplicação
│   │   ├── java/         # Código em Java
│   │   │   └── com/
│   │   │       └── db/
│   │   │           └── stock_analysis/
│   │   │               ├── domain/        # Lógica de negócios
│   │   │               └── infrastructure/ # Integrações e camada de controller
│   │   └── resources/    # Arquivos de configuração e templates
│   └── test/             # Testes automatizados
├── pom.xml               # Arquivo de configuração do Maven
├── README.md             # Este arquivo
```
---

## 🛠️ Tecnologias Utilizadas

- **Java 21**: Linguagem principal para processamento e integração de dados.
- **BRAPI API**: Para obtenção de informações atualizadas sobre ações brasileiras.
- **ChatGPT**: Para análise e geração de insights financeiros baseados em linguagem natural.
- **Spring Boot**: Framework para simplificar o desenvolvimento de aplicações web.

---

## 📦 Instalação e Configuração

1. **Clone este repositório:**
   ```bash
   git clone https://github.com/duiliobenjoino/stock-analysis.git
   cd stock-analysis
   ```

2. **Configure as variáveis de ambiente:**
   
   Defina as variáveis de ambiente `OPENAI_TOKEN` e `BRAPI_TOKEN` no seu sistema operacional:
   ```bash
   export OPENAI_TOKEN=sua-chave-openai
   export BRAPI_TOKEN=sua-chave-brapi
   ```
   Alternativamente, você pode configurar estas variáveis diretamente no arquivo `application.yaml` em `src/main/resources/`:
   ```properties
   openai.api.key=${OPENAI_TOKEN}
   brapi.api.key=${BRAPI_TOKEN}

3. **Execute o sistema:**
   
   Certifique-se de que você possui o Maven e o Java 21 instalados e execute os comandos:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

---

## 📊 Exemplo de Uso

A aplicação permite consultar informações de uma ação e obter análises detalhadas. Por exemplo, usando o endpoint disponibilizado pelo `StockAnalysisController`:

### Requisição HTTP

**Endpoint:**
```
GET /stock/analysis
```

**Exemplo de chamada com o código da ação PETR4:**
```bash
curl --location 'localhost:8080/stock/analysis?ticker=PETR4&avgPrice=35.70'
```

### Resposta Esperada (JSON):
```json
{
   "engine": "CHAT_GPT",
   "stock": {
      "ticker": "PETR4",
      "currentPrice": 36.97,
      "avgPrice": 35.70,
      "minVariation": 0,
      "maxVariation": 0,
      "avgVariation": 0.0,
      "priceEarnings": 5.6742567608815095,
      "earningsPerShare": 5.6742567608815095
   },
   "publicRecommendations": [
      {
         "source": "XP Investimentos",
         "date": "2023-10-01",
         "recommendation": "Manutenção",
         "priceTarget": "40.00"
      },
      {
         "source": "Itaú BBA",
         "date": "2023-09-28",
         "recommendation": "Compra",
         "priceTarget": "42.00"
      },
      {
         "source": "Goldman Sachs",
         "date": "2023-09-22",
         "recommendation": "Venda",
         "priceTarget": "34.50"
      },
      {
         "source": "BTG Pactual",
         "date": "2023-09-15",
         "recommendation": "Neutro",
         "priceTarget": "36.00"
      },
      {
         "source": "Morgan Stanley",
         "date": "2023-09-10",
         "recommendation": "Compra",
         "priceTarget": "43.00"
      }
   ],
   "saleCall": "10",
   "salePut": "5",
   "resume": "Com base nas recomendações públicas consultadas, a decisão de manter o ativo PETR4 foi realizada por estar levemente acima do preço médio de compra (R$ 35,70 frente ao atual R$ 36,97), com expectativa mista no mercado. Recomenda-se a venda de opções de call com um afastamento de 10% do preço atual e opções de put com um afastamento de 5%, considerando o sentimento geral de manutenção."
}
```

---

## 🤝 Contribuição

Contribuições são sempre bem-vindas! Sinta-se à vontade para:

1. Abrir issues com sugestões ou problemas encontrados.
2. Submeter pull requests com melhorias ou correções.
3. Compartilhar ideias para novas funcionalidades.

