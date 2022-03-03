package com.codescannerqr.generator.model;

public class SectionList {

    private int name;
    private int image;
    private int color;

    public SectionList(int name, int image, int color) {
        this.name = name;
        this.image = image;
        this.color = color;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
