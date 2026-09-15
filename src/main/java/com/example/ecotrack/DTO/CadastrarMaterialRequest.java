package com.example.ecotrack.DTO;

public class CadastrarMaterialRequest {

    public CadastrarMaterialRequest(){
    }

    private String nomeMaterial;
    private String urlImagem;
    private Double peso;

    public String getNomeMaterial() {
        return nomeMaterial;
    }

    public void setNomeMaterial(String nomeMaterial) {
        this.nomeMaterial = nomeMaterial;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }
}
