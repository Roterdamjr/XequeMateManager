package test;

/*
--- Ação: ABEV3 (ID: 2) ---
  [ 02/2025 ]: 1,2439%
  [ 03/2025 ]: 4,8201%
  [ 04/2025 ]: 2,6433%
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

--- Ação: ALOS3 (ID: 12) ---
  [ 04/2025 ]: 3,0558%
  [ 05/2025 ]: 2,4828%
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

--- Ação: SLCE3 (ID: 10) ---
  [ 03/2025 ]: 0,2566%
  [ 04/2025 ]: 1,0998%
  [ 05/2025 ]: 0,6965%
  
  ===== RETORNO TOTAL ACUMULADO POR MÊS =====
  [ 05/2025 ] Total: 3,1794%
  [ 02/2025 ] Total: 1,2439%
  [ 04/2025 ] Total: 6,7989%
  [ 03/2025 ] Total: 5,0768%
  
===== RETORNO TOTAL ACUMULADO POR MÊS =====
  [ 08/2025 ] Total: 6,7405%
  [ 05/2025 ] Total: 33,7063%
  [ 06/2025 ] Total: 11,1853%
  [ 02/2025 ] Total: 4,4653%
  [ 04/2025 ] Total: 43,5176%
  [ 07/2025 ] Total: 5,5310%
  [ 09/2025 ] Total: 1,1300%
  [ 03/2025 ] Total: 30,0821%

*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.AcaoDAO;
import dao.OpcaoDAO;

import model.Acao;
import model.Opcao;
import model.Operacao;
import util.OperacaoAnalyticsDividendos3X;
import util.OperacaoAnalyticsGanhaGanha;
import util.Utils;

public class TesteRetorno {
/*
 * teste   
 * */
	 public static void main(String[] args) {

		List<Acao> acoes = new AcaoDAO().obterAcoesFechadas("3x1");
		
		List<Acao> acoesProcessadas = new ArrayList<>();
		for (Acao acao : acoes) { 
		    if (acao != null) {
		        acoesProcessadas.add(acao);
		        List<Object[]> listaRetorno = calcularRetornoMensal(acao);
		        imprimirResultadosFinais(acao, listaRetorno);
		    }
		}
		
		Map<String, Double> totaisPorMes = calcularTotalRetornoMensal(acoesProcessadas);
		imprimirTotaisPorMes(totaisPorMes);
    }

    public static Map<String, Double> calcularTotalRetornoMensal(List<Acao> acoes) {

        Map<String, Double> totaisPorMes = new HashMap<>();

        for (Acao acao : acoes) {
            List<Object[]> retornosIndividuais = calcularRetornoMensal(acao);
            
            for (Object[] par : retornosIndividuais) {
                String mes = (String) par[0];
                Double retorno = (Double) par[1];

                totaisPorMes.put(mes, totaisPorMes.getOrDefault(mes, 0.0) + retorno);
            }
        }
        return totaisPorMes;
    }

    private static List<Object[]> calcularRetornoMensal(Acao acao) {
    	/*
    	 * retorna os percentuais mensais da Ação
    	 */
    	List<Object[]> retornoMensal = new ArrayList<>();

        List<Opcao> opcoes = new OpcaoDAO().obterOpcoesPorIdAcao(acao.getId());
        Operacao op = new Operacao(acao, opcoes); 
        
        Double resultadoEmValor = new OperacaoAnalyticsGanhaGanha().sumarizaResultado(op, false).getResultado();
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


    
    private static void imprimirResultadosFinais(Acao acao, List<Object[]> listaRetorno) {
        
        System.out.println("\n--- Ação: " + acao.getAtivo() + " (ID: " + acao.getId() + ") ---");
        
        for (Object[] par : listaRetorno) {
            String mes = (String) par[0];
            Double valor = (Double) par[1];
            
            System.out.printf("  [ %s ]: %.4f%%%n", mes, valor * 100);
        }
    }
    
    private static void imprimirTotaisPorMes(Map<String, Double> totais) {
        System.out.println("===== RETORNO TOTAL ACUMULADO POR MÊS =====");
        for (Map.Entry<String, Double> entry : totais.entrySet()) {
            String mes = entry.getKey();
            Double valorTotal = entry.getValue();
            System.out.printf("  [ %s ] Total: %.4f%%%n", mes, valorTotal * 100);
        }
        System.out.println("=================================================");
    }
}