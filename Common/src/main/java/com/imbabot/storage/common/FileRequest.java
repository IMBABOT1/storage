package com.imbabot.storage.common;

public class FileRequest extends AbstractMessage{

    private String fileName;
    public String user;


    public void setUser(String user) {
        this.user = user;
    }



    public String getFileName(){
        return fileName;
    }


    public FileRequest(String fileName, String user){
        this.fileName = fileName;
        this.user = user;

    }
}
