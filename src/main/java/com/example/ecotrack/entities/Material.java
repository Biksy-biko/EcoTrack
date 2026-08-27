package com.example.ecotrack.entities;

public class Material {

    private Long idMaterial;
    private String nomeMaterial;
    private Double peso;

    public Material(Long idMaterial, String nomeMaterial, Double peso){
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
}
