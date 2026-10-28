package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.*;
import com.example.ecotrack.entities.Material;
import com.example.ecotrack.entities.Usuario;
import com.example.ecotrack.repository.EntregaRepository;
import com.example.ecotrack.repository.MaterialRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/materiais")
@Tag(
        name = "Controlador de Materiais",
        description = "Gerenciador de informativos dos materiais do sistema."
)
public class MaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    @GetMapping("/{id}")
    @Operation(
            summary = "Consultar material por ID",
            description = "Busca e retorna os detalhes de um material específico com base no ID fornecido na URL. Retorna 404 caso o material não seja encontrado."
    )
    public ResponseEntity<Material> ConsultaMaterialPorId(@PathVariable Long id) {
        var material = materialRepository.findById(id).orElse(null);

        if (material == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(material);
    }

    @GetMapping
    @Operation(
            summary = "Consultar todos os materiais",
            description = "Retorna uma lista completa contendo todos os materiais cadastrados no sistema."
    )
    public List<Material> ConsultarMateriais() {

        return materialRepository.findAll();
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar novo material",
            description = "Recebe os dados de um novo material no corpo da requisição, salva as informações no banco de dados e retorna o ID gerado juntamente com uma mensagem de confirmação."
    )
    public ResponseEntity<CadastrarMaterialResponse> CadastrarMaterial(@RequestBody CadastrarMaterialRequest materialRequest) {

        Material materialBanco = new Material();
        materialBanco.setNomeMaterial(materialRequest.getNomeMaterial());
        materialBanco.setPeso(materialRequest.getPeso());
        materialBanco.setUrlImagem(materialRequest.getUrlImagem());
        materialBanco.setDescricao(materialRequest.getDescricao());
        materialBanco.setDecomposicao(materialRequest.getDecomposicao());

        materialRepository.save(materialBanco);

        return ResponseEntity.ok(new CadastrarMaterialResponse(materialBanco.getIdMaterial(), "Material Inserido."));

    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar material existente",
            description = "Atualiza os dados de um material localizado pelo ID informado na URL utilizando as novas informações enviadas no payload. Retorna 404 caso o material não exista."
    )
    public ResponseEntity<AtualizarMaterialResponse> AtualizarMaterial(@PathVariable Long id, @RequestBody AtualizarMaterialRequest materialRequest) {

        Material materialBanco = materialRepository.findById(id).orElse(null);

        if (materialBanco != null){
            materialBanco.setNomeMaterial(materialRequest.getNomeMaterial());
            materialBanco.setPeso(materialRequest.getPeso());
            materialBanco.setUrlImagem(materialRequest.getUrlImagem());
            materialBanco.setDescricao(materialRequest.getDescricao());
            materialBanco.setDecomposicao(materialRequest.getDecomposicao());


            materialRepository.save(materialBanco);
            return ResponseEntity.ok(new AtualizarMaterialResponse(materialBanco.getIdMaterial(),"Material atualizado.."));

        }return ResponseEntity.notFound().build();
    }
}