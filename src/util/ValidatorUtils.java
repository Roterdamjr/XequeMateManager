package util;

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
}