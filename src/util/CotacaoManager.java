package util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dao.CotacaoDAO;

public class CotacaoManager {
    
    // ID da planilha e URL de exportação permanecem os mesmos
    //private static final String SPREADSHEET_ID = "1c998yZg4TG7iVzfYniLOV7hLrisBqp5M-BLhTLoltW4";
    private static final String SPREADSHEET_ID = "14n2_HjLUWNdYTiuytOI35UuGQlDS-jiq5N88aVQdzQk";
    
    
    private static final String CSV_EXPORT_URL = "https://docs.google.com/spreadsheets/d/" + 
                                                   SPREADSHEET_ID + 
                                                   "/export?format=csv";

    /**
     * Lê o CSV da URL e retorna um Mapa (Ativo -> Cotação).
     * @return Um Map onde a chave é o código do Ativo e o valor é a Cotação (Double).
     */
    public static void atualizarCotacoesNoDatabase() {
        CotacaoManager reader = new CotacaoManager();
        Map<String, Double> cotacoes = reader.lerCotacoesPorAtivo();

        System.out.println("Total de Ativos lidos: " + cotacoes.size());

        CotacaoDAO dao = new CotacaoDAO();

        try {
            System.out.println("Iniciando a sincronização dos dados com o banco de dados...");
            dao.limparEInserir(cotacoes);
            
            System.out.println("Sincronização concluída! Os dados foram atualizados no TB_COTACAO.");
        } catch (Exception e) {
            System.err.println("ERRO: Falha ao inserir dados no banco de dados."+e.getMessage() );
            e.printStackTrace();
        }
    }
    
    private Map<String, Double> lerCotacoesPorAtivo() {
    	
        Map<String, Double> cotacoesPorAtivo = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new URL(CSV_EXPORT_URL).openStream()))) {

            String linha = reader.readLine(); 
            
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue; // Pula linhas vazias
                }

                // 3. Dividir a linha por vírgula. 
                // A REGEX complexa lida com o campo Cotacao_orig que está entre aspas e contém vírgula.
                // Colunas: [0] Ativo, [1] "Cotacao_orig", [2] Cotacao (Double)
                String[] colunas = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                // Garantir que a linha tenha as colunas necessárias
                if (colunas.length >= 3) {
                    try {
                        String ativo = colunas[0].trim();
                        String cotacaoStr = colunas[2].trim();
                        Double cotacao = Double.parseDouble(cotacaoStr);
                        
                        cotacoesPorAtivo.put(ativo, cotacao);                
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter cotação para número na linha: " + linha);
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace();  }

        return cotacoesPorAtivo;
    }


}