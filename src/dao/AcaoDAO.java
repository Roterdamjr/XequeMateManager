package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Acao;
import model.Opcao;

public class AcaoDAO {

    public void comprarAcao(String ativo, String dataCompra, String quantidade, String preco, String tipoOperacao) {
        String sql = "INSERT INTO TB_ACAO (ativo, data_compra, quantidade, preco_compra,tipo_operacao)"
        		+ " VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ativo);
            pstmt.setString(2, dataCompra);
            pstmt.setDouble(3, Double.parseDouble(quantidade.replace(",", ".")));
            pstmt.setDouble(4, Double.parseDouble(preco.replace(",", ".")));
            pstmt.setString(5, tipoOperacao);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void venderAcao(int id, double precoVenda, String dataVenda) {
        String sql = "UPDATE TB_ACAO SET preco_venda = ?, data_venda = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, precoVenda);
            pstmt.setString(2, dataVenda);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Acao> obterAcoesAbertas() {
        String sql = "SELECT id, ativo, quantidade, preco_compra, preco_venda,data_compra,data_venda "
        		+ "FROM TB_ACAO WHERE data_venda IS NULL ORDER BY ativo";
        
        List<Acao> acoes = new ArrayList<>();

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Acao acao = new Acao();
                acao.setId(rs.getInt("id"));
                acao.setAtivo(rs.getString("ativo"));
                acao.setQuantidade(rs.getInt("quantidade"));
                acao.setPrecoCompra(rs.getDouble("preco_compra"));
                acao.setPrecoVenda(rs.getDouble("preco_venda"));
                acao.setDataCompra(rs.getString("data_compra"));
                acao.setDataVenda(rs.getString("data_venda"));
                acoes.add(acao);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return acoes;
    }
    
    public List<Acao> obterAcoesFechadas() {
        String sql = "SELECT id, ativo, quantidade, preco_compra, preco_venda,data_compra,data_venda "
        		+ "FROM TB_ACAO WHERE data_venda IS NOT NULL ORDER BY ativo";
        
        List<Acao> acoes = new ArrayList<>();

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Acao acao = new Acao();
                acao.setId(rs.getInt("id"));
                acao.setAtivo(rs.getString("ativo"));
                acao.setQuantidade(rs.getInt("quantidade"));
                acao.setPrecoCompra(rs.getDouble("preco_compra"));
                acao.setPrecoVenda(rs.getDouble("preco_venda"));
                acao.setDataCompra(rs.getString("data_compra"));
                acao.setDataVenda(rs.getString("data_venda"));
                acoes.add(acao);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return acoes;
    }
    
    public Acao obterAcaoPorId(int idAcao) {
        Acao acao = null;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TB_ACAO WHERE id = ?")
            ) {

            stmt.setInt(1, idAcao);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    acao = new Acao();
                    acao.setId(rs.getInt("id"));
                    acao.setAtivo(rs.getString("ativo"));
                    acao.setQuantidade(rs.getInt("quantidade"));
                    acao.setPrecoCompra(rs.getDouble("preco_compra"));
                    acao.setPrecoVenda(rs.getDouble("preco_venda"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter ação: " + e.getMessage());
            e.printStackTrace();
        }

        return acao;
    }

    
}
