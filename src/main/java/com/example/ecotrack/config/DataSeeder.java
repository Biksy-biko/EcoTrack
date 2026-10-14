package com.example.ecotrack.config;

import com.example.ecotrack.entities.Cidade;
import com.example.ecotrack.repository.CidadeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CidadeRepository cidadeRepository;

    public DataSeeder(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (cidadeRepository.count() == 0) {
            System.out.println("Tabela vazia. Buscando todas as cidades na API do IBGE (isso pode levar alguns segundos)...");

            RestTemplate restTemplate = new RestTemplate();
            String url = "https://servicodados.ibge.gov.br/api/v1/localidades/municipios";

            MunicipioDTO[] municipios = restTemplate.getForObject(url, MunicipioDTO[].class);

            if (municipios != null) {
                List<Cidade> cidades = new ArrayList<>();

                for (MunicipioDTO dto : municipios) {
                    if (dto.microrregiao() == null || dto.microrregiao().mesorregiao() == null) {
                        continue;
                    }

                    Cidade cidade = new Cidade();
                    cidade.setCidade(dto.nome());
                    cidade.setIBGE(String.valueOf(dto.id()));
                    cidade.setUf(dto.microrregiao().mesorregiao().uf().sigla());

                    cidades.add(cidade);
                }

                cidadeRepository.saveAll(cidades);
                System.out.println("Sucesso! " + cidades.size() + " cidades foram cadastradas no banco de dados.");
            }
        } else {
            System.out.println("As cidades já estão cadastradas no banco.");
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MunicipioDTO(Long id, String nome, MicrorregiaoDTO microrregiao) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MicrorregiaoDTO(MesorregiaoDTO mesorregiao) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MesorregiaoDTO(@JsonProperty("UF") UFDTO uf) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UFDTO(String sigla) {}
}