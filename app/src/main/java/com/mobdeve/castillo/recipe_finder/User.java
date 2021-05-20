package com.mobdeve.castillo.recipe_finder;

public class User {

    public String name, email,desc,userID;


    public User(){

    }
    public User(String email,String name,String desc){
        this.email = email;
        this.name = name;
        this.desc = desc;

    }

    public User(String email,String name,String desc, String userID){
        this.email = email;
        this.name = name;
        this.desc = desc;
        this.userID = userID;

    }



    public User(String name,String desc){
        this.name = name;
        this.desc = desc;

    }



    //getters
    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getDesc(){
        return this.desc;
    }

    public String getUserID(){
        return this.userID;
    }


    //setters
    public void setName(String newName){
        this.name = newName;
    }

    public void setEmail(String newEmail){
        this.email = newEmail;
    }

    public void setDesc(String newDesc){
        this.desc = newDesc;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

}