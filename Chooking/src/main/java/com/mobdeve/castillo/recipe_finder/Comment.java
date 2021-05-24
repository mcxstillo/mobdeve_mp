package com.mobdeve.castillo.recipe_finder;

public class Comment {
    public String user;
    public String comment;
    public String imgUri;

    public Comment(String user, String comment, String imgUri) {
        this.user = user;
        this.comment = comment;
        this.imgUri = imgUri;

    }

    public Comment() {

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

     public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }



}
