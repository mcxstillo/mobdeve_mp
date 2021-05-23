package com.mobdeve.castillo.recipe_finder;

import android.net.Uri;

import java.util.ArrayList;

public class Recipe {
    public String name;
    public String creator;
    public String cuisine;
    public String serving_size;
    public int likes;
    public int dislikes;
    public float rating;
    public String difficulty;
    public String preptime;
    public String cookingtime;
    public String desc;
    public String imgUri;
    public ArrayList<Steps> steps;
    public String recipeID;

    public Recipe(){
        this.name = "";
        this.cuisine = "";
        this.creator = "";
        this.serving_size = "0";
//        this.likes = 0;
//        this.dislikes = 0;
        this.difficulty = "";
        this.preptime = "";
        this.cookingtime = "";
        this.desc = "";
    }

    public Recipe(String name, String creator,String cuisine, String serving_size, String difficulty, String preptime, String cookingtime, String desc) {
        this.name = name;
        this.creator = creator;
        this.cuisine = cuisine;
        this.serving_size = serving_size;
        this.likes = 0;
        this.dislikes = 0;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
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

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }




    public String getRecipeID(){
        return recipeID;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
