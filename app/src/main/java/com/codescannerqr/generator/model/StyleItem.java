package com.codescannerqr.generator.model;

import android.graphics.Bitmap;

import java.util.List;

public class StyleItem {

    private final int icon;
    private String title;
    private final List<Bitmap> bitmapList;
    private String args;

    public StyleItem(int icon, String title, List<Bitmap> bitmapList, String args) {
        this.icon = icon;
        this.title = title;
        this.bitmapList = bitmapList;
        this.args = args;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }
}
