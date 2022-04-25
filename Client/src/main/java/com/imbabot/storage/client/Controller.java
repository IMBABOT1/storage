package com.imbabot.storage.client;


import com.imbabot.storage.common.*;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        setAuthenticated(false);

        Network.start(8189);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        AbstractMessage msg = Network.readObj();
                        if (msg instanceof AuthName){
                            setAuthenticated(true);
                            nickName = ((AuthName) msg).getName();
                            String path = "client_storage_" + nickName;
                            checkAndCreateDirectories();
                            if (authenticated) {
                                refreshServerList();
                                refreshClientList();
                            }
                            break;
                        }
                    }
                    while (true) {
                        AbstractMessage msg = Network.readObj();
                        if (msg instanceof FileMessage) {
                            downloadFile(msg);
                        }
                        if (msg instanceof ServerFiles) {
                            getServerFiles(msg);
                        }
                        if (msg instanceof CloseConnection) {
                                closeConnection(msg);
                        }
                    }
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }finally {
                    Network.stop();
                }
            }
        });
        t.setDaemon(true);
        t.start();

        if (authenticated) {
            refreshServerList();
            refreshClientList();
        }
    }

    private void checkAndCreateDirectories() throws IOException{
        if (!Files.exists(Paths.get("client_storage_" + nickName))) {
            Files.createDirectory(Paths.get("client_storage_" + nickName));
        }
        if (!Files.exists(Paths.get("server_storage_" + nickName))) {
            Files.createDirectory(Paths.get("server_storage_" + nickName));
        }
    }


    void getServerFiles(AbstractMessage msg) throws ClassNotFoundException, IOException {
        ServerFiles sf = (ServerFiles) msg;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                serverList.getItems().clear();
                for (String s : sf.getList()) {
                    serverList.getItems().add(s);
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
                refreshClientList();
            }
        });
    }


    public void sendFile()throws IOException {
        if (Files.exists(Paths.get("client_storage_" + nickName + "\\" + clientList.getSelectionModel().getSelectedItem()))){
            FileMessage fm = new FileMessage(Paths.get("client_storage_" + nickName + "\\" + clientList.getSelectionModel().getSelectedItem()), nickName);
            Network.sendMsg(fm);
            refreshServerList();
        }
    }

    public void deleteFileFromClient() throws IOException{
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Files.delete(Paths.get("client_storage_" + nickName + "\\" + clientList.getSelectionModel().getSelectedItem()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                refreshClientList();
            }
        });

    }

    public void refreshClientList(){
        try {
            clientList.getItems().clear();
            Files.list(Paths.get("client_storage_" + nickName)).map(path -> path.getFileName().toString()).forEach(o -> clientList.getItems().add(o));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSession(){
        Network.sendMsg(new CloseConnection(this.nickName));
    }

    public void downloadFile(){
        Network.sendMsg(new FileRequest(serverList.getSelectionModel().getSelectedItem(), nickName));
    }

    public void deleteFileFromServer(){
        Network.sendMsg(new DeleteFileFromServer(serverList.getSelectionModel().getSelectedItem()));
        refreshServerList();
    }

    public void refreshServerList(){
        if (authenticated) {
            System.out.println(nickName);
            Network.sendMsg(new RequestServerFiles(nickName));
        }
    }

    public void tryToAuth(ActionEvent actionEvent) {
        Network.sendMsg(new TryToAuth(loginField.getText(), passField.getText()));
        loginField.clear();
        passField.clear();
    }

    private void closeConnection(AbstractMessage message){
        System.exit(0);
    }
}