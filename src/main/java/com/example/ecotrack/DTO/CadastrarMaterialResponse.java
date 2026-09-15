package com.example.ecotrack.DTO;

public class CadastrarMaterialResponse {

    public CadastrarMaterialResponse(){}

    public CadastrarMaterialResponse(Long idMaterial, String mensagem){
        this.idMaterial= idMaterial;
        this.mensagem= mensagem;
    }

    private Long idMaterial;
    private String mensagem;

    public Long getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
