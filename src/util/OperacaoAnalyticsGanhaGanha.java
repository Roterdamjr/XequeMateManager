package util;


import dao.OpcaoDAO;
import model.Acao;

public class OperacaoAnalyticsGanhaGanha extends OperacaoAnalyticsBase{

	   protected double calcularPrecoVenda(Acao acao,
											double strike, 
											double cotacao, 
											double precoVenda,
											boolean isOperacaoAberta) {
	
		   double strike_call = new OpcaoDAO().ob
				   strike_put=0;
		   
			if (isOperacaoAberta){
				if (cotacao > strike_call) {
					return strike;
				}else if (cotacao <= strike_call && cotacao >= strike_put) {
					return cotacao;
				}else {
					return strike_put;
				}
			} else {
				return precoVenda;
			}
	   }

}
