package com.example.ecotrack.utils;

public class CodigoPontoColeta {

        public static String gerarCodigoPontoColeta(String codigoCompleto) {
            if (codigoCompleto == null || !codigoCompleto.contains("-")) {
                return codigoCompleto;
            }

            String parteAleatoria = codigoCompleto.substring(codigoCompleto.indexOf("-") + 1);

            int meio = parteAleatoria.length() / 2;

            return parteAleatoria.substring(0, meio);
        }
}

