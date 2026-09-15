package com.example.ecotrack.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaterial;
    private String nomeMaterial;
    private Double peso;
    private String urlImagem;

    public Material(){}

    public Material(Long idMaterial, String nomeMaterial, Double peso, String urlImagem){
        this.idMaterial=idMaterial;
        this.nomeMaterial=nomeMaterial;
        this.peso=peso;
    }

    public Long getIdMaterial(){
        return this.idMaterial;
    }

    public String getNomeMaterial() {
        return this.nomeMaterial;
    }

    public Double getPeso() {
        return this.peso;
    }

    public void setIdMaterial(Long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public void setNomeMaterial(String nomeMaterial) {
        this.nomeMaterial = nomeMaterial;
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
}
