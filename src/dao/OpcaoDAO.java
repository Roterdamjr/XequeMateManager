package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Acao;
import model.Opcao;

// Assumindo que você tem uma tabela TB_OPCAO com campos semelhantes a TB_ACAO
public class OpcaoDAO {

    /**
     * Obtém opções que foram 'vendidas' (criadas) mas ainda não 'compradas' pelo usuário.
     * O campo data_compra IS NULL indica que a opção está disponível para compra.
     */
    public List<Opcao> obterOpcoesNaoCompradas() {

/*
 * SELECT
  T1.id,
  T1.opcao,
  T1.data_venda,
  T1.quantidade,
  T1.preco_venda,
  T1.strike,
  T2.ativo AS ativo_acao
FROM TB_OPCAO AS T1
JOIN TB_ACAO AS T2
  ON T1.id_acao = T2.id
WHERE
  T2.data_venda IS  NULL 
  and tipo_operacao='DIV'
GROUP BY
  T2.ativo
HAVING
  SUBSTR(T1.data_venda, 7, 4) || '-' || SUBSTR(T1.data_venda, 4, 2) || '-' || SUBSTR(T1.data_venda, 1, 2) =
  MAX(SUBSTR(T1.data_venda, 7, 4) || '-' || SUBSTR(T1.data_venda, 4, 2) || '-' || SUBSTR(T1.data_venda, 1, 2))
ORDER BY
  T2.ativo;
 */
    	
        String sql = "SELECT\r\n"
        		+ "  T1.id,\r\n"
        		+ "  T1.opcao,\r\n"
        		+ "  T1.data_venda,\r\n"
        		+ "  T1.quantidade,\r\n"
        		+ "  T1.preco_venda,\r\n"
        		+ "  T1.strike,\r\n"
        		+ "  T2.ativo AS ativo_acao\r\n"
        		+ "FROM TB_OPCAO AS T1\r\n"
        		+ "JOIN TB_ACAO AS T2\r\n"
        		+ "  ON T1.id_acao = T2.id\r\n"
        		+ "WHERE\r\n"
        		+ "  T2.data_venda IS  NULL \r\n"
        		+ "  and tipo_operacao='DIV'\r\n"
        		+ "GROUP BY\r\n"
        		+ "  T2.ativo\r\n"
        		+ "HAVING\r\n"
        		+ "  SUBSTR(T1.data_venda, 7, 4) || '-' || SUBSTR(T1.data_venda, 4, 2) || '-' || SUBSTR(T1.data_venda, 1, 2) =\r\n"
        		+ "  MAX(SUBSTR(T1.data_venda, 7, 4) || '-' || SUBSTR(T1.data_venda, 4, 2) || '-' || SUBSTR(T1.data_venda, 1, 2))\r\n"
        		+ "ORDER BY\r\n"
        		+ "  T2.ativo;";
        List<Opcao> opcoes = new ArrayList<>();

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Opcao opcao = new Opcao();
                opcao.setId(rs.getInt("id"));
                opcao.setOpcao(rs.getString("opcao"));
                opcao.setQuantidade(rs.getInt("quantidade"));
                opcao.setPrecoVenda(rs.getDouble("preco_venda"));
                opcao.setStrike(rs.getDouble("strike"));
                opcoes.add(opcao);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter opções não compradas: " + e.getMessage());
        }
        return opcoes;
    }

    
    public List<Opcao> obterOpcoesPorIdAcao(int idAcao) {
    	
        String sql = "SELECT opcao, quantidade, data_compra, data_venda, preco_compra, preco_venda,strike "
        		+ "FROM TB_OPCAO WHERE id_Acao = ?";
        
        List<Opcao> opcoes = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.connect();
        	PreparedStatement stmt = conn.prepareStatement(sql);
        	) {
        	stmt.setInt(1, idAcao);
      	
            try (ResultSet rs = stmt.executeQuery()) {
            	while (rs.next()) {
                	Opcao opcao = new Opcao();
                    opcao.setOpcao(rs.getString("opcao"));
                    opcao.setDataCompra(rs.getString("data_compra"));
                    opcao.setDataVenda(rs.getString("data_venda"));
                    opcao.setQuantidade(rs.getInt("quantidade"));
                    opcao.setPrecoCompra(rs.getDouble("preco_compra"));
                    opcao.setPrecoVenda(rs.getDouble("preco_venda"));
                    opcao.setStrike(rs.getDouble("strike"));
                    opcoes.add(opcao);
                }
            }
            
       } catch (SQLException e) {
           System.out.println(e.getMessage());
       }
        
        return opcoes;
    }
    
    /**
     * Registra a compra de uma opção existente.
     */
    public void comprarOpcao(int id, String dataCompra, double precoCompra) {
        String sql = "UPDATE TB_OPCAO SET data_compra = ?, preco_compra = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dataCompra);
            pstmt.setDouble(2, precoCompra);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao comprar opção: " + e.getMessage());
        }
    }
    public void venderOpcao(int idAcao,  String dataVenda,String opcao, String quantidade,
    						String strike,double precoVenda) {
    	
    	String sql = "INSERT INTO TB_OPCAO (id_acao, data_venda, opcao, quantidade, "
    			+ "strike, preco_venda) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Tratamento de tipos para o banco de dados
            double quantidadeDouble = Double.parseDouble(quantidade.replace(",", "."));
            double strikeDouble = Double.parseDouble(strike.replace(",", "."));
            
            pstmt.setInt(1, idAcao);
            pstmt.setString(2, dataVenda);
            pstmt.setString(3, opcao);
            pstmt.setDouble(4, quantidadeDouble);
            pstmt.setDouble(5, strikeDouble);
            pstmt.setDouble(6, precoVenda); // Preço de Venda da Opção (Prêmio)

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao vender/inserir opção: " + e.getMessage());
        }
    }
    

     /**
      * Retorna o valor de 'strike' da última opção vendida pertencente a uma Ação específica.
      * @param idAcao O ID da Ação para filtrar a busca.
      * @return O valor do strike da última opção vendida para aquela Ação, ou 0.0 se não encontrar.
      */
     public static double obterStrikeUltimaOpcaoVendida(int idAcao) {
         // Ordena por ID em ordem decrescente e filtra pelo id_acao
         String sql = "SELECT strike FROM TB_OPCAO "
         		+ "WHERE data_venda IS NOT NULL AND id_acao = ? "
         		+ "ORDER BY id DESC LIMIT 1";
         
         double strike = 0.0;

         try (Connection conn = DatabaseManager.connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) { // Usa PreparedStatement

             pstmt.setInt(1, idAcao); // Define o parâmetro idAcao
             
             try (ResultSet rs = pstmt.executeQuery()) {
                 if (rs.next()) {
                     strike = rs.getDouble("strike");
                 }
             }
         } catch (SQLException e) {
             System.out.println("Erro ao obter o strike da última opção vendida por idAcao: " + e.getMessage());
         }
         return strike;
     }

 

 
    
}