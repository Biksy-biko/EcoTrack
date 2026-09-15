package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.CadastrarEntregaRequest;
import com.example.ecotrack.DTO.CadastrarEntregaResponse;
import com.example.ecotrack.entities.Entrega;
import com.example.ecotrack.entities.Material;
import com.example.ecotrack.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

    @Autowired
    private EntregaRepository entregaRepository;

    @GetMapping("/{id}")
    public String EntregaId (@PathVariable Long id){
        return "ID de entrega:" + id;
    }

    @GetMapping
    public List<Material> ConsultarEtrega(){

        return entregaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<CadastrarEntregaResponse> RegistrarEntrega (@RequestBody CadastrarEntregaRequest entregaRequest) {

        Entrega entregaBanco = new Entrega();
        entregaBanco.setCidadeOrigem(entregaRequest.getCidadeOrigem());
        entregaBanco.setUfOrigem(entregaRequest.getUfOrigem());
        entregaBanco.setCidadeDestino(entregaRequest.getCidadeDestino());
        entregaBanco.setUfDestino(entregaRequest.getUfDestino());
        entregaBanco.setCargaTotal(entregaRequest.getCargaTotal());
        entregaBanco.setTempoEstimado(entregaRequest.getTempoEstimado());

        entregaBanco.setStatus("Pendente");
        entregaBanco.setDataCriacao(LocalDateTime.now());

        entregaRepository.save(entregaBanco);
        return ResponseEntity.ok(new CadastrarEntregaResponse(entregaBanco.getIdEntrega(),"Entrega Marcada."));
    }

    /*@PostMapping
    public ResponseEntity<Entrega> CancelarEntrega (@RequestBody Entrega entregaRequest){
        return ResponseEntity.ok(entregaRequest);
    }*/

    @GetMapping("/{dataEntrega}")
    public String DatadaEntrega(@PathVariable Long dataEntrega) {
        return "Data: "+dataEntrega;
    }

    @GetMapping("/{quantidadeEntrega}")
    public String QuantidadeEntregas(@PathVariable Long quantidadeEntrega) {
        return "Entregas já realziadas: "+quantidadeEntrega;
    }

}
