package com.studio.luciano.agendapmba.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.studio.luciano.agendapmba.config.ConfiguracaoFirebase;
import com.studio.luciano.agendapmba.helper.UnidadeFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Unidade implements Serializable {

    private String id;
    private String nomeUnidade;
    private String nomeComandante;
    private String nomeSubcomandante;
    private String outroNome;
    private String emailUnidade;
    private String telefoneComandante;
    private String telefoneSubcomandante;
    private String telefone3;
    private String telefoneFixo;
    private String foto;
    private String endereco;
    private String bairro;
    private String cidade;
    private String funcaoC;
    private String funcaoSC;
    private String funcao3;

    public Unidade() {

        //Cria o id automaticamente pelo próprio Firebase
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference unidadeRef = database.child("unidade");
        String idUnidade = unidadeRef.push().getKey();//O push gera um código e o getKey recupera
        setId(idUnidade);

    }

    public void salvarUnidade(){

        DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference unidadeRef = firebaseref.child("unidade");

        unidadeRef.child(getId()).setValue(this);//Usando o this ele salva todos os dados configurados no usuário
    }

    public void remover(){

        DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference unidadeRef = firebaseref.child("unidade");

        unidadeRef.child(getId()).removeValue();//Remover o item
    }

    public void atualizarUnidade(){

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference unidadeRef = database.child("unidade")
                .child(getId());

        Map<String, Object> valoresUnidade = converterMap();

        unidadeRef.updateChildren(valoresUnidade);
    }

    @Exclude
    public Map<String, Object> converterMap(){
        HashMap<String, Object> unidadeMap = new HashMap<>();
        unidadeMap.put("nomeUnidade", getNomeUnidade());
        unidadeMap.put("nomeCmd", getNomeComandante());
        unidadeMap.put("nomeScmd", getNomeSubcomandante());
        unidadeMap.put("nomeOutro", getOutroNome());
        unidadeMap.put("telefoneComandante", getTelefoneComandante());
        unidadeMap.put("telefoneSubcomandante", getTelefoneSubcomandante());
        unidadeMap.put("telefoneFixo", getTelefoneFixo());
        unidadeMap.put("telefone3", getTelefone3());
        unidadeMap.put("emailUnidade", getEmailUnidade());
        unidadeMap.put("foto", getFoto());

        return unidadeMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeUnidade() {
        return nomeUnidade;
    }

    public void setNomeUnidade(String nomeUnidade) {
        this.nomeUnidade = nomeUnidade;
    }

    public String getNomeComandante() {
        return nomeComandante;
    }

    public void setNomeComandante(String nomeComandante) {
        this.nomeComandante = nomeComandante;
    }

    public String getNomeSubcomandante() {
        return nomeSubcomandante;
    }

    public void setNomeSubcomandante(String nomeSubcomandante) {
        this.nomeSubcomandante = nomeSubcomandante;
    }

    public String getOutroNome() {
        return outroNome;
    }

    public void setOutroNome(String outroNome) {
        this.outroNome = outroNome;
    }

    public String getEmailUnidade() {
        return emailUnidade;
    }

    public void setEmailUnidade(String emailUnidade) {
        this.emailUnidade = emailUnidade;
    }

    public String getTelefoneComandante() {
        return telefoneComandante;
    }

    public void setTelefoneComandante(String telefoneComandante) {
        this.telefoneComandante = telefoneComandante;
    }

    public String getTelefoneSubcomandante() {
        return telefoneSubcomandante;
    }

    public void setTelefoneSubcomandante(String telefoneSubcomandante) {
        this.telefoneSubcomandante = telefoneSubcomandante;
    }

    public String getTelefone3() {
        return telefone3;
    }

    public void setTelefone3(String telefone3) {
        this.telefone3 = telefone3;
    }

    public String getTelefoneFixo() {
        return telefoneFixo;
    }

    public void setTelefoneFixo(String telefoneFixo) {
        this.telefoneFixo = telefoneFixo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getFuncaoC() {
        return funcaoC;
    }

    public void setFuncaoC(String funcaoC) {
        this.funcaoC = funcaoC;
    }

    public String getFuncaoSC() {
        return funcaoSC;
    }

    public void setFuncaoSC(String funcaoSC) {
        this.funcaoSC = funcaoSC;
    }

    public String getFuncao3() {
        return funcao3;
    }

    public void setFuncao3(String funcao3) {
        this.funcao3 = funcao3;
    }
}
