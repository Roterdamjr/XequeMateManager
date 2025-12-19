package relat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.OpcaoDAO;
import model.Acao;
import model.Opcao;
import model.Operacao;
import util.OperacaoAnalyticsDividendos3X;
import util.Utils;

public class Desempenho {
	
	public static DesempenhoConsolidado calcularlDesempenhoMensal(List<Acao> acoes) {
	    Map<String, Double> somaPercentualPorMes = new HashMap<>();
	    Map<String, Double> totaisPorMesEmValor = new HashMap<>();
	    Map<String, Integer> contagemPorMes = new HashMap<>();

	    for (Acao acao : acoes) {
	        List<Object[]> retornosIndividuais = calcularRetornoMensalPorAcao(acao);
	        
	        for (Object[] par : retornosIndividuais) {
	            String mes = (String) par[0];
	            Double retorno = (Double) par[1];
	            Double retornoEmValor = (Double) par[2];

	            somaPercentualPorMes.merge(mes, retorno, Double::sum);
	            totaisPorMesEmValor.merge(mes, retornoEmValor, Double::sum);

	            contagemPorMes.merge(mes, 1, Integer::sum);
	            
	            System.out.println(mes + "; " + acao.getAtivo() + "; " + retornoEmValor + ";" + retorno);
	        }
	    }

	    Map<String, Double> mediaPercentualPorMes = new HashMap<>();
	    somaPercentualPorMes.forEach((mes, soma) -> {
	        double media = soma / contagemPorMes.get(mes);
	        mediaPercentualPorMes.put(mes, media);
	    });

	    return new DesempenhoConsolidado(mediaPercentualPorMes, totaisPorMesEmValor);
	}

    public static List<Object[]> calcularRetornoMensalPorAcao(Acao acao) {
    	/*
    	 * retorna os percentuais mensais da Ação
    	 */
    	List<Object[]> retornoMensal = new ArrayList<>();

        List<Opcao> opcoes = new OpcaoDAO().obterOpcoesPorIdAcao(acao.getId());
        Operacao op = new Operacao(acao, opcoes); 
        
        Double resultadoEmValor = new OperacaoAnalyticsDividendos3X().sumarizaResultado(op, false).getResultado();
        String dtCompra = acao.getDataCompra();
        String dtVenda = acao.getDataVenda();

        Map<String, Integer> mapaDiasPorMes = Utils.contarDiasPorMes(dtCompra, dtVenda);
        
        int totalDias=0;
        try {
        	totalDias = Utils.calcularTotalDias(dtCompra, dtVenda);
        }catch (Exception e) {
        	System.out.println("erro em : " + acao);
		}
        
        
        double valorInvestido = acao.getQuantidade() * acao.getPrecoCompra();
        if (valorInvestido == 0) {
            return retornoMensal;
        }

        Double retornoPercentualTotal = resultadoEmValor / valorInvestido;
 
        for (Map.Entry<String, Integer> entry : mapaDiasPorMes.entrySet()) {
            int qtDiasNoMes = entry.getValue();
            Double retornoPercentual = ((double) qtDiasNoMes / totalDias) * retornoPercentualTotal;
            
            Double retornoEmValor =            ((double) qtDiasNoMes / totalDias) *  resultadoEmValor;
            
            		
            Object[] linhaRetorno = new Object[] { 	entry.getKey(), 
            										retornoPercentual ,
            										retornoEmValor};
            retornoMensal.add(linhaRetorno);
        }
        
        return retornoMensal;
    }
    
    public static class DesempenhoConsolidado {
        public final Map<String, Double> percentual;
        public final Map<String, Double> valor;

        public DesempenhoConsolidado(Map<String, Double> p, Map<String, Double> v) {
            this.percentual = p;
            this.valor = v;
        }
    }
}
