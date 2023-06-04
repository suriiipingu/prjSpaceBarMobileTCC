package com.example.spacebar;

public class ItemLista {

    private String titulo, data, texto, NomeUsuario, Login;
    private byte[] iconImagem, postImagem;

    private int postId;

    //sem imagem e sem texto
    public ItemLista(String titulo, String data, String texto, String NomeUsuario, String Login, byte[] iconImagem, byte[] postImg) {
        this.titulo = titulo;
        this.data = data;
        this.texto = texto;
        this.NomeUsuario = NomeUsuario;
        this.Login = Login;
        this.iconImagem = iconImagem;
        this.postImagem = postImg;


    }



    public byte[] getIconImagem() {
        return iconImagem;
    }

    public byte[] getPostImagem() {
        return postImagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getData() {
        return data;
    }

    public String getTexto() {
        return texto;
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
