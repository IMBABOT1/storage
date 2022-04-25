package com.imbabot.storage.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileMessage extends AbstractMessage {

    private String fileName;
    private byte[] data;
    private String name;



    public String getFileName(){
        return fileName;
    }
    public byte[] getData(){
        return data;
    }
    public String getName(){
        return name;
    }





    public FileMessage(Path path, String name) throws IOException {
        fileName = path.getFileName().toString();
        data = Files.readAllBytes(path);
        this.name = name;

    }
}