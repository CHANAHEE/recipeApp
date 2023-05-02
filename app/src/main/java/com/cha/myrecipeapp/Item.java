package com.cha.myrecipeapp;

import android.graphics.Bitmap;

import java.net.URI;

public class Item {
    int index;
    String mainImg;
    String title;
    String hash;
    String recipe_text;
    String recipe_ingre;
    String name;
    int icon;
    Boolean isBookmarkSelected;
    public Item() {
    }


    public Item(String name,int icon){
        this.name = name;
        this.icon = icon;

    }
    public Item(int index, String mainImg, String title, String hash, String recipe_text, String recipe_ingre, Boolean isBookmarkSelected) {
        this.index = index;
        this.mainImg = mainImg;
        this.title = title;
        this.hash = hash;
        this.recipe_text = recipe_text;
        this.recipe_ingre = recipe_ingre;
        this.isBookmarkSelected = isBookmarkSelected;
    }
}
