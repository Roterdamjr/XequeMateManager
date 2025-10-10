package util;


public class OperacaoAnalyticsGanhaGanha extends OperacaoAnalytics{

		protected double calcularPrecoVenda(double strike, 
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
