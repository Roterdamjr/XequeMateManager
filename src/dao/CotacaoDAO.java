package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

// Importe o modelo de Cotação, se você tiver um.
// Se não tiver, o Map<String, Double> é suficiente para o método de inserção.
// import model.Cotacao; 

public class CotacaoDAO {

    /**
     * Limpa toda a tabela TB_COTACAO e insere as novas cotações.
     * Esta é a operação de sincronização massiva.
     *
     * @param novasCotacoes Um Mapa com (Ativo -> Cotação) para inserção.
     */
	public void limparEInserir(Map<String, Double> novasCotacoes) {
        // Query para limpar a tabela
        final String SQL_TRUNCATE = "DELETE FROM TB_COTACAO";
        // Query para inserir
        final String SQL_INSERT = "INSERT INTO TB_COTACAO (ATIVO, COTACAO) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.connect()) {
            
            // 1. Desativa o auto-commit para garantir atomicidade (transação)
            conn.setAutoCommit(false);

            // 2. Limpa a tabela
            try (PreparedStatement stmtTruncate = conn.prepareStatement(SQL_TRUNCATE)) {
                stmtTruncate.executeUpdate();
            }

            // 3. Insere os novos dados (usando Batch para performance)
            try (PreparedStatement stmtInsert = conn.prepareStatement(SQL_INSERT)) {
                int count = 0;
                
                for (Map.Entry<String, Double> entry : novasCotacoes.entrySet()) {
                    stmtInsert.setString(1, entry.getKey());
                    stmtInsert.setDouble(2, entry.getValue());
                    
                    stmtInsert.addBatch();
                    
                    if (++count % 1000 == 0) {
                        stmtInsert.executeBatch();
                    }
                }
                
                stmtInsert.executeBatch(); // Executa o batch final
            }

            conn.commit();

        } catch (SQLException e) {
            System.out.println("Erro durante a sincronização de cotações: " + e.getMessage());
            // Nota: Em um ambiente real, você faria um rollback explícito aqui.
        }
    }

    /**
     * Busca a cotação mais recente de um ativo específico no banco de dados.
     * * @param ativo O código do ativo a ser buscado (ex: "BBAS3").
     * @return O valor da cotação como Double, ou null se o ativo não for encontrado.
     */
    public Double buscarCotacaoPorAtivo(String ativo) {
        final String SQL_SELECT = "SELECT COTACAO FROM TB_COTACAO WHERE ATIVO = ?";
        
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT)) {

            stmt.setString(1, ativo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retorna o valor da coluna COTACAO
                    return rs.getDouble("COTACAO");
                } else {
                    return null; // Ativo não encontrado
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar cotação para o ativo " + ativo + ": " + e.getMessage());
            return null;
        }
    }
}