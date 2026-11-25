package com.example.ecotrack.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPonto;
    private String cidadePonto;
    private String ufPonto;
    private String logradouro;
    private String codPonto;

    @ManyToMany(mappedBy = "pontoColeta")
    @JsonIgnore
    private List<Entrega> entrega = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "cidade_id", referencedColumnName = "id")
    private Cidade cidade;

    public PontoColeta(){}

    public PontoColeta (Long id, String cidadePonto, String codPonto){
        this.idPonto = id;
        this.cidadePonto = cidadePonto;
        this.codPonto = codPonto;
    }

    public Long getIdPonto(){
        return this.idPonto;
    }

    public String getCidadePonto() {
        return this.cidadePonto;
    }

    public String getCodPonto() {
        return this.codPonto;
    }

    public void setIdPonto(Long idPonto) {
        this.idPonto = idPonto;
    }

    public void setCidadePonto(String cidadePonto) {
        this.cidadePonto = cidadePonto;
    }

    public void setCodPonto(String codPonto) {
        this.codPonto = codPonto;
    }

    public List<Entrega> getEntrega() {
        return entrega;
    }

    public void setEntrega(List<Entrega> entrega) {
        this.entrega = entrega;
    }

    /*public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }*/

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
