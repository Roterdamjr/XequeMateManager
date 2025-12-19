package util;

import java.util.List;

import dao.CotacaoDAO;
import dao.DividendoDAO;
import dao.OpcaoDAO;
import model.Acao;
import model.Dividendo;
import model.Opcao;
import model.Operacao;
import model.ResultadoOperacao;

public abstract class OperacaoAnalyticsBase {
	
	protected abstract double calcularPrecoVenda(Acao acao,
			boolean isOperacaoAberta);
	
	public ResultadoOperacao sumarizaResultado(Operacao operacao, boolean isOperacaoAberta) {
		 
		Acao acao = operacao.getAcao();
		

		
		List<Opcao> opcoes = operacao.getOpcoes();
	    List<Dividendo> dividendos = new DividendoDAO().buscarPorAcao(acao.getId());
	    
	    Double resultadoDasOpcoes = 0.0;
	    if (opcoes != null && !opcoes.isEmpty()) {
	        for (Opcao opcao : opcoes) {
	            resultadoDasOpcoes +=  opcao.getPrecoVenda() - opcao.getPrecoCompra();
	        }
	    }
	    
	    Double resultadoDosDividendos = 0.0;
	    if (dividendos != null && !dividendos.isEmpty()) {
	        for (Dividendo dividendo : dividendos) {
	        	resultadoDosDividendos +=  dividendo.getValor();
	        }
	    }
	    
	    double cotacao = new CotacaoDAO().buscarCotacaoPorAtivo(acao.getAtivo());
	    
		int quantidade = acao.getQuantidade();
		double strike = OpcaoDAO.obterStrikeUltimaOpcaoVendida(acao.getId());
		
		double precoMedio = acao.getPrecoCompra()  - resultadoDasOpcoes - resultadoDosDividendos;

		/*
		 * utiliza método concreto
		 */

        double precoVendaCalculo = calcularPrecoVenda(acao,isOperacaoAberta);
        /*         */
        
		Double resultado = quantidade * (precoVendaCalculo - precoMedio);
		
		return new ResultadoOperacao(
				acao.getAtivo(),
				quantidade,
				acao.getPrecoCompra(),
				acao.getPrecoVenda(),
				precoMedio,
				strike,
				cotacao,
				resultado,
				resultadoDasOpcoes,
				resultadoDosDividendos
		);
	}
}
