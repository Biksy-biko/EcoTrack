package com.example.ecotrack.DTO;

import com.example.ecotrack.entities.Curso;
import com.example.ecotrack.entities.Usuario;

import java.util.List;

public class CursoConsultaResponse {

    public CursoConsultaResponse() {
    }

    public CursoConsultaResponse(Curso curso) {
        this.id = curso.getId();
        this.titulo = curso.getTitulo();
        this.descricao = curso.getDescriçao();
        this.matriculas = curso.getAlunos().stream().map(UsuarioConsultaResponse::new).toList();
    }

    private Long id;
    private String titulo;
    private String descricao;
    private List<UsuarioConsultaResponse> matriculas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<UsuarioConsultaResponse> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<UsuarioConsultaResponse> matriculas) {
        this.matriculas = matriculas;
    }
}
