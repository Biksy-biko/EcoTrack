package com.example.ecotrack.DTO;

public class PontoColetaRequest {

    public PontoColetaRequest(){}

    private String cidadePonto;
    private String ufPonto;
    private String logradouro;

    public PontoColetaRequest(String cidadePonto, String ufPonto,String logradouro, String codPonto) {
        this.cidadePonto = cidadePonto;
        this.ufPonto = ufPonto;
        this.logradouro = logradouro;
    }

    public String getCidadePonto() {
        return cidadePonto;
    }

    public void setCidadePonto(String cidadePonto) {
        this.cidadePonto = cidadePonto;
    }

    public String getUfPonto() {
        return ufPonto;
    }

    public void setUfPonto(String ufPonto) {
        this.ufPonto = ufPonto;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }
}
