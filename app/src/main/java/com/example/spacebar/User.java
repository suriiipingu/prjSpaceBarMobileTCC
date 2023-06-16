package com.example.spacebar;

public class User {
    private String name,login;

    private int userID, seguidores;

    private byte[] iconResId;

    private boolean hasSeguido;



    public User(int UserID, String name, String login, byte[] iconResId, int seguidores) {
        this.userID = UserID;
        this.name = name;
        this.login = login;
        this.iconResId = iconResId;
        this.seguidores = seguidores;
    }
    public boolean hasSeguido() {
        return hasSeguido;
    }

    public void setHasSeguido(boolean hasSeguido) {
        this.hasSeguido = hasSeguido;
    }

    public String getName() {
        return name;
    }

    public int getUserID(){ return  userID;}


    public String getLogin() {
        return login;
    }
    public byte[] getIconResId() {
        return iconResId;
    }

    public int getSeguidores(){ return seguidores;}

}
