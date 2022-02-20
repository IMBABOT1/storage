package com.imbabot.storage.client;


import com.imbabot.storage.common.*;
import javafx.application.Platform;
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


    private ClientHandler clientHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientHandler = new ClientHandler(this);

        Network.start(8189);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        AbstractMessage msg = Network.readObj();
                        if (msg instanceof FileMessage) {
                           clientHandler.downloadFile(msg);
                        }
                        if (msg instanceof ServerFiles) {
                            clientHandler.getServerFiles(msg);
                        }
                    }
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                } finally {
                    Network.stop();
                }
            }
        });
        t.setDaemon(true);
        t.start();

        refreshClientList();
        refreshServerList();
    }


    public void sendFile()throws IOException {
        if (Files.exists(Paths.get("client_storage/" + clientList.getSelectionModel().getSelectedItem()))){
            FileMessage fm = new FileMessage(Paths.get("client_storage/" + clientList.getSelectionModel().getSelectedItem()));
            Network.sendMsg(fm);
            refreshServerList();
        }
    }

    public void deleteFileFromClient() throws IOException{
        Files.delete(Paths.get("client_storage/" + clientList.getSelectionModel().getSelectedItem()));
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
        Network.sendMsg(new FileRequest(serverList.getSelectionModel().getSelectedItem()));
    }

    public void deleteFileFromServer(){

    }

    public void refreshServerList(){
        Network.sendMsg(new RequestServerFiles());
    }
}