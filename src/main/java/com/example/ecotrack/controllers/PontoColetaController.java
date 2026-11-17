package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.PontoColetaRequest;
import com.example.ecotrack.DTO.PontoColetaResponse;
import com.example.ecotrack.entities.PontoColeta;
import com.example.ecotrack.repository.PontoColetaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pontoscoleta")
@Tag(
        name = "Controlador dos Pontos de Coleta",
        description = "Cria, edita, lista e deleta os pontos de coleta do sistema. Cada ponto de coleta possui um código único, e apenas uma cidade atribuída para si."
)
public class PontoColetaController {

    @Autowired
    private PontoColetaRepository pontoColetaRepository;

    @PostMapping
    @Operation(
            summary = "Cadastrar novo Ponto de Coleta",
            description = "Operação responsável por salvar novos pontos de coleta no Banco de Dados."
    )
    public ResponseEntity<PontoColetaResponse> CadastrarPontoColeta (@RequestBody PontoColetaRequest pontoColetaRequest){
        PontoColeta pontoBanco = new PontoColeta();
        pontoBanco.setCidadePonto(pontoColetaRequest.getCidadePonto());
        pontoBanco.setUfPonto(pontoColetaRequest.getUfPonto());
        pontoBanco.setCodPonto(pontoColetaRequest.getCodPonto());

        pontoColetaRepository.save(pontoBanco);
        return ResponseEntity.ok(new PontoColetaResponse(pontoBanco.getIdPonto(),"Ponto de Coleta Registrado!"));
    }

}
