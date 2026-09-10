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
    /*private Cidade cidadeOrigem;
    private Cidade ufOrigem;
    private Cidade cidadeDestino;
    private Cidade ufDestino;
     */
    private LocalDateTime tempoEstimado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataEntrega;

    private Double cargaTotal;

    public Entrega(Long idEntrega, LocalDateTime tempoEstimado, LocalDateTime dataEntrega, Cidade cidadeEntrega, Cidade ufDestino, Double cargaTotal){
        this.idEntrega=idEntrega;
        this.tempoEstimado=tempoEstimado;
        this.dataEntrega=dataEntrega;
       /* this.cidadeDestino =cidadeEntrega;
        this.ufDestino = ufDestino;*/
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

    /*public Cidade getCidadeDestino() {
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
    }*/
}
