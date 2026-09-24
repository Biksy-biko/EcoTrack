package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.EmpresaRequest;
import com.example.ecotrack.DTO.EmpresaResponse;
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

    @GetMapping
    public List<Empresa> ListarEmpresas (){

        return empresaRepository.findAll();
    }
}