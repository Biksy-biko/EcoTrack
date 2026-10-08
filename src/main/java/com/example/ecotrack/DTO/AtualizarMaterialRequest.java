package com.example.ecotrack.DTO;

public class AtualizarMaterialRequest {
    public AtualizarMaterialRequest(){}

    private String nomeMaterial;
    private Double peso;
    private String urlImagem;
    private String descricao;
    private String decomposicao;

    public String getNomeMaterial() {
        return nomeMaterial;
    }

    public void setNomeMaterial(String nomeMaterial) {
        this.nomeMaterial = nomeMaterial;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDecomposicao() {
        return decomposicao;
    }

    public void setDecomposicao(String decomposicao) {
        this.decomposicao = decomposicao;
    }
}
