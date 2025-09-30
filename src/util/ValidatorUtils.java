package util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ValidatorUtils {
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
}