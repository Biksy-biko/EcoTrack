package com.example.ecotrack.DTO;

import com.example.ecotrack.entities.Cidade;

import java.time.LocalDateTime;

public class CadastrarEntregaRequest {
    public CadastrarEntregaRequest(){}

    private Cidade cidadeOrigem;
    private Cidade ufOrigem;
    private Cidade cidadeEntrega;
    private Cidade ufEntrega;
    private Double cargaTotal;
    private LocalDateTime tempoEstimado;

}
