package com.example.ecotrack.entities;

import jakarta.persistence.*;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPonto;
    private String localPonto;
    private String numero;

    @ManyToMany(mappedBy = "pontoColeta")
    private List<Entrega> entrega = new ArrayList<>();

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

    public List<Entrega> getEntrega() {
        return entrega;
    }

    public void setEntrega(List<Entrega> entrega) {
        this.entrega = entrega;
    }
}
