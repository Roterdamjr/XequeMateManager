package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.AcaoDAO;
import model.Acao;
import util.Desempenho;

public class TestaDesempenho {
	 public static void main(String[] args) {

		List<Acao> acoes = new AcaoDAO().obterAcoesFechadas();
		
		List<Acao> acoesProcessadas = new ArrayList<>();
		for (Acao acao : acoes) { 
		    if (acao != null) {
		        acoesProcessadas.add(acao);
		    }
		}
		
		Map<String, Double> totaisPorMes = Desempenho.calcularlDesempenhoMensal(acoesProcessadas);
        for (Map.Entry<String, Double> entry : totaisPorMes.entrySet()) {
            String mes = entry.getKey();
            Double valorTotal = entry.getValue();
            System.out.printf("  [ %s ] Total: %.4f%%%n", mes, valorTotal * 100);
        }
   }

}
