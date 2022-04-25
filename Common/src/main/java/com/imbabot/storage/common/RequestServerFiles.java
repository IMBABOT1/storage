package com.imbabot.storage.common;

public class RequestServerFiles extends AbstractMessage {



    public RequestServerFiles(String user) {
        this.user = user;
    }

    private String user;

    public String getUser(){
        return user;
    }

}
