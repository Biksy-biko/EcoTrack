package com.example.ecotrack.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cidade;
    private String IBGE;
    private String uf;

   /* @OneToMany
    private Entrega entrega;*/

    public Cidade() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getIBGE() {
        return IBGE;
    }

    public void setIBGE(String IBGE) {
        this.IBGE = IBGE;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }


}
