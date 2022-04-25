package com.imbabot.storage.common;

public class CloseConnection extends AbstractMessage{
    private String name;


    public String getName(){
        return name;
    }

    public CloseConnection(String name){
        this.name = name;
    }
}
