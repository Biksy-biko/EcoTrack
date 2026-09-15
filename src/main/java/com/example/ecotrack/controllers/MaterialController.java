package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.CadastrarMaterialRequest;
import com.example.ecotrack.DTO.CadastrarMaterialResponse;
import com.example.ecotrack.entities.Material;
import com.example.ecotrack.entities.Usuario;
import com.example.ecotrack.repository.EntregaRepository;
import com.example.ecotrack.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/materiais")
public class MaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    @GetMapping("/{id}")
    public String ConsultarMaterial(@PathVariable Long id) {
        return "ID do Material:" + id;
    }

    @GetMapping
    public List<Material> ConsultarMateriais(){
        
        return materialRepository.findAll();
    }

    @GetMapping("/{quantidade}")
    public String QuantidadeMaterial (@PathVariable Long quantidade){
        return "A quantidade de material é (quilos):" + quantidade;
    }

    @PostMapping
    public ResponseEntity<CadastrarMaterialResponse> CadastrarMaterial (@RequestBody CadastrarMaterialRequest materialRequest){

        Material materialBanco = new Material();
        materialBanco.setNomeMaterial(materialRequest.getNomeMaterial());
        materialBanco.setPeso(materialRequest.getPeso());
        materialBanco.setUrlImagem(materialRequest.getUrlImagem());

        materialRepository.save(materialBanco);

        return ResponseEntity.ok(new CadastrarMaterialResponse(materialBanco.getIdMaterial(),"Material Inserido."));

    }
}