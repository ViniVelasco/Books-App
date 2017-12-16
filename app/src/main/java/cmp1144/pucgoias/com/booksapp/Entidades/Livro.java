package cmp1144.pucgoias.com.booksapp.Entidades;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vinic on 27/11/2017.
 */
public class Livro {
    private String uid;
    private String ISBN;
    private String titulo;
    private String autor;
    private String editora;
    private Integer edicao;
    private String idUsuario;
    private String assunto;
    private String capaBase64;
    private String emailProprietario;
    private String telefoneProprietario;

    /**
     * Transforma objeto livro num HashMap
     * @return
     */
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapLivro = new HashMap<>();

        hashMapLivro.put("uid", getUid());
        hashMapLivro.put("ISBN", getISBN());
        hashMapLivro.put("titulo", getTitulo());
        hashMapLivro.put("autor", getAutor());
        hashMapLivro.put("editora", getEditora());
        hashMapLivro.put("edicao", getEdicao());
        hashMapLivro.put("idUsuario", getIdUsuario());
        hashMapLivro.put("assunto", getAssunto());
        hashMapLivro.put("capaBase64", getCapaBase64());
        hashMapLivro.put("emailProprietario", getEmailProprietario());

        return hashMapLivro;

    }

    public String getCapaBase64() {
        return capaBase64;
    }

    public void setCapaBase64(String capaBase64) {
        this.capaBase64 = capaBase64;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Integer getEdicao() {
        return edicao;
    }

    public void setEdicao(Integer edicao) {
        this.edicao = edicao;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmailProprietario() {
        return emailProprietario;
    }

    public void setEmailProprietario(String emailProprietario) {
        this.emailProprietario = emailProprietario;
    }

    public String getTelefoneProprietario() {
        return telefoneProprietario;
    }

    public void setTelefoneProprietario(String telefoneProprietario) {
        this.telefoneProprietario = telefoneProprietario;
    }
}
