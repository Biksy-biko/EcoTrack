package com.example.ecotrack.utils;

public class CnpjUtils {

    // Construtor privado para evitar a instanciação da classe utilitária
    private CnpjUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    public static boolean isValidCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            return false;
        }

        String cleanCnpj = OnlyNumbers.extract(cnpj);

        if (cleanCnpj.length() != 14 || cleanCnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            int[] weightFirstDigit = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] weightSecondDigit = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int sum = 0;
            for (int i = 0; i < 12; i++) {
                sum += Character.getNumericValue(cleanCnpj.charAt(i)) * weightFirstDigit[i];
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) {
                firstDigit = 0;
            }

            if (firstDigit != Character.getNumericValue(cleanCnpj.charAt(12))) {
                return false;
            }

            sum = 0;
            for (int i = 0; i < 13; i++) {
                sum += Character.getNumericValue(cleanCnpj.charAt(i)) * weightSecondDigit[i];
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) {
                secondDigit = 0;
            }

            return secondDigit == Character.getNumericValue(cleanCnpj.charAt(13));

        } catch (Exception e) {
            return false;
        }
    }
}
