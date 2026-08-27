package com.example.ecotrack.entities;

public class Usuario {

    private Long id;
    private String nome;
    private String cpf;
    private String dataNascimento;

    public Usuario() {
        this.id = 1L;
    }
    public Usuario(String nome, String cpf, String dataNascimento){
        this.id = 1000L;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public Long getId(){
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public String getDataNascimento() {
        return this.dataNascimento;
    }

    public void setNome(String nome) {
        if (this.nome.isEmpty()){
            this.nome = nome;
        }
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
