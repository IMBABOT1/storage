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

    void getServerFiles(AbstractMessage msg) throws ClassNotFoundException, IOException {
        ServerFiles sf = (ServerFiles) msg;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.serverList.getItems().clear();
                for (String s : sf.getList()) {
                     controller.serverList.getItems().add(s);
                }
            }
        });
    }

    void downloadFile(AbstractMessage msg)  {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FileMessage fm = (FileMessage) msg;
                try {
                    Files.write(Paths.get("client_storage/" + fm.getFileName()), fm.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                controller.refreshClientList();
            }
        });
    }

    void closeConnection(AbstractMessage msg){
        System.exit(0);
    }
}
