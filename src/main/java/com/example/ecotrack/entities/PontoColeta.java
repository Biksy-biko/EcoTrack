package com.example.ecotrack.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.security.PrivateKey;

@Entity
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPonto;
    private String localPonto;
    private String numero;

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
}
