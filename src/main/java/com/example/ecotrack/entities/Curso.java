package com.example.ecotrack.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity

public class Curso {

    public Curso() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descriçao;

    @ManyToMany(mappedBy = "cursos")
    private Set<Usuario> alunos = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "usuariocadastro_id", nullable = true)
    private Usuario usuarioCadastro;

    public void adicionarAluno (Usuario usuario){
        this.alunos.add(usuario);
        usuario.getCursos().add(this);
    }

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

    public String getDescriçao() {
        return descriçao;
    }

    public Set<Usuario> getAlunos() {
        return alunos;
    }

    public void setAlunos(Set<Usuario> alunos) {
        this.alunos = alunos;
    }

    public Usuario getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(Usuario usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public void setDescriçao(String descriçao) {
        this.descriçao = descriçao;


    }
}
