package util;

import java.util.List;

import dao.CotacaoDAO;
import dao.OpcaoDAO;
import model.Acao;
import model.Opcao;

public class OperacaoAnalyticsTresPraUm extends OperacaoAnalyticsBase{

	   protected double calcularPrecoVenda(Acao acao,boolean isOperacaoAberta) {
		   
		   double strike_call=0,strike_put=0;
		   double cotacao =new CotacaoDAO().buscarCotacaoPorAtivo(acao.getAtivo());
		   
		   List<Opcao> opcoes =  new OpcaoDAO().obterOpcoesPorIdAcao(acao.getId());
		   for (Opcao op : opcoes) {
			   if (Utils.identificarTipoOpcao(op.getOpcao()) == "CALL"){
				   strike_call = op.getStrike();
			   }else {
				   strike_put = op.getStrike();
			   }
		   }

			if (isOperacaoAberta){
				if (cotacao > strike_call) {
					return strike_call;
				}else if (cotacao <= strike_call && cotacao >= strike_put) {
					return cotacao;
				}else {
					return strike_put;
				}
			} else {
				return acao.getPrecoVenda();
			}
	   }

}
