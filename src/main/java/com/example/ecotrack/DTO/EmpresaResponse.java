package com.example.ecotrack.DTO;

public class EmpresaResponse {

    private Long id;
    private String mensagem;

    public EmpresaResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
