package com.example.ecotrack.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEntrega;
    private Cidade cidadeOrigem;
    private Cidade ufOrigem;
    private Cidade cidadeDestino;
    private Cidade ufDestino;
    private String status;
    private LocalDateTime tempoEstimado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataEntrega;
    private Double cargaTotal;

    @ManyToOne
    @JoinColumn(name = "cidade_id", referencedColumnName = "id")
    private Cidade cidade;


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
        return cidadeDestino.getCidade();
    }

    public void setCidadeDestino(String cidadeDestino) {
        this.cidadeDestino = getCidade();
    }

    public String getUfDestino() {
        return ufDestino.getCidade();
    }

    public void setUfDestino(String ufDestino) {
        this.ufDestino = getCidade();
    }

    public String getCidadeOrigem() {
        return cidadeOrigem.getCidade();
    }

    public void setCidadeOrigem(String cidadeOrigem) {
        this.cidadeOrigem = getCidade();
    }

    public String getUfOrigem() {
        return ufOrigem.getCidade();
    }

    public void setUfOrigem(String ufOrigem) {
        this.ufOrigem = getCidade();
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {

        this.cidade = getCidade();
        this.cidadeDestino = getCidade();
        this.cidadeOrigem = getCidade();
    }
}
