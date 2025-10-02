package util;

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

public class Relatorio {
	
	public static void exibirResumoOperacoesNaoVendidas() {
		List<Acao> acoesNaVendidas = new AcaoDAO().obterAcoesNaoVendidas();
		for (Acao acao : acoesNaVendidas) {
			Operacao op = new OperacaoDAO().buscaOperacao(acao.getId());
			exibirResumoDaOperacao(op);
		}
	}
	
	public static void exibirResumoDaOperacao(Operacao operacao) {
		
		Acao acao = operacao.getAcao();
		List<Opcao> opcoes= operacao.getOpcoes();
		List<Dividendo> dividendos = new DividendoDAO().buscarPorAcao(acao.getId());
		
		/*************************
		 * 		RESULTADO
		 *************************/
		Double resultadoDasOpcoes = 0.0;
	    if (opcoes != null && !opcoes.isEmpty()) {
	        for (Opcao opcao : opcoes) {
	        	resultadoDasOpcoes +=  opcao.getPrecoVenda() - opcao.getPrecoCompra() ;
	        }
	    }
	    
	    Double resultadoDosDividendos= 0.0;
	    if (dividendos != null && !dividendos.isEmpty()) {
		    for (Dividendo dividendo : dividendos) {
		    	resultadoDosDividendos += dividendo.getValor();
		    }
	    }
	    
		/*************************
		 * 		AÇÃO
		 *************************/
        Double precoMedio =  acao.getPrecoCompra() - resultadoDasOpcoes - resultadoDosDividendos;
        Double cotacao = new CotacaoDAO().buscarCotacaoPorAtivo(acao.getAtivo());
        int quantidade = acao.getQuantidade();
        Double strike = OpcaoDAO.obterStrikeUltimaOpcaoVendida(acao.getId());
        Double venda = cotacao > strike ? strike:cotacao;
        Double resultado = quantidade *(venda - precoMedio);
        Double patrimonio = quantidade * cotacao;

        System.out.println( acao.getAtivo()+ 
        		"	 Qtde: " + quantidade + 
        		"	 Compra: " + ValidatorUtils.formatarParaDuasDecimais(acao.getPrecoCompra())+ 
        		"	 Strike: " + ValidatorUtils.formatarParaDuasDecimais(strike) +
        		"	 PM: " + ValidatorUtils.formatarParaDuasDecimais(precoMedio) +
        		"	 Cotação "+cotacao + 
        		"	 Resultado: "+ ValidatorUtils.formatarParaDuasDecimais(resultado) +
        		"	 Patrimonio "+ ValidatorUtils.formatarParaDuasDecimais(patrimonio)
        );
       
		/*************************
		 * 		OPÇÃO
		 *************************/
	    if (opcoes != null && !opcoes.isEmpty()) {
	        for (Opcao opcao : opcoes) {
	        	System.out.println(opcao.getOpcao() + 
	            		", Compra: " + ValidatorUtils.formatarParaDuasDecimais(opcao.getPrecoCompra())+ 
	            		", Venda: " + ValidatorUtils.formatarParaDuasDecimais(opcao.getPrecoVenda()) + 
	            		", Strike: " + ValidatorUtils.formatarParaDuasDecimais(opcao.getStrike() )
	            );
	        }
	    }
	    
		/*************************
		 * 		DIVIDENDOS
		 *************************/
	    for (Dividendo dividendo : dividendos) {
	    	System.out.println(dividendo);
	    }
	    
	    System.out.println("-------------------------------------------------------------------");
	}
}
