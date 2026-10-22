package com.example.ecotrack.entities;

import jakarta.persistence.*;

import java.security.PrivateKey;

@Entity
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPonto;
    private String localPonto;
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrega_id", nullable = false)
    private Entrega entrega;

    public PontoColeta(){}

    public PontoColeta (Long id, String localPonto, String numero){
        this.idPonto = id;
        this.localPonto = localPonto;
        this.numero = numero;
    }

    public Long getIdPonto(){
        return this.idPonto;
    }

    public String getLocalPonto() {
        return this.localPonto;
    }

    public String getNumero() {
        return this.numero;
    }

    public void setIdPonto(Long idPonto) {
        this.idPonto = idPonto;
    }

    public void setLocalPonto(String localPonto) {
        this.localPonto = localPonto;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }
}
