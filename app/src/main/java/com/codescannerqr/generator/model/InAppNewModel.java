package com.codescannerqr.generator.model;

public class InAppNewModel {

    private int iconItem;
    private int titleID;
    private String descID;
    private boolean visibleTitle;

    public InAppNewModel(int iconItem, int titleID, String descID, boolean visibleTitle) {
        this.iconItem = iconItem;
        this.titleID = titleID;
        this.descID = descID;
        this.visibleTitle = visibleTitle;
    }

    public int getIconItem() {
        return iconItem;
    }

    public int getTitleID() {
        return titleID;
    }

    public String getDescID() {
        return descID;
    }

    public void setDescID(String descID) {
        this.descID = descID;
    }

}
