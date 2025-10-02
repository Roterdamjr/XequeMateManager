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
		 List<String> linhas =Relatorio.gerarResumoOperacoesFechadas();
		 for(String l:linhas) {
			 System.out.println(l);
		 }
		 
/*
			List<Acao> acoesNaVendidas = new AcaoDAO().obterAcoesNaoVendidas();
			for (Acao acao : acoesNaVendidas) {
				Operacao op = new OperacaoDAO().buscaOperacao(acao.getId());
				System.out.println(acao);
			}
			*/
	}
	


	
    
    
}
