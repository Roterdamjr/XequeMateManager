package util;


public class OperacaoAnalyticsGanhaGanha extends OperacaoAnalyticsBase{

		protected double calcularResultado(double strike, 
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
