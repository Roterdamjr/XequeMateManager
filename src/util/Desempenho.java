package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.OpcaoDAO;
import model.Acao;
import model.Opcao;
import model.Operacao;

public class Desempenho {
   public static Map<String, Double> calcularlDesempenhoMensal(List<Acao> acoes) {

        Map<String, Double> totaisPorMes = new HashMap<>();

        for (Acao acao : acoes) {
            List<Object[]> retornosIndividuais = calcularRetornoMensalPorAcao(acao);
            
            for (Object[] par : retornosIndividuais) {
                String mes = (String) par[0];
                Double retorno = (Double) par[1];

                totaisPorMes.put(mes, totaisPorMes.getOrDefault(mes, 0.0) + retorno);
            }
        }
        return totaisPorMes;
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
        int totalDias = Utils.calcularTotalDias(dtCompra, dtVenda);
        
        double valorInvestido = acao.getQuantidade() * acao.getPrecoCompra();
        if (valorInvestido == 0) {
            return retornoMensal;
        }

        Double retornoPercentualTotal = resultadoEmValor / valorInvestido;
 
        for (Map.Entry<String, Integer> entry : mapaDiasPorMes.entrySet()) {
            int qtDiasNoMes = entry.getValue();
            Double retornoPercentual = ((double) qtDiasNoMes / totalDias) * retornoPercentualTotal;
            
            Object[] parMesValor = new Object[] { entry.getKey(), retornoPercentual };
            retornoMensal.add(parMesValor);
        }
        
        return retornoMensal;
    }
}
