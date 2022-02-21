package com.imbabot.storage.common;

public class ServerStorage extends AbstractMessage {

    private String name;

    public String getStorage(){
        return name;
    }

    public void setStorage(String name){
        this.name = name;
    }


}
