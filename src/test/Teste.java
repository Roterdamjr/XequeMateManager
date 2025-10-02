package test;

import java.util.List;

import dao.AcaoDAO;
import dao.CotacaoDAO;
import dao.DividendoDAO;
import dao.OpcaoDAO;
import dao.OperacaoDAO;
import model.Acao;
import model.Dividendo;
import model.Opcao;
import model.Operacao;
import util.Relatorio;
import util.ValidatorUtils;

public class Teste {

	 public static void main(String[] args) {
		 //Relatorio.exibirResumoOperacoesNaoVendidas();

/*
			List<Acao> acoesNaVendidas = new AcaoDAO().obterAcoesNaoVendidas();
			for (Acao acao : acoesNaVendidas) {
				Operacao op = new OperacaoDAO().buscaOperacao(acao.getId());
				System.out.println(acao);
			}
			*/
	}
	
	 public static void testarInsercaoDividendo() {
	        DividendoDAO dao = new DividendoDAO();
	        
	        // 1. Crie uma instância de Dividendo
	        // IMPORTANTE: idAcao DEVE existir na tabela de Ações. Use um ID válido.
	        int idAcaoTeste = 1; // SUBSTITUA POR UM ID DE AÇÃO VÁLIDO NO SEU BANCO!
	        double valorDividendo = 0.55;
	        
	        Dividendo novoDividendo = new Dividendo(idAcaoTeste, valorDividendo);
	        
	        System.out.println("Tentando inserir o dividendo: " + novoDividendo.toString());
	        
	        try {
	            // 2. Chame o método de inserção
	            dao.inserir(novoDividendo);
	            System.out.println("SUCESSO: Dividendo inserido com sucesso para a Ação ID: " + idAcaoTeste);
	            
	            // Opcional: Verifique a lista (se o método listarPorAcao estiver funcionando)
	            List<Dividendo> dividendosDaAcao = dao.buscarPorAcao(idAcaoTeste);
	            System.out.println("Dividendos atuais para a Ação ID " + idAcaoTeste + ": " + dividendosDaAcao.size());
	            
	        } catch (Exception e) {
	            System.err.println("FALHA: Erro ao inserir dividendo.");
	            e.printStackTrace();
	        }
	    }

	
    
    
}
