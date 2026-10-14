package com.example.ecotrack.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CepUtils {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    // Construtor privado para impedir instanciação
    private CepUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /**
     * Record para encapsular o resultado da cidade e UF.
     * Recurso nativo do Java 17 (imutável e conciso).
     */
    public record Localizacao(String cidade, String uf) {}

    /**
     * Consulta um CEP na API do ViaCEP e retorna a Cidade e a UF correspondentes.
     */
    public static Localizacao buscarCidadeEUfPorCep(String cep) {
        if (cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("O CEP não pode ser nulo ou em branco.");
        }

        // Reaproveita o OnlyNumbers para limpar a string (ex: "01001-000" -> "01001000")
        String cepLimpo = OnlyNumbers.extract(cep);

        if (cepLimpo.length() != 8) {
            throw new IllegalArgumentException("CEP inválido. Deve conter exatamente 8 dígitos.");
        }

        String url = "https://viacep.com.br/ws/" + cepLimpo + "/json/";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro ao consultar o serviço de CEP. Status HTTP: " + response.statusCode());
            }

            String json = response.body();

            // Verifica se o ViaCEP retornou um erro indicando que o CEP não existe
            if (json.contains("\"erro\": true") || json.contains("\"erro\":true")) {
                throw new IllegalArgumentException("CEP não encontrado: " + cep);
            }

            // Extração dos campos
            String cidade = extrairValorJson(json, "localidade");
            String uf = extrairValorJson(json, "uf");

            return new Localizacao(cidade, uf);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("A requisição de CEP foi interrompida.", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar a consulta do CEP: " + e.getMessage(), e);
        }
    }

    /**
     * Método auxiliar privado para extrair valores do JSON retornado sem precisar de bibliotecas pesadas.
     */
    private static String extrairValorJson(String json, String chave) {
        Pattern pattern = Pattern.compile("\"" + chave + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
