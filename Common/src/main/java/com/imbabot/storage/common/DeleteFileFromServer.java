package com.imbabot.storage.common;

public class DeleteFileFromServer extends AbstractMessage{

    private String name;
    private String userName;

    public String getName(){
        return name;
    }
    public String getUserName(){
        return userName;
    }

    public DeleteFileFromServer(String name, String userName){
        this.name = name;
        this.userName = userName;
    }
}
