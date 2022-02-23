package com.imbabot.storage.client;


import com.imbabot.storage.common.*;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import sun.misc.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.*;

public class Controller implements Initializable {


    @FXML
    ListView<String> clientList;

    @FXML
    ListView<String> serverList;

    @FXML
    Button loginButton;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passField;

    @FXML
    Button  sendFile, deleteFromClient, closeSession,  downloadFile, deleteFromServer;

    @FXML
    HBox loginBox;

    private ClientHandler clientHandler;
    private String nickName;
    public String getName(){
        return nickName;
    }
    private boolean authenticated;

    private String path;

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
//        loginBox.setVisible(!authenticated);
//        loginBox.setManaged(!authenticated);
        passField.setVisible(!authenticated);
        passField.setManaged(!authenticated);
        loginField.setVisible(!authenticated);
        loginField.setManaged(!authenticated);
        clientList.setVisible(authenticated);
        clientList.setManaged(authenticated);
        serverList.setVisible(authenticated);
        serverList.setManaged(authenticated);
        loginButton.setVisible(!authenticated);
        loginButton.setManaged(!authenticated);
        sendFile.setVisible(authenticated);
        sendFile.setManaged(authenticated);
        deleteFromClient.setVisible(authenticated);
        deleteFromClient.setManaged(authenticated);
        deleteFromServer.setVisible(authenticated);
        deleteFromServer.setManaged(authenticated);
        closeSession.setVisible(authenticated);
        closeSession.setManaged(authenticated);
        downloadFile.setVisible(authenticated);
        downloadFile.setManaged(authenticated);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientHandler = new ClientHandler(this);
        setAuthenticated(false);

        try {
            CountDownLatch cdl = new CountDownLatch(1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Network.getInstance().start(cdl);
                }
            }).start();
            cdl.await();
        }catch (InterruptedException e){
            throw new RuntimeException("Network is not available");
        }

        FileRegion fileRegion = new DefaultFileRegion()


//        Network.start(8189);
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (true) {
//                        AbstractMessage msg = Network.readObj();
//                        if (msg instanceof AuthName){
//                            setAuthenticated(true);
//                            createDirectory(msg);
//                            String path = "client_storage_" + ((AuthName) msg).getName();
//                            AuthName name = new AuthName();
//                            nickName = name.getName();
//                            refreshClientList();
//                            refreshServerList();
//                            break;
//                        }
//                    }
//                    while (true) {
//                        while (true) {
//                            AbstractMessage msg = Network.readObj();
//                            if (msg instanceof FileMessage) {
//                                clientHandler.downloadFile(msg);
//                            }
//                            if (msg instanceof ServerFiles) {
//                                clientHandler.getServerFiles(msg);
//                            }
//                            if (msg instanceof CloseConnection) {
//                                clientHandler.closeConnection(msg);
//                            }
//                        }
//                    }
//                } catch (ClassNotFoundException | IOException e) {
//                    e.printStackTrace();
//                }finally {
//                    Network.stop();
//                }
//            }
//        });
//        t.setDaemon(true);
//        t.start();
//
//
//
//        refreshClientList();
//        refreshServerList();

    }

    private void createDirectory(AbstractMessage msg) throws IOException{

        if (!Files.exists(Paths.get("client_storage_" + ((AuthName) msg).getName()))){
            Files.createDirectory(Paths.get("client_storage_" + ((AuthName) msg).getName()));
        }

        String temp = "server_storage_" + ((AuthName) msg).getName();
        ServerStorage storage = new ServerStorage();
        storage.setStorage(temp);
        Network.sendMsg(storage);
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
        refreshClientList();
    }

    public void refreshClientList(){
        try {
            clientList.getItems().clear();
            Files.list(Paths.get("client_storage/")).map(path -> path.getFileName().toString()).forEach(o -> clientList.getItems().add(o));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSession(){
        Network.sendMsg(new CloseConnection());
    }

    public void downloadFile(){
        Network.sendMsg(new FileRequest(serverList.getSelectionModel().getSelectedItem()));
    }

    public void deleteFileFromServer(){
        Network.sendMsg(new DeleteFileFromServer(serverList.getSelectionModel().getSelectedItem()));
        refreshServerList();
    }

    public void refreshServerList(){
        Network.sendMsg(new RequestServerFiles());
    }

    public void tryToAuth(ActionEvent actionEvent) {
        Network.sendMsg(new TryToAuth(loginField.getText(), passField.getText()));
        loginField.clear();
        passField.clear();
    }
}