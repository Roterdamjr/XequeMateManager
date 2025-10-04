package test;

import java.util.List;

import dao.AcaoDAO;
import dao.CotacaoDAO;
import dao.DividendoDAO;
import dao.OpcaoDAO;

import model.Acao;
import model.Dividendo;
import model.Opcao;
import model.Operacao;
import model.OperacaoDividendo;
import util.Relatorio;
import util.ValidatorUtils;

public class Teste {

	 public static void main(String[] args) {

		Acao acao = new Acao(1);
		
   		Acao ac = new AcaoDAO().obterAcaoPorId(acao.getId());
	    List<Opcao> opcoes =  new OpcaoDAO().obterOpcoesPorIdAcao(acao.getId());
	    Operacao op = new OperacaoDividendo(ac,opcoes) ;
	    
        List<Dividendo> dividendos = new DividendoDAO().buscarPorAcao(acao.getId());
        
        
	}
	


	
    
    
}
