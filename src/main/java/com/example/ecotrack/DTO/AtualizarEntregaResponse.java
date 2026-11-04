package com.example.ecotrack.DTO;

public class AtualizarEntregaResponse {

    public AtualizarEntregaResponse(){}

    public AtualizarEntregaResponse (Long id, Long idEntrega,String mensagem){
        this.id=id;
        this.mensagem=mensagem;
    }

    private Long id;
    private String mensagem;
    private Long idEntrega;

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

    public Long getIdEntrega(Long idEntrega){return idEntrega;}

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
    }
}
