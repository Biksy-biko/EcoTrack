package com.example.ecotrack.entities;

import java.time.LocalDateTime;

public class Entrega {

    private Long idEntrega;
    private Cidade cidadeOrigem;
    private Cidade ufOrigem;
    private LocalDateTime tempoEstimado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataEntrega;
    private Cidade cidadeDestino;
    private Cidade ufDestino;
    private Double cargaTotal;

    public Entrega(Long idEntrega, LocalDateTime tempoEstimado, LocalDateTime dataEntrega, Cidade cidadeEntrega, Cidade ufDestino, Double cargaTotal){
        this.idEntrega=idEntrega;
        this.tempoEstimado=tempoEstimado;
        this.dataEntrega=dataEntrega;
        this.cidadeDestino =cidadeEntrega;
        this.ufDestino = ufDestino;
        this.cargaTotal=cargaTotal;
    }

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

    public Cidade getCidadeDestino() {
        return cidadeDestino;
    }

    public void setCidadeDestino(Cidade cidadeDestino) {
        this.cidadeDestino = cidadeDestino;
    }

    public Cidade getUfDestino() {
        return ufDestino;
    }

    public void setUfDestino(Cidade ufDestino) {
        this.ufDestino = ufDestino;
    }

    public void setCargaTotal(Double cargaTotal) {
        this.cargaTotal = cargaTotal;
    }

    public Cidade getCidadeOrigem() {
        return cidadeOrigem;
    }

    public void setCidadeOrigem(Cidade cidadeOrigem) {
        this.cidadeOrigem = cidadeOrigem;
    }

    public Cidade getUfOrigem() {
        return ufOrigem;
    }

    public void setUfOrigem(Cidade ufOrigem) {
        this.ufOrigem = ufOrigem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
