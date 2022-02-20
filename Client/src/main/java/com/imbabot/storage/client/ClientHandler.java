package com.imbabot.storage.client;

import com.imbabot.storage.common.AbstractMessage;
import com.imbabot.storage.common.FileMessage;
import com.imbabot.storage.common.ServerFiles;
import javafx.application.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientHandler {


    private Controller controller;

    public ClientHandler(Controller controller){
        this.controller = controller;
    }

    void getServerFiles() throws ClassNotFoundException, IOException {
        AbstractMessage msg = Network.readObj();
        ServerFiles sf = (ServerFiles) msg;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (String s : sf.getList()) {
                     controller.serverList.getItems().add(s);
                }
            }
        });
    }

    void downloadFile() throws ClassNotFoundException, IOException {
        AbstractMessage msg = Network.readObj();
        FileMessage fm = (FileMessage) msg;
        Files.write(Paths.get("client_storage/" + fm.getFileName()), fm.getData());
    }

}
