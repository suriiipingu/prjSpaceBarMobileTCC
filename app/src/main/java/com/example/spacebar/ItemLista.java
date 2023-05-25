package com.example.spacebar;

public class ItemLista {
    private int imagemResId;
    private String texto;

    public ItemLista(int imagemResId, String texto) {
        this.imagemResId = imagemResId;
        this.texto = texto;
    }

    public int getImagemResId() {
        return imagemResId;
    }

    public String getTexto() {
        return texto;
    }
}
