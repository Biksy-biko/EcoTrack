package com.example.ecotrack.entities;

import java.security.PrivateKey;

public class PontoColeta {

    private Long idPonto;
    private String localPonto;
    private String numero;

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
