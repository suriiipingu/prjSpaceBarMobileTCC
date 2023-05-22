package com.example.spacebar;

public class Item {
    private String imageName;
    private String text;

    public Item(String imageName, String text) {
        this.imageName = imageName;
        this.text = text;
    }

    public String getImageName() {
        return imageName;
    }

    public String getText() {
        return text;
    }
}

