package com.imbabot.storage.common;

public class DeleteFileFromServer extends AbstractMessage{

    private String name;

    public String getName(){
        return name;
    }

    public DeleteFileFromServer(String name){
        this.name = name;
    }
}
