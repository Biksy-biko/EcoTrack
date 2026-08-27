package com.example.ecotrack.controllers;

import com.example.ecotrack.entities.Material;
import com.example.ecotrack.entities.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/materiais")
public class MaterialController {

    @GetMapping("/{id}")
    public String ConsultarMaterial(@PathVariable Long id) {
        return "ID do Material:" + id;
    }

    @GetMapping("/{nome}")
    public String MaterialNome(@PathVariable String nome) {
        return "Material:" + nome;
    }

    @GetMapping("/{quantidade}")
    public String QuantidadeMaterial (@PathVariable Long quantidade){
        return "A quantidade de material é (quilos):" + quantidade;
    }

    @PostMapping
    public ResponseEntity<Material> ConsultarMaterialporId (@RequestBody Material materialrequest){
        return ResponseEntity.ok(materialrequest);
    }
}