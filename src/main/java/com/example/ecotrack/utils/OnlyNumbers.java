package com.example.ecotrack.utils;

public class OnlyNumbers {

    // Construtor privado para evitar a instanciação da classe utilitária
    private OnlyNumbers() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /**
     * Remove todos os caracteres que não sejam dígitos numéricos.
     * Útil para limpar formatações de CPF, CNPJ, Telefones, CEP, etc.
     *
     * @param value String de entrada (ex: "(11) 98888-7777" ou "123.456.789-01")
     * @return String contendo apenas os números (ex: "11988887777" ou "12345678901")
     */
    public static String extract(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        // O regex "\\D" encontra qualquer caractere que NÃO seja um dígito (0-9) e o substitui por vazio
        return value.replaceAll("\\D", "");
    }
}