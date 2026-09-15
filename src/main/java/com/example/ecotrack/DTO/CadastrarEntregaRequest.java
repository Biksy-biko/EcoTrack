package com.example.ecotrack.DTO;

import java.time.LocalDateTime;

public class CadastrarEntregaRequest {

    public CadastrarEntregaRequest(){}

    private String cidadeOrigem;
    private String ufOrigem;
    private String cidadeDestino;
    private String ufDestino;
    private Double cargaTotal;
    private LocalDateTime tempoEstimado;

    public String getUfOrigem() {
        return ufOrigem;
    }

    public void setUfOrigem(String ufOrigem) {
        this.ufOrigem = ufOrigem;
    }

    public String getCidadeDestino() {
        return cidadeDestino;
    }

    public void setCidadeDestino(String cidadeDestino) {
        this.cidadeDestino = cidadeDestino;
    }

    public String getUfDestino() {
        return ufDestino;
    }

    public void setUfDestino(String ufDestino) {
        this.ufDestino = ufDestino;
    }

    public Double getCargaTotal() {
        return cargaTotal;
    }

    public void setCargaTotal(Double cargaTotal) {
        this.cargaTotal = cargaTotal;
    }

    public String getCidadeOrigem() {
        return cidadeOrigem;
    }

    public void setCidadeOrigem(String cidadeOrigem) {
        this.cidadeOrigem = cidadeOrigem;
    }

    public LocalDateTime getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(LocalDateTime tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }
}
