package util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;


public class Utils {
    /**
     * Verifica se a string fornecida representa um valor numérico (double).
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            // 1. Substitui vírgula por ponto para padronizar o formato Double do Java
            String numericString = str.trim().replace(",", ".");
            
            // 2. Tenta fazer o parse para Double
            Double.parseDouble(numericString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static String formatarParaDuasDecimais(double valor) {
        // 1. Define o padrão de formatação
        // #,##0.00 -> Indica um separador de milhar (#,) e duas casas decimais (,00)
        String padrao = "#,##0.00";
        
        // 2. Cria o Locale brasileiro
        Locale brasil = new Locale("pt", "BR");
        
        // 3. Define os símbolos de formatação com base no Locale (vírgula para decimal e ponto para milhar)
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(brasil);
        
        // 4. Cria o formatador com o padrão e os símbolos brasileiros
        DecimalFormat formatador = new DecimalFormat(padrao, simbolos);
        
        // 5. Retorna o valor formatado
        return formatador.format(valor);
    }



        /**
         * Calcula quantos dias de cada mês estão contidos em um período.
         * * @param dtCompraString Data de início do período (ex: "10/01/2025").
         * @param dtVendaString Data de fim do período (ex: "22/02/2025").
         * @return Um Map onde a chave é o mês/ano (ex: "01/2025") e o valor é o número de dias.
         */
	public static Map<String, Integer> contarDiasPorMes(String dtCompraString, String dtVendaString) {
	    
	    // Define o formato de data (dd/MM/yyyy)
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    
	    // Converte as Strings para objetos LocalDate
	    LocalDate dataAtual = LocalDate.parse(dtCompraString, formatter);
	    LocalDate dataFim = LocalDate.parse(dtVendaString, formatter);
	
	    // Map para armazenar os resultados (LinkedHashMap mantém a ordem de inserção)
	    Map<String, Integer> diasPorMes = new LinkedHashMap<>();
	    
	    // Define o formato para a chave do Map (MM/yyyy)
	    DateTimeFormatter mesAnoFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
	
	    // Loop principal: Itera do dia da compra até o dia da venda (inclusive)
	    while (!dataAtual.isAfter(dataFim)) {
	        
	        String mesAno = dataAtual.format(mesAnoFormatter);
	        
	        // Adiciona 1 ao contador daquele mês/ano
	        diasPorMes.merge(mesAno, 1, Integer::sum);
	        
	        // Move para o próximo dia
	        dataAtual = dataAtual.plusDays(1);
	    }
	    
	    return diasPorMes;
	}
}