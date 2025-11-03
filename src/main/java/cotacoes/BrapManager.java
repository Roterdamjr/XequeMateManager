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


public class BrapManager {

    private static final String BRAPI_TOKEN = "pHLWGjXiKZqHXtXuge7cwR"; 
    private static final String BASE_URL = "https://brapi.dev/api/quote/";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final long DELAY_MS = 500; 

    /**
     * Atualiza todas as cotações.
     * @return null em caso de sucesso, ou uma String contendo a mensagem de erro em caso de falha.
     */
    public static String atualizarCotacoesNoDatabase() {
        System.out.println("Iniciando teste de atualização BrapiClient...");
        String resultado = atualizarTodasCotacoes();
        
        if (resultado == null) {
            return "✅ Atualização concluída com sucesso.";
        } else {
        	return "❌ Falha na atualização: " + resultado ;
        }   	
    }
    
    public static String atualizarTodasCotacoes() {
        List<String> ativos = new AcaoDAO().obterNomeDeAcoesAbertas();
        Map<String, Double> cotacoesParaInserir = new TreeMap<>();
        
        if (ativos == null || ativos.isEmpty()) {
            // Retorna um erro se não houver ativos para buscar
            return "Nenhuma ação aberta encontrada para buscar cotações.";
        }

        for (String ativo : ativos) {
            String erro = processaUmAtivo(ativo, cotacoesParaInserir);
 
            if (erro != null) {
                return erro;
            }
  
            try {
                Thread.sleep(DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Erro de interrupção: " + e.getMessage());
                return "Processo interrompido durante o atraso.";
            }
        }
        
        // Finaliza: Insere todas as cotações coletadas no banco de dados
        try {
            if (!cotacoesParaInserir.isEmpty()) {
                new CotacaoDAO().limparEInserir(cotacoesParaInserir);
            }
            return null; 
        } catch (Exception e) {
            System.err.println("Erro ao finalizar a inserção no banco de dados: " + e.getMessage());
            e.printStackTrace();
            return "Erro ao salvar cotações no banco de dados.";
        }
    }
    
    /**
     * Busca a cotação de um único ativo e a armazena no mapa.
     * @return String de erro em caso de falha, ou null em caso de sucesso.
     */
    private static String processaUmAtivo(String ticket, Map<String, Double> cotacoesParaInserir) {
        String url = BASE_URL + ticket + "?token=" + BRAPI_TOKEN;
        
        try {
            Request request = new Request.Builder().url(url).build();

            try (Response response = CLIENT.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String msgErro = "Falha HTTP (" + response.code() + ") ao buscar " + ticket + ".";
                    System.err.println(msgErro);
                    // O erro 429 deve ser capturado aqui
                    return msgErro; 
                }

                String jsonResponse = response.body().string();
                return parseJson(jsonResponse, cotacoesParaInserir);
            }
        } catch (IOException e) {
            String msgErro = "Erro de I/O ao conectar com a API: " + e.getMessage();
            System.err.println(msgErro);
            e.printStackTrace();
            return msgErro;
        } catch (Exception e) {
            String msgErro = "Erro inesperado ao processar o ativo " + ticket + ": " + e.getMessage();
            System.err.println(msgErro);
            e.printStackTrace();
            return msgErro;
        }
    }

    /**
     * Faz o parse do JSON e adiciona a cotação ao mapa.
     * @return String de erro em caso de falha no parse, ou null em caso de sucesso.
     */
    private static String parseJson(String jsonResponse, Map<String, Double> cotacoesParaInserir) {
        try {
            BrapiQuoteResponse response = MAPPER.readValue(jsonResponse, BrapiQuoteResponse.class);

            if (response.getResults() != null && !response.getResults().isEmpty()) {
                QuoteData cotacao = response.getResults().get(0); 
                
                String ativo = cotacao.getSymbol();
                Double preco = cotacao.getRegularMarketPrice();
                
                if (ativo != null && preco != null) {
                    cotacoesParaInserir.put(ativo, preco);
                    return null; // Sucesso
                } else {
                    return "Dados de cotação inválidos ou incompletos.";
                }
            } else {
                return "Cotação não encontrada na resposta da API.";
            }
        } catch (IOException e) {
            String msgErro = "Erro ao fazer parse da resposta JSON: " + e.getMessage();
            System.err.println(msgErro);
            e.printStackTrace();
            return msgErro;
        }
    }
    
    public static void main(String[] args) {
        // Exemplo de como um JFrame chamaria e trataria o retorno
        System.out.println("Iniciando teste de atualização BrapiClient...");
        String resultado = atualizarTodasCotacoes();
        
        if (resultado == null) {
            System.out.println("✅ Atualização concluída com sucesso.");
        } else {
            System.err.println("❌ Falha na atualização: " + resultado);
            // Em um JFrame, esta string seria exibida em um JOptionPane.showMessageDialog
        }
    }
}