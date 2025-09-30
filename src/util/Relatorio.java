package util;

import java.util.List;

import dao.AcaoDAO;
import dao.CotacaoDAO;
import dao.OpcaoDAO;
import dao.OperacaoDAO;
import model.Acao;
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
		
		List<Opcao> opcoes= operacao.getOpcoes();
		
		Acao acao = operacao.getAcao();
		
		Double resultadoDasOpcoes = 0.0;
	    if (opcoes != null && !opcoes.isEmpty()) {
	        for (Opcao opcao : opcoes) {
	        	resultadoDasOpcoes +=  opcao.getPrecoVenda() - opcao.getPrecoCompra() ;
	        }
	    }
	    
        Double precoMedio =  acao.getPrecoCompra() - resultadoDasOpcoes ;
        Double cotacao = new CotacaoDAO().buscarCotacaoPorAtivo(acao.getAtivo());
        int quantidade = acao.getQuantidade();
        Double strike = OpcaoDAO.obterStrikeUltimaOpcaoVendida(acao.getId());
        Double venda = cotacao > strike ? strike:cotacao;
        Double resultado = quantidade *(venda - precoMedio);
        Double patrimonio = quantidade * cotacao;
        /*
         * Ação: BBDC4, Qtde: 500, Compra: 11.78, Strike: 16.25, PM: 13.58, Cotação 17.68 ,
         * Resultado: 1335.00, Patrimonio 8840.0
         */
        System.out.println( acao.getAtivo()+ 
        		"	 Qtde: " + quantidade + 
        		"	 Compra: " + ValidatorUtils.formatarParaDuasDecimais(acao.getPrecoCompra())+ 
        		"	 Strike: " + ValidatorUtils.formatarParaDuasDecimais(strike) +
        		"	 PM: " + ValidatorUtils.formatarParaDuasDecimais(precoMedio) +
        		"	 Cotação "+cotacao + 
        		"	 Resultado: "+ ValidatorUtils.formatarParaDuasDecimais(resultado) +
        		"	 Patrimonio "+ ValidatorUtils.formatarParaDuasDecimais(patrimonio)
        );
       
	    if (opcoes != null && !opcoes.isEmpty()) {
	        for (Opcao opcao : opcoes) {
	        	System.out.println(opcao.getOpcao() + 
	            		", Compra: " + ValidatorUtils.formatarParaDuasDecimais(opcao.getPrecoCompra())+ 
	            		", Venda: " + ValidatorUtils.formatarParaDuasDecimais(opcao.getPrecoVenda()) + 
	            		", Strike: " + ValidatorUtils.formatarParaDuasDecimais(opcao.getStrike() )
	            );
	        }
	    }
	    
	    System.out.println("-------------------------------------------------------------------");
	}
}
