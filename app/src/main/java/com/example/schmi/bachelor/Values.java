package com.example.schmi.bachelor;

public class Values {

    private static Values instance;
    private String username;

    private Values(){

    }

    public static Values getInstance(){
        if(instance == null)
            instance = new Values();
        return instance;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
