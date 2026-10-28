package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.EmpresaConsultaResponse;
import com.example.ecotrack.DTO.EmpresaRequest;
import com.example.ecotrack.DTO.EmpresaResponse;
import com.example.ecotrack.DTO.UsuarioConsultaResponse;
import com.example.ecotrack.entities.Empresa;
import com.example.ecotrack.repository.EmpresaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@Tag(
        name = "Empresas",
        description = "Classe de controle e CRUD de Empresas."
)
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    @Operation(
            summary = "Listar todas as empresas",
            description = "Retorna uma lista contendo todas as empresas cadastradas no sistema convertidas para o formato de consulta."
    )
    public List<EmpresaConsultaResponse> ConsultarEmpresa(){

        return empresaRepository.findAll().stream().map(EmpresaConsultaResponse::new).toList();
    }

    @GetMapping("/cnpj/{cnpj}/usuarios")
    @Operation(
            summary = "Buscar usuários por CNPJ da empresa",
            description = "Busca uma empresa pelo seu CNPJ e retorna a lista de todos os usuários vinculados a ela. Retorna 404 caso a empresa não seja encontrada."
    )
    public ResponseEntity<List<UsuarioConsultaResponse>> BuscarUsuariosPorEmpresaCnpj (@PathVariable String cnpj) {
        var empresaBanco = empresaRepository.getEmpresaByCnpj(cnpj).orElse(null);

        if (empresaBanco == null){
            return  ResponseEntity.notFound().build();
        }

        var usuarioEmpresaBanco = empresaBanco.getUsuarios().stream().map(UsuarioConsultaResponse::new).toList();
        //começa como Lista, se torna um stream do DTO, que depois vira uma Lista do DTO

        return ResponseEntity.ok(usuarioEmpresaBanco);
    }

    @PostMapping("/criar")
    @Operation(
            summary = "Cadastrar nova empresa",
            description = "Recebe os dados da empresa no corpo da requisição, realiza o cadastro no banco de dados e retorna o ID gerado junto com uma mensagem de sucesso."
    )
    public ResponseEntity<EmpresaResponse> CadastrarEmpresa (@RequestBody EmpresaRequest empresaRequest){
        Empresa empresaBanco = new Empresa();

        empresaBanco.setRazaoSocial(empresaRequest.getRazaoSocial());
        empresaBanco.setCnpj(empresaRequest.getCnpj());
        empresaBanco.setNomeFantasia(empresaRequest.getNomeFantasia());
        empresaBanco.setInscricaoEstadual(empresaRequest.getInscricaoEstadual());

        empresaRepository.save(empresaBanco);
        EmpresaResponse empresaResponse = new EmpresaResponse();

        empresaResponse.setId(empresaBanco.getId());
        empresaResponse.setMensagem("Cadastro Realizado com sucesso!");

        return ResponseEntity.ok(empresaResponse);
    }
}