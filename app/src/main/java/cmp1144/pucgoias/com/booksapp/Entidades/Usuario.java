package cmp1144.pucgoias.com.booksapp.Entidades;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import cmp1144.pucgoias.com.booksapp.DAO.ConfiguracaoFireBase;

/**
 * Created by vinic on 22/11/2017.
 */

public class Usuario {

    private String id;
    private String email;
    private String senha;
    private String nome;
    private String sobrenome;
    private String dataNascimento;
    private String sexo;
    private String telefone;
    private Short exibirEmail;
    private Short exibirNotificacoes;

    public Usuario() {
    }

    public void salvar() {
        DatabaseReference referencia = ConfiguracaoFireBase.getFireBase();
        referencia.child("usuario").child(String.valueOf(getId())).setValue(this);
    }

    /**
     * Transforma objeto usuário num HashMap
     * @return
     */
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("senha", getSenha());
        hashMapUsuario.put("nome", getNome());
        hashMapUsuario.put("sobrenome", getSobrenome());
        hashMapUsuario.put("dataNascimento", getDataNascimento());
        hashMapUsuario.put("sexo", getSexo());
        hashMapUsuario.put("telefone", getTelefone());

        return hashMapUsuario;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Short getExibirEmail() {
        return exibirEmail;
    }

    public void setExibirEmail(Short exibirEmail) {
        this.exibirEmail = exibirEmail;
    }

    public Short getExibirNotificacoes() {
        return exibirNotificacoes;
    }

    public void setExibirNotificacoes(Short exibirNotificacoes) {
        this.exibirNotificacoes = exibirNotificacoes;
    }
}
