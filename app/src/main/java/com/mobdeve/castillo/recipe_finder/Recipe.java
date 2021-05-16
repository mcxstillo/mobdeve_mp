package com.mobdeve.castillo.recipe_finder;

public class Recipe {
    public String name;
    public String cuisine;
    public String serving_size;
    public float rating;
    public String difficulty;
    public String preptime;

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
}
