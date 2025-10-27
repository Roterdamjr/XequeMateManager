package util;

import dao.CotacaoDAO;
import dao.OpcaoDAO;
import model.Acao;

public class OperacaoAnalyticsTresPraUm extends OperacaoAnalyticsBase{

   protected double calcularPrecoVenda(Acao acao, boolean isOperacaoAberta) {
	   
	    double cotacao = new CotacaoDAO().buscarCotacaoPorAtivo(acao.getAtivo());

		double strike = OpcaoDAO.obterStrikeUltimaOpcaoVendida(acao.getId());
	   
		if (isOperacaoAberta){
			return (strike < cotacao) ? strike : cotacao;
		} else {
			return acao.getPrecoVenda();
		}
	}

}
