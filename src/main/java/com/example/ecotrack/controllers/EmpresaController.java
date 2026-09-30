package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.EmpresaConsultaResponse;
import com.example.ecotrack.DTO.EmpresaRequest;
import com.example.ecotrack.DTO.EmpresaResponse;
import com.example.ecotrack.DTO.UsuarioConsultaResponse;
import com.example.ecotrack.entities.Empresa;
import com.example.ecotrack.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    public List<EmpresaConsultaResponse> ConsultarEmpresa(){

        return empresaRepository.findAll().stream().map(EmpresaConsultaResponse::new).toList();
    }

    @GetMapping("/cnpj/{cnpj}/usuarios")
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