package com.example.ecotrack.utils;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodigoRastreioUtils {

        private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private static final int CODE_LENGTH = 10;
        private static final SecureRandom RANDOM = new SecureRandom();

        public String generate() {
            String randomPart = IntStream.range(0, CODE_LENGTH)
                    .map(i -> RANDOM.nextInt(CHAR_POOL.length()))
                    .mapToObj(CHAR_POOL::charAt)
                    .map(String::valueOf)
                    .collect(Collectors.joining());

            return "ENT-" + randomPart;
        }
}

