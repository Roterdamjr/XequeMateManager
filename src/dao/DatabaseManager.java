package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	//trabalho
   // private static final String DB_PATH = "jdbc:sqlite:C:/workspace/XequeMateManager/db/XequeMate.db";
   private static final String DB_PATH = "jdbc:sqlite:C:/workspace/XequeMateManager/db/XequeMateDBTeste.db";
   
   //casa
 // private static final String DB_PATH = "jdbc:sqlite:C:\\Eclipse_worksapce\\XequeMateManager\\db\\XequeMate.db";
 // private static final String DB_PATH = "jdbc:sqlite:C:\\Eclipse_worksapce\\XequeMateManager\\db\\XequeMateDBTeste.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_PATH);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados: " + e.getMessage());
        }
        return conn;
    }
}