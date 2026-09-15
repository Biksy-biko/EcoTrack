package com.example.ecotrack.DTO;

public class CadastrarEntregaResponse {

    public CadastrarEntregaResponse(){}

    public CadastrarEntregaResponse(Long idEntrega, String mensagem){
        this.idEntrega=idEntrega;
        this.mensagem=mensagem;
    }


    private Long idEntrega;
    private String mensagem;

    public Long getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
