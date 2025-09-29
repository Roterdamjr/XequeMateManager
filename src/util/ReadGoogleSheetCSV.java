package util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dao.CotacaoDAO;

public class ReadGoogleSheetCSV {
    
    // ID da planilha e URL de exportação permanecem os mesmos
    private static final String SPREADSHEET_ID = "1c998yZg4TG7iVzfYniLOV7hLrisBqp5M-BLhTLoltW4";
    private static final String CSV_EXPORT_URL = "https://docs.google.com/spreadsheets/d/" + 
                                                   SPREADSHEET_ID + 
                                                   "/export?format=csv";

    /**
     * Lê o CSV da URL e retorna um Mapa (Ativo -> Cotação).
     *
     * @return Um Map onde a chave é o código do Ativo e o valor é a Cotação (Double).
     */
    public Map<String, Double> lerCotacoesPorAtivo() {
        // Usa HashMap para armazenar o par (Ativo, Cotação)
        Map<String, Double> cotacoesPorAtivo = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new URL(CSV_EXPORT_URL).openStream()))) {

            // 1. Ler e descartar o cabeçalho ("Ativo,Cotacao_orig,Cotacao")
            String linha = reader.readLine(); 
            
            // 2. Loop para ler as linhas de dados
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
                        // Coluna 0: Ativo (String)
                        String ativo = colunas[0].trim();
                        
                        // Coluna 2: Cotação (String no formato 23.36)
                        String cotacaoStr = colunas[2].trim();
                        
                        // Converter a string para Double
                        Double cotacao = Double.parseDouble(cotacaoStr);
                        
                        // 4. Adicionar ao mapa
                        cotacoesPorAtivo.put(ativo, cotacao); 
                        
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter cotação para número na linha: " + linha);
                        // Você pode optar por ignorar ou registrar o erro
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Lidar com exceções de conexão ou leitura
        }

        return cotacoesPorAtivo;
    }

    public static void main(String[] args) {
        ReadGoogleSheetCSV reader = new ReadGoogleSheetCSV();
        Map<String, Double> cotacoes = reader.lerCotacoesPorAtivo();

        System.out.println("Total de Ativos lidos: " + cotacoes.size());
        
        // 2. Instancia o DAO
        CotacaoDAO dao = new CotacaoDAO();

        try {
            // 3. Chama o método do DAO para limpar a tabela e inserir as novas cotações
            System.out.println("Iniciando a sincronização dos dados com o banco de dados...");
            dao.limparEInserir(cotacoes);
            
            System.out.println("Sincronização concluída! Os dados foram atualizados no TB_COTACAO.");

        } catch (Exception e) {
            System.err.println("ERRO: Falha ao inserir dados no banco de dados.");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}