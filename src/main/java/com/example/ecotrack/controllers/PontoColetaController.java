package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.PontoColetaRequest;
import com.example.ecotrack.DTO.PontoColetaResponse;
import com.example.ecotrack.entities.PontoColeta;
import com.example.ecotrack.repository.EntregaRepository;
import com.example.ecotrack.repository.PontoColetaRepository;
import com.example.ecotrack.utils.CodigoPontoColeta;
import com.example.ecotrack.utils.CodigoRastreioUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pontoscoleta")
@Tag(
        name = "Controlador dos Pontos de Coleta",
        description = "Cria, edita, lista e deleta os pontos de coleta do sistema. Cada ponto de coleta possui um código único, e apenas uma cidade atribuída para si."
)
public class PontoColetaController {

    @Autowired
    private PontoColetaRepository pontoColetaRepository;
    @Autowired
    private EntregaRepository codEntregaRepository;


    @GetMapping
    @Operation(
            summary = "Listagem dos pontos de coleta cadastrados.",
            description = "Operação responsável por listar, sem parâmetros, todos os pontos de coleta salvos no banco de dados."
    )
    public List<PontoColeta> ConsultarPontos(){
        return pontoColetaRepository.findAll();
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar novo Ponto de Coleta",
            description = "Operação responsável por salvar novos pontos de coleta no Banco de Dados."
    )
    public ResponseEntity<PontoColetaResponse> CadastrarPontoColeta (@RequestBody PontoColetaRequest pontoColetaRequest){
        PontoColeta pontoBanco = new PontoColeta();
        pontoBanco.setCidadePonto(pontoColetaRequest.getCidadePonto());
        pontoBanco.setUfPonto(pontoColetaRequest.getUfPonto());
        pontoBanco.setLogradouro(pontoColetaRequest.getLogradouro());

        String codPonto;
        do {
            codPonto = CodigoRastreioUtils.gerarCodigo();
        }while (pontoColetaRepository.existsByCodPonto(codPonto));

        codPonto = CodigoPontoColeta.gerarCodigoPontoColeta(codPonto);

        pontoBanco.setCodPonto(codPonto);

        pontoColetaRepository.save(pontoBanco);
        return ResponseEntity.ok(new PontoColetaResponse(pontoBanco.getIdPonto(),"Ponto de Coleta Registrado!"));
    }

}
