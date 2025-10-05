package test;

import java.util.List;
import java.util.Map;

import dao.AcaoDAO;
import dao.CotacaoDAO;
import dao.DividendoDAO;
import dao.OpcaoDAO;

import model.Acao;
import model.Dividendo;
import model.Opcao;
import model.Operacao;
import model.OperacaoDividendo;
import model.ResultadoOperacao;
import util.OperacaoAnalytics;
import util.Relatorio;
import util.Utils;

public class TesteRetorno {
/* ABEV3 id=2
Ativo: ABEV3  resultadoEmValor:484.99999999999943  Período: 21/02/2025 a 17/04/2025  retornoPercentualTotal: 0.08707360861759415  Dias: 56
Mês: 02/2025  Dias: 8  retornoPercentual: 0.012439086945370593
Mês: 03/2025  Dias: 31  retornoPercentual: 0.04820146191331105
Mês: 04/2025  Dias: 17  retornoPercentual: 0.026433059758912508
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Ativo: ALOS3  resultadoEmValor:217.99999999999997  Período: 15/04/2025 a 13/05/2025  retornoPercentualTotal: 0.055386178861788614  Dias: 29
Mês: 04/2025  Dias: 16  retornoPercentual: 0.030557891785814407
Mês: 05/2025  Dias: 13  retornoPercentual: 0.024828287075974207
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Ativo: SLCE3  resultadoEmValor:75.9999999999998  Período: 25/03/2025 a 19/05/2025  retornoPercentualTotal: 0.020529443544030197  Dias: 56
Mês: 03/2025  Dias: 7  retornoPercentual: 0.0025661804430037747
Mês: 04/2025  Dias: 30  retornoPercentual: 0.010997916184301891
Mês: 05/2025  Dias: 19  retornoPercentual: 0.006965346916724531
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	
	 public static void main(String[] args) {
		 umaAcao(new Acao(2));
		 umaAcao(new Acao(12));
		 umaAcao(new Acao(10));
	}
	 
	private static void umaAcao(Acao ac) {
   		Acao acao = new AcaoDAO().obterAcaoPorId( ac.getId() );
	    List<Opcao> opcoes =  new OpcaoDAO().obterOpcoesPorIdAcao(acao.getId());
	    Operacao op =new Operacao(acao,opcoes) ; 
		
	    Double resultadoEmValor = OperacaoAnalytics.sumarizaReeultado(op,false).getResultado();
	    String dtCompra = acao.getDataCompra();
	    String dtVenda=acao.getDataVenda();

	    Map<String, Integer> mapa = Utils.contarDiasPorMes(dtCompra, dtVenda);

	    int totalDias = Utils.calcularTotalDias(dtCompra, dtVenda);

        Double retornoPercentualTotal =  resultadoEmValor / (acao.getQuantidade()*acao.getPrecoCompra());
        System.out.println("Ativo: "+ acao.getAtivo() +
        		"  resultadoEmValor:"+ resultadoEmValor + 
        		"  Período: " + dtCompra + " a " + dtVenda + 
        		"  retornoPercentualTotal: "  + retornoPercentualTotal+
        		"  Dias: "+ totalDias);
        
        for (Map.Entry<String, Integer> entry : mapa.entrySet()) {
        	int qtDiasNoMes = entry.getValue();
        	
        	// retorno percentual é o retorno dividido pelo valor investido
        	Double retornoPercentual = (double) qtDiasNoMes / totalDias * retornoPercentualTotal;
 
            System.out.println("Mês: " +    entry.getKey() +
            		"  Dias: " +qtDiasNoMes + 
                    "  retornoPercentual: " +retornoPercentual
                );
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}
	


	
    
    
}
