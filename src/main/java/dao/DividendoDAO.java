package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Acao;
import model.Dividendo;

public class DividendoDAO {

    public void inserir( Dividendo dividendo) {
        String sql = "INSERT INTO tb_dividendo (id_acao, valor) VALUES ( ?, ?)";
        try (Connection conn = DatabaseManager.connect();
        	 PreparedStatement stmt = conn.prepareStatement(sql)) {
            
        	stmt.setInt(1, dividendo.getIdAcao());
            stmt.setDouble(2, dividendo.getValor());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Dividendo> buscarPorAcao(int idAcao) {
        List<Dividendo> dividendos = new ArrayList<>();
        String sql = "SELECT * FROM tb_dividendo WHERE id_acao = ?";
        try (Connection conn = DatabaseManager.connect();
        	PreparedStatement stmt = conn.prepareStatement(sql)) {
            
        	stmt.setInt(1, idAcao);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Dividendo d = new Dividendo(
                    rs.getInt("id_acao"),
                    rs.getDouble("valor")
                );
                dividendos.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dividendos;
    }

    // Listar todos
    public List<Dividendo> buscarTodos() {
        List<Dividendo> dividendos = new ArrayList<>();
        String sql = "SELECT * FROM tb_dividendo";
        
        try (Connection conn = DatabaseManager.connect();
        	Statement stmt = conn.createStatement();
        		
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Dividendo d = new Dividendo(
                    rs.getInt("id_acao"),
                    rs.getDouble("valor")
                );
                dividendos.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dividendos;
    }
}

