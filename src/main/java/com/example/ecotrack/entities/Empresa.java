package com.example.ecotrack.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String cnpj;

    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;

    @OneToMany(mappedBy = "empresa")
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "empresa")
    private List<Entrega> entregas;

    public Empresa() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Entrega> getEntregas() {
        return entregas;
    }

    public void setEntregas(List<Entrega> entregas) {
        this.entregas = entregas;
    }
}
