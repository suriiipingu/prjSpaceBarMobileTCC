package com.example.spacebar;

public class ItemLista {

    private String titulo, data, NomeUsuario, Login;
    private byte[] iconImagem;

    public ItemLista(String titulo, String data, String NomeUsuario, String Login, byte[] iconImagem) {
        this.titulo = titulo;
        this.data = data;
        this.NomeUsuario = NomeUsuario;
        this.Login = Login;
        this.iconImagem = iconImagem;

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
}
