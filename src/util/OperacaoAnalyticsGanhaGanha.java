package util;


import model.Acao;

public class OperacaoAnalyticsGanhaGanha extends OperacaoAnalyticsBase{

	   protected double calcularPrecoVenda(Acao acao,
											double strike, 
											double cotacao, 
											double precoVenda,
											boolean isOperacaoAberta) {

					
			if (isOperacaoAberta){
			return (strike < cotacao) ? strike : cotacao;
			} else {
			return precoVenda;
			}
	   }

}
