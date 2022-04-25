package com.imbabot.storage.common;

public class FileRequest extends AbstractMessage{

    private String fileName;
<<<<<<< HEAD
    public String user;


    public void setUser(String user) {
        this.user = user;
    }

=======
>>>>>>> d231f1215b4c7892af64ab32083b02099f331595


    public String getFileName(){
        return fileName;
    }


<<<<<<< HEAD
    public FileRequest(String fileName, String user){
        this.fileName = fileName;
        this.user = user;
=======
    public FileRequest(String fileName){
        this.fileName = fileName;
>>>>>>> d231f1215b4c7892af64ab32083b02099f331595

    }
}
