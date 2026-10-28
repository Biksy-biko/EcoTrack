package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.*;
import com.example.ecotrack.entities.Entrega;
import com.example.ecotrack.entities.Material;
import com.example.ecotrack.entities.Usuario;
import com.example.ecotrack.repository.EntregaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/entregas")
@Tag(
        name = "Controlador de Entregas",
        description = "Classe responsável por todo o gerenciamento, desde o cadastro até o cancelamento, das entregas do sistema."
)
public class EntregaController {

    @Autowired
    private EntregaRepository entregaRepository;

    @GetMapping("/{id}")
    @Operation(
            summary = "Consultar entrega por ID",
            description = "Retorna uma string contendo o ID da entrega solicitada via parâmetro de caminho."
    )
    public String EntregaId (@PathVariable Long id){
        return "ID de entrega:" + id;
    }

    @GetMapping
    @Operation(
            summary = "Consultar todas as entregas",
            description = "Retorna uma lista completa contendo todas as entregas cadastradas no repositório."
    )
    public List<Entrega> ConsultarEtrega(){

        return entregaRepository.findAll();
    }

    @PostMapping
    @Operation(
            summary = "Registrar nova entrega",
            description = "Cadastra uma nova entrega validando o tempo estimado. Se o tempo for no passado, retorna um erro de requisição; caso contrário, salva a entrega com status 'Pendente'."
    )
    public ResponseEntity<CadastrarEntregaResponse> RegistrarEntrega (@RequestBody CadastrarEntregaRequest entregaRequest) {

        Entrega entregaBanco = new Entrega();
        entregaBanco.setCidadeOrigem(entregaRequest.getCidadeOrigem());
        entregaBanco.setUfOrigem(entregaRequest.getUfOrigem());
        entregaBanco.setCidadeDestino(entregaRequest.getCidadeDestino());
        entregaBanco.setUfDestino(entregaRequest.getUfDestino());
        entregaRequest.setTempoEstimado((LocalDateTime.now().plusHours(2)));

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
    @Operation(
            summary = "Cancelar entrega",
            description = "Busca uma entrega pelo ID e altera o seu status para 'Cancelada', salvando a alteração no banco de dados."
    )
    public ResponseEntity<AtualizarEntregaResponse> CancelarEntrega (@PathVariable Long id){

            Entrega entregaBanco = entregaRepository.findById(id).orElse(null);

            if (entregaBanco != null){
                entregaBanco.setStatus("Cancelada");
                entregaRepository.save(entregaBanco);
                return ResponseEntity.ok().build();

            }return ResponseEntity.notFound().build();
        }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Concluir entrega",
            description = "Atualiza o status de uma entrega existente para 'Entregue', registra o horário atual da entrega e salva os dados."
    )
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

