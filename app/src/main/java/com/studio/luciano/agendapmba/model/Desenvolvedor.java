package com.studio.luciano.agendapmba.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.studio.luciano.agendapmba.config.ConfiguracaoFirebase;

public class Desenvolvedor {

    private String id;
    private String nome;
    private String email;
    private String codigo;
    private String senha;
    private String foto;

    public Desenvolvedor() {
    }

    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference desenvolvedor = firebaseRef.child("desenvolvedor").child(getId());

        desenvolvedor.setValue(this);//Usando o this ele salva todos os dados configurados no usuário
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
