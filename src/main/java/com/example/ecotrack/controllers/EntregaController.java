package com.example.ecotrack.controllers;

import com.example.ecotrack.entities.Entrega;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

    @GetMapping("/{id}")
    public String EntregaId (@PathVariable Long id){
        return "ID de entrega:" + id;
    }

    @PostMapping
    public ResponseEntity<Entrega> RegistrarEntrega (@RequestBody Entrega entregaRequest){return ResponseEntity.ok(entregaRequest);}

    @PostMapping
    public ResponseEntity<Entrega> CancelarEntrega (@RequestBody Entrega entregaRequest){return ResponseEntity.ok(entregaRequest);}

    /*@GetMapping("/{dataEntrega}")
    public String DatadaEntrega(@PathVariable Long dataEntrega) {
        return "Data: "+dataEntrega;
    }

    @GetMapping("/{quantidadeEntrega}")
    public String QuantidadeEntregas(@PathVariable Long quantidadeEntrega) {
        return "Entregas já realziadas: "+quantidadeEntrega;
    }*/

}
