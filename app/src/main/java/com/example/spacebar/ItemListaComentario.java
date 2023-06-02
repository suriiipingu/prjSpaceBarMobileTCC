package com.example.spacebar;

public class ItemListaComentario {

    private String nome, login, data, texto;
    private byte[] iconImagem;

    private int postId;

    public ItemListaComentario(String nome, String login, String data, String texto, byte[] iconImagem) {
        this.nome = nome;
        this.login= login;
        this.data = data;
        this.texto = texto;
        this.iconImagem = iconImagem;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getData() {
        return data;
    }

    public void setId(int postId) {
        this.postId = postId;
    }
    public int getId() {
        return postId;
    }

    public String getTexto() {
        return texto;
    }

    public byte[] getIconImagem() {
        return iconImagem;
    }


}
