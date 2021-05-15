package com.mobdeve.castillo.recipe_finder;

public class Recipe {
    public String name;
    public float rating;
    public String difficulty;
    public String preptime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
