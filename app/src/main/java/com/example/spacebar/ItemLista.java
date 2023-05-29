package com.example.spacebar;

public class ItemLista {

    private String titulo, data, NomeUsuario, Login;
    private byte[] iconImagem;
    private boolean curtido;

    private int postId;

    public ItemLista(String titulo, String data, String NomeUsuario, String Login, byte[] iconImagem) {
        this.titulo = titulo;
        this.data = data;
        this.NomeUsuario = NomeUsuario;
        this.Login = Login;
        this.iconImagem = iconImagem;
        this.curtido = false;

    }

    public byte[] getIconImagem() {
        return iconImagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getData() {
        return data;
    }

    public String getNome() {
        return NomeUsuario;
    }
    public String getLogin() {
        return Login;
    }



    public int getId() {
        return postId;
    }

    public void setId(int postId) {
        this.postId = postId;
    }

}
