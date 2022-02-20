package com.imbabot.storage.common;

public class AuthName extends AbstractMessage{

    private String name;

    public String getName(){
        return name;
    }

    public AuthName(String name){
        this.name = name;
    }

}
