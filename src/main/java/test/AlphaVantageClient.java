package test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Arrays; // 💡 NOVO IMPORT

public class AlphaVantageClient {

    private static final String API_KEY = "D32SHHZ7R5PWNNCD"; 

    private static final String BASE_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=";

    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static void main(String[] args) {
        List<String> ativos = Arrays.asList("PETR4.SA", "VALE3.SA", "ITSA4.SA");

        System.out.println("Buscando cotações diárias para os ativos: " + ativos + "...");
        
        try {
            Map<String, Double> cotacoes = obterCotacoesAtivos(ativos);
            
            System.out.println("\n✅ Cotações Recebidas:");
            cotacoes.forEach((ticker, preco) -> 
                System.out.printf("  - %s: R$ %.2f%n", ticker, preco));
            
        } catch (IOException e) {
            System.err.println("❌ Ocorreu um erro de I/O ou HTTP: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Ocorreu um erro inesperado: " + e.getMessage());
        }
    }

    /**
     * Busca o preço de fechamento mais recente para uma lista de ativos.
     */
    public static Map<String, Double> obterCotacoesAtivos(List<String> tickers) throws Exception {
        Map<String, Double> resultados = new HashMap<>();

        for (String ticker : tickers) {
            System.out.println("-> Buscando dados para " + ticker + "...");
            
            Double preco = obterCotacaoRecente(ticker);
            if (preco != null) {
                resultados.put(ticker, preco);
            }
        }
        return resultados;
    }

    /**
     * Executa a chamada HTTP e extrai a cotação de fechamento mais recente para um único ativo.
     */
    private static Double obterCotacaoRecente(String ticker) throws IOException, Exception {
        String url = BASE_URL + ticker + "&apikey=" + API_KEY;
        
        Request request = new Request.Builder().url(url).build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na requisição para " + ticker + ": " + 
                			response.code() + " " + response.message());
            }

            String jsonResponse = response.body().string();
            return processarJsonCotacao(jsonResponse, ticker);
        }
    }

    /**
     * Processa a resposta JSON para extrair o preço de fechamento mais recente.
     */
    private static Double processarJsonCotacao(String jsonResponse, String ticker) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);

        if (rootNode.has("Error Message")) {
            System.err.println("❌ ERRO da API Alpha Vantage para " + ticker + ": " + rootNode.get("Error Message").asText());
            return null;
        }

        JsonNode timeSeriesNode = rootNode.path("Time Series (Daily)");

        if (timeSeriesNode.isMissingNode() || timeSeriesNode.isEmpty()) {
            System.err.println("⚠️ Dados de cotação para " + ticker + " não encontrados ou formato inesperado.");
            return null;
        }

        // Obtém a entrada mais recente (a primeira no Iterator)
        Iterator<Map.Entry<String, JsonNode>> fields = timeSeriesNode.fields();
        
        if (fields.hasNext()) {
            Map.Entry<String, JsonNode> latestEntry = fields.next();
            JsonNode latestQuote = latestEntry.getValue();
            
            String closePriceText = latestQuote.path("4. close").asText();
            
            if (closePriceText.isEmpty()) {
                 System.err.println("⚠️ Preço de fechamento para " + ticker + " não encontrado na entrada mais recente.");
                 return null;
            }

            try {
                Double closePrice = Double.parseDouble(closePriceText);
                System.out.printf("  (Último Fechamento: R$ %.2f)%n", closePrice);
                return closePrice;
            } catch (NumberFormatException e) {
                System.err.println("❌ Erro ao converter o preço de " + closePriceText + " para Double para " + ticker + ".");
                return null;
            }

        } else {
             System.err.println("⚠️ Nenhuma entrada de cotação encontrada na série temporal para " + ticker + ".");
             return null;
        }
    }
}