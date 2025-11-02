package cotacoes;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.AcaoDAO;
import dao.CotacaoDAO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class BrapiClient {

    private static final String BRAPI_TOKEN = "pHLWGjXiKZqHXtXuge7cwR"; 
    private static final String BASE_URL = "https://brapi.dev/api/quote/";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final long DELAY_MS = 500; 

    public static void main(String[] args) {
    	System.out.println("inciaindo");
        atualizarTodasCotacoes();
        System.out.println("Fim do procedimento");
    }
    
    public static void atualizarTodasCotacoes() {
        List<String> ativos = new AcaoDAO().obterNomeDeAcoesAbertas();
        Map<String, Double> cotacoesParaInserir = new TreeMap<>();
        
        if (ativos == null || ativos.isEmpty()) {
            return;
        }

        for (String ativo : ativos) {
            processaUmAtivo(ativo, cotacoesParaInserir);
  
            try {
                Thread.sleep(DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        if (!cotacoesParaInserir.isEmpty()) {
            new CotacaoDAO().limparEInserir(cotacoesParaInserir);
        }
    }

    private static void processaUmAtivo(String ticket, Map<String, Double> cotacoesParaInserir) {
        String url = BASE_URL + ticket + "?token=" + BRAPI_TOKEN;
        
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = CLIENT.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return; 
                }

                String jsonResponse = response.body().string();
                parseJson(jsonResponse, cotacoesParaInserir);
            }
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (Exception e) {
        }
    }

    private static void parseJson(String jsonResponse, Map<String, Double> cotacoesParaInserir) throws IOException {
        BrapiQuoteResponse response = MAPPER.readValue(jsonResponse, BrapiQuoteResponse.class);

        if (response.getResults() != null && !response.getResults().isEmpty()) {
            QuoteData cotacao = response.getResults().get(0); 
            
            String ativo = cotacao.getSymbol();
            Double preco = cotacao.getRegularMarketPrice();

            if (ativo != null && preco != null) {
                cotacoesParaInserir.put(ativo, preco);
            }
        }
    }
}