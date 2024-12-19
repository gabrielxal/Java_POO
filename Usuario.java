package com.mycompany.unidadepoo;

public class Usuario {
    private int id; 
    private String nome;
    private String senha; 
    private String dataNascimento;
    private String projeto;

    public Usuario(String nome, String senha, String dataNascimento, String projeto) {
        this.nome = nome;
        this.senha = senha; 
        this.dataNascimento = dataNascimento;
        this.projeto = projeto;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getProjeto() {
        return projeto;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha; 
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setProjeto(String projeto) {
        this.projeto = projeto;
    }
}
