package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.*;
import com.example.ecotrack.entities.Entrega;
import com.example.ecotrack.entities.Material;
import com.example.ecotrack.entities.Usuario;
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
    public List<Entrega> ConsultarEtrega(){

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

        if (entregaRequest.getTempoEstimado().isBefore(LocalDateTime.now())){
            return ResponseEntity.badRequest()
                    .body(new CadastrarEntregaResponse(null,"Sem entregas para o passado, viajante do tempo!"));
        }

        entregaBanco.setStatus("Pendente");
        entregaBanco.setDataCriacao(LocalDateTime.now());

        entregaRepository.save(entregaBanco);
        return ResponseEntity.ok(new CadastrarEntregaResponse(entregaBanco.getIdEntrega(),"Entrega Marcada."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AtualizarEntregaResponse> CancelarEntrega (@PathVariable Long id){

            Entrega entregaBanco = entregaRepository.findById(id).orElse(null);

            if (entregaBanco != null){
                entregaBanco.setStatus("Cancelada");
                entregaRepository.save(entregaBanco);
                return ResponseEntity.ok().build();

            }return ResponseEntity.notFound().build();
        }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AtualizarEntregaResponse> ConcluirEntrega (@PathVariable Long id, @RequestBody AtualizarStatusEntregaRequest entregaRequest){

        Entrega entregaBanco = entregaRepository.findById(id).orElse(null);

        if (entregaBanco != null){
            entregaBanco.setStatus("Entregue");
            entregaBanco.setDataEntrega(LocalDateTime.now());
            entregaRepository.save(entregaBanco);
            return ResponseEntity.ok(new AtualizarEntregaResponse(entregaBanco.getIdEntrega(),"Entrega Finalizada."));


        }return ResponseEntity.notFound().build();
    }
}

    /*@GetMapping("/{dataEntrega}")
    public String DatadaEntrega(@PathVariable Long dataEntrega) {
        return "Data: "+dataEntrega;
    }

    @GetMapping("/{quantidadeEntrega}")
    public String QuantidadeEntregas(@PathVariable Long quantidadeEntrega) {
        return "Entregas já realziadas: "+quantidadeEntrega;
    }*/

