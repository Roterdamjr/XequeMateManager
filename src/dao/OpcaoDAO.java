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
        // Exemplo de SQL: Seleciona opções criadas (data_venda NOT NULL) e não compradas (data_compra IS NULL)
        String sql = "SELECT id, opcao, quantidade, preco_venda, strike FROM TB_OPCAO WHERE data_compra IS NULL ORDER BY opcao";
        List<Opcao> opcoes = new ArrayList<>();

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Opcao opcao = new Opcao();
                opcao.setId(rs.getInt("id"));
                opcao.setOpcao(rs.getString("opcao"));
                opcao.setQuantidade(rs.getDouble("quantidade"));
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
                    opcao.setQuantidade(rs.getDouble("quantidade"));
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
    
}