package com.example.ecotrack.controllers;


import com.example.ecotrack.DTO.*;
import com.example.ecotrack.entities.Curso;
import com.example.ecotrack.repository.CursoRepository;
import com.example.ecotrack.repository.UsuarioRepository;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<CursoResponse> cadastrarCurso(@RequestBody CursoRequest cursoRequest){
        Curso cursoBanco = new Curso();

        cursoBanco.setTitulo(cursoRequest.getTitulo());
        cursoBanco.setDescriçao(cursoRequest.getDescricao());

        cursoRepository.save(cursoBanco);

        return ResponseEntity.ok(new CursoResponse(cursoBanco.getId(), "Curso Criado!"));
    }

    @PostMapping("/matricula")
    public ResponseEntity<CursoResponse> matricula (@RequestBody MatriculaRequest matriculaRequest){

        var usuarioBanco = usuarioRepository.findById(matriculaRequest.getUsuario_id()).orElse(null);

        var cursoBanco = cursoRepository.findById(matriculaRequest.getCurso_id()).orElse(null);

        if(usuarioBanco == null || cursoBanco == null){
            return ResponseEntity.notFound().build();
        }

        if(cursoBanco.getAlunos().contains(usuarioBanco)){
            throw new IllegalArgumentException("Aluno já cadastrado nesse curso!");
        }

        cursoBanco.adicionarAluno(usuarioBanco);

        cursoRepository.save(cursoBanco);

        return ResponseEntity.ok(new CursoResponse(cursoBanco.getId(),"Curso cadastrado com sucesso."));
    }

    @GetMapping
    public ResponseEntity<List<CursoConsultaResponse>> listarTodos(){

        var listaCurso = cursoRepository.findAll().stream().map(CursoConsultaResponse::new).toList();

        return ResponseEntity.ok(listaCurso);

    }
}
