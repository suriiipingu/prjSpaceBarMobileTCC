package com.example.spacebar;

public class User {
    private String name,login;

    private int userID;

    private byte[] iconResId;

    private boolean hasSeguido;



    public User(int UserID, String name, String login, byte[] iconResId) {
        this.userID = UserID;
        this.name = name;
        this.login = login;
        this.iconResId = iconResId;
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
}
