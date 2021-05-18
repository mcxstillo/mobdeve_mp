package com.mobdeve.castillo.recipe_finder;

import android.net.Uri;

import java.util.ArrayList;

public class Recipe {
    public String name;
    public String cuisine;
    public String serving_size;
    public float rating;
    public String difficulty;
    public String preptime;
    public String cookingtime;
    public String desc;
    public String imgUri;
    public ArrayList<Steps> steps;

    public Recipe(){
        this.name = "";
        this.cuisine = "";
        this.serving_size = "0";
        this.rating = 0;
        this.difficulty = "";
        this.preptime = "";
        this.cookingtime = "";
        this.desc = "";
    }

    public Recipe(String name, String cuisine, String serving_size, float rating, String difficulty, String preptime, String cookingtime, String desc) {
        this.name = name;
        this.cuisine = cuisine;
        this.serving_size = serving_size;
        this.rating = rating;
        this.difficulty = difficulty;
        this.preptime = preptime;
        this.cookingtime = cookingtime;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getServing_size() {
        return serving_size;
    }

    public void setServing_size(String serving_size) {
        this.serving_size = serving_size;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getPreptime() {
        return preptime;
    }

    public void setPreptime(String preptime) {
        this.preptime = preptime;
    }

    public String getCookingtime() {
        return cookingtime;
    }

    public void setCookingtime(String cookingtime) {
        this.cookingtime = cookingtime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public void setSteps(ArrayList<Steps> steps) {
        this.steps = steps;
    }
}
