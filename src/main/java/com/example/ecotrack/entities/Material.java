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

    public Material(){}

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
