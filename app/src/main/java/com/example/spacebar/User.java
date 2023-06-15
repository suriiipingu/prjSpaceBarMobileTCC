package com.example.spacebar;

public class User {
    private String name,login;

    private int userID;

    private byte[] iconResId;

    public User(int UserID, String name, String login, byte[] iconResId) {
        this.userID = UserID;
        this.name = name;
        this.login = login;
        this.iconResId = iconResId;
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
