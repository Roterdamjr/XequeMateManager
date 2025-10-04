package test;

import java.util.List;
import java.util.Map;

import dao.AcaoDAO;
import dao.CotacaoDAO;
import dao.DividendoDAO;
import dao.OpcaoDAO;

import model.Acao;
import model.Dividendo;
import model.Opcao;
import model.Operacao;
import model.OperacaoDividendo;
import model.ResultadoOperacao;
import util.OperacaoAnalytics;
import util.Relatorio;
import util.Utils;

public class Teste {

	 public static void main(String[] args) {

	
   		Acao acao = new AcaoDAO().obterAcaoPorId( 2 );
	    List<Opcao> opcoes =  new OpcaoDAO().obterOpcoesPorIdAcao(acao.getId());
	    Operacao op =new Operacao(acao,opcoes) ; 
		
	    Double resultado = OperacaoAnalytics.sumarizaReeultado(op,false).getResultado();
	    String dtCompra = acao.getDataCompra();
	    String dtVenda=acao.getDataVenda();
	    
	    Map<String, Integer> mapa = Utils.contarDiasPorMes(dtCompra, dtVenda);

        System.out.println("Período: " + dtCompra + " a " + dtVenda);
        System.out.println("================================");
        
        // Imprime o resultado
        for (Map.Entry<String, Integer> entry : mapa.entrySet()) {
            System.out.printf("Mês %s: %d dias%n", entry.getKey(), entry.getValue());
        }
        
	}
	


	
    
    
}
