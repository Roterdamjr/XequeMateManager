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
	   
	   double maximo = (strike_call > strike_put) ? strike_call : strike_put;
	   double minimo = (strike_call < strike_put) ? strike_call : strike_put;

		if(acao.getId()==58){
			int a=1;
			a++;
		}
		if (isOperacaoAberta){
			if (cotacao > maximo) {
				return maximo;
			}else if (cotacao <= maximo && cotacao >= minimo) {
				return cotacao;
			}else {
				return minimo;
			}
		} else {
			return acao.getPrecoVenda();
		}
   }

}
