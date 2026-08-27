package com.example.ecotrack.entities;

import java.time.LocalDateTime;

public class Entrega {

    private Long idEntrega;
    private LocalDateTime dataEntrega;
    private Cidade cidadeEntrega;
    private Cidade ufEntrega;
    private Double cargaTotal;

    public Entrega(Long idEntrega, LocalDateTime dataEntrega, Cidade cidadeEntrega, Cidade ufEntrega, Double cargaTotal){
        this.idEntrega=idEntrega;
        this.dataEntrega=dataEntrega;
        this.cidadeEntrega=cidadeEntrega;
        this.ufEntrega=ufEntrega;
        this.cargaTotal=cargaTotal;
    }

    public Long getIdEntrega(){
        return this.idEntrega;
    }
    public LocalDateTime getDataEntrega(){
        return this.dataEntrega;
    }
    public Double getCargaTotal() {
        return this.cargaTotal;
    }

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Cidade getCidadeEntrega() {
        return cidadeEntrega;
    }

    public void setCidadeEntrega(Cidade cidadeEntrega) {
        this.cidadeEntrega = cidadeEntrega;
    }

    public Cidade getUfEntrega() {
        return ufEntrega;
    }

    public void setUfEntrega(Cidade ufEntrega) {
        this.ufEntrega = ufEntrega;
    }

    public void setCargaTotal(Double cargaTotal) {
        this.cargaTotal = cargaTotal;
    }
}
