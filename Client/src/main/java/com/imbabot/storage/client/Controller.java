package com.imbabot.storage.client;


import com.imbabot.storage.common.AbstractMessage;
import com.imbabot.storage.common.FileMessage;
import com.imbabot.storage.common.FileRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    ListView<String> clientList;

    @FXML
    ListView<String> serverList;

    private Path clientStorage = Paths.get("client_Storage/");
    private Path serverStorage = Paths.get("server_Storage/");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         Network.start(8189);
         Thread t = new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     while (true){
                         AbstractMessage msg = Network.readObj();
                         if (msg instanceof FileMessage){
                             FileMessage fm =  (FileMessage) msg;
                             Files.write(Paths.get("client_storage/" + fm.getFileName()), fm.getData());
                         }
                     }
                 }catch (ClassNotFoundException | IOException e){
                     e.printStackTrace();
                 }finally {
                     Network.stop();
                 }
             }
         });
         t.setDaemon(true);
         t.start();

        refreshClientList();
    }

    public void sendFile(){

    }

    public void deleteFileFromClient(){

    }

    public void refreshClientList(){
        try {
            clientList.getItems().clear();
            Files.list(clientStorage).map(path -> path.getFileName().toString()).forEach(o -> clientList.getItems().add(o));
        } catch (IOException e) {
            e.printStackTrace();
        }
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