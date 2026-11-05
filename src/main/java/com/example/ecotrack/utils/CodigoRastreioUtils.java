package com.example.ecotrack.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CodigoRastreioUtils {

        private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private static final int CODE_LENGTH = 10;
        private static final SecureRandom RANDOM = new SecureRandom();

        public static String gerarCodigo() {
            String codigoAleatorio = IntStream.range(0, CODE_LENGTH)
                    .map(i -> RANDOM.nextInt(CHAR_POOL.length()))
                    .mapToObj(CHAR_POOL::charAt)
                    .map(String::valueOf)
                    .collect(Collectors.joining());

            return "ENT-" + codigoAleatorio;
        }
}

