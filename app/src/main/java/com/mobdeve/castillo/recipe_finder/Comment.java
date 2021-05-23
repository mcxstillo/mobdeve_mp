package com.mobdeve.castillo.recipe_finder;

public class Comment {
    public User user;
    public String comment;

    public Comment(User user, String comment) {
        this.user = user;
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
