package com.example.ecotrack.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEntrega;
    private String cidadeOrigem;
    private String ufOrigem;
    private String cidadeDestino;
    private String ufDestino;
    private String status;
    private LocalDateTime tempoEstimado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataEntrega;

    private Double cargaTotal;

    public Entrega(Long idEntrega, LocalDateTime tempoEstimado, LocalDateTime dataEntrega, String cidadeEntrega, String ufDestino, Double cargaTotal){
        this.idEntrega=idEntrega;
        this.tempoEstimado=tempoEstimado;
        this.dataEntrega=dataEntrega;
        this.cidadeDestino =cidadeEntrega;
        this.ufDestino = ufDestino;
        this.cargaTotal=cargaTotal;
    }
    public Entrega(){}

    public Long getIdEntrega(){
        return this.idEntrega;
    }
    public LocalDateTime getDataEntrega(){
        return this.dataEntrega;
    }
    public LocalDateTime getTempoEstimado(){
        return this.tempoEstimado;
    }
    public Double getCargaTotal() {
        return this.cargaTotal;
    }

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
    }

    public void setTempoEstimado(LocalDateTime tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    public void setCargaTotal(Double cargaTotal) {
        this.cargaTotal = cargaTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCidadeOrigem() {
        return cidadeOrigem;
    }

    public void setCidadeOrigem(String cidadeOrigem) {
        this.cidadeOrigem = cidadeOrigem;
    }

    public String getUfOrigem() {
        return ufOrigem;
    }

    public void setUfOrigem(String ufOrigem) {
        this.ufOrigem = ufOrigem;
    }
}
