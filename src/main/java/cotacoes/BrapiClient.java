package cotacoes;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.AcaoDAO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class BrapiClient {

    private static final String BRAPI_TOKEN = "pHLWGjXiKZqHXtXuge7cwR"; 
    private static final String BASE_URL = "https://brapi.dev/api/quote/";

    public static void main(String[] args) {
    	List<String> ativos =new AcaoDAO().obterNomeDeAcoesAbertas();
    	for(String ativo:ativos) {
    		processaUmAtivo(ativo);
    	}
    	
    }
    
    private static void processaUmAtivo(String  ticket) {
        System.out.println("Buscando cotação para " + ticket + " via brapi...");
        
        String url = BASE_URL + ticket + "?token=" + BRAPI_TOKEN;
        
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Erro na requisição: " + response.code() + " " + response.message());
                }

                String jsonResponse = response.body().string();
                processJson(jsonResponse);
            }
        } catch (IOException e) {
            System.err.println("❌ Ocorreu um erro de I/O ou HTTP: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void processJson(String jsonResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        // Mapeia o JSON para o objeto de resposta principal
        BrapiQuoteResponse response = mapper.readValue(jsonResponse, BrapiQuoteResponse.class);

        // 4. Extrai a cotação
        if (response.getResults() != null && !response.getResults().isEmpty()) {
            QuoteData cotacao = response.getResults().get(0); // Pega o primeiro (e único) resultado
            
            System.out.println("\n✅ Conexão BEM-SUCEDIDA e dados recebidos!");
          //  System.out.println("-".repeat(40));
            System.out.printf("  Ativo: %s (%s)%n", cotacao.getSymbol(), cotacao.getShortName());
            System.out.printf("  Preço Atual: R$ %.2f%n", cotacao.getRegularMarketPrice());
            System.out.printf("  Última Atualização: %s%n", cotacao.getRegularMarketTime());
        //    System.out.println("-".repeat(40));
            
        } else {
            System.err.println("\n⚠️ Não foi possível encontrar a cotação (o array 'results' está vazio).");
            System.err.println("Verifique o token ou o código do ativo.");
        }
    }
}
