package com.imbabot.storage.client;


import com.imbabot.storage.common.AbstractMessage;
import com.imbabot.storage.common.FileMessage;
import com.imbabot.storage.common.FileRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Network network;

    @FXML
    ListView clientList;

    @FXML
    ListView serverList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         network = new Network(8189);
         Thread t = new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     while (true){
                         AbstractMessage msg = network.readObj();
                         if (msg instanceof FileMessage){
                             FileMessage fm =  (FileMessage) msg;
                             Files.write(Paths.get("client_storage/" + fm.getFileName()), fm.getData());
                         }

                     }
                 }catch (ClassNotFoundException | IOException e){
                     e.printStackTrace();
                 }finally {
                     network.stop();
                 }
             }
         });
         t.setDaemon(true);
         t.start();
    }

    public void sendFile(){

    }

    public void deleteFileFromClient(){

    }

    public void refreshClientList(){

    }

    public void closeSession(){

    }

    public void downloadFile(){

    }

    public void deleteFileFromServer(){

    }

    public void refreshServerList(){

    }


}