package com.example.ecotrack.DTO;

import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;
import java.util.List;

public class CadastrarEntregaRequest {

    public CadastrarEntregaRequest(){}

    private String codigoRastreio;
    private String cidadeOrigem;
    private String ufOrigem;
    private String cidadeDestino;
    private String ufDestino;
    private Double cargaTotal;
    private LocalDateTime tempoEstimado;
    private Long idEntrega;
    private List<Long> idPonto;

    public CadastrarEntregaRequest(Long idEntrega, List<Long> idPonto) {
        this.idEntrega = idEntrega;
        this.idPonto = idPonto;
    }

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

    public List<Long> getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(List<Long> idPonto) {
        this.idPonto = idPonto;
    }

    public Long getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }
}
