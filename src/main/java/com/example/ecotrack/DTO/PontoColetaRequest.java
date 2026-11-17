package com.example.ecotrack.DTO;

public class PontoColetaRequest {

    public PontoColetaRequest(){}

    private String cidadePonto;
    private String ufPonto;
    private String codPonto;

    public PontoColetaRequest(String cidadePonto, String ufPonto, String codPonto) {
        this.cidadePonto = cidadePonto;
        this.ufPonto = ufPonto;
        this.codPonto = codPonto;
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

    public String getCodPonto() {
        return codPonto;
    }

    public void setCodPonto(String codPonto) {
        this.codPonto = codPonto;
    }
}
