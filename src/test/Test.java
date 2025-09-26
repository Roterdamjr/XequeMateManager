package test;

import java.util.List;

import dao.AcaoDAO;
import model.Acao;



import dao.AcaoDAO;

public class Test {
    
    public static void main(String[] args) {
        AcaoDAO acaoDAO = new AcaoDAO();

        String ativo = "PETR4";
        String dataCompra = "24/09/2025";
        String quantidade = "100.0";
        String preco = "35.50";

        acaoDAO.comprarAcao(ativo, dataCompra, quantidade, preco);

        System.out.println("Ação " + ativo + " inserida com sucesso na TB_ACAO.");
    }
}
