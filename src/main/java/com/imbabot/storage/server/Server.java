package com.imbabot.storage.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8189)){
            System.out.println("Server is running waiting for clients...");
            Socket socket = serverSocket.accept();
            System.out.println("Client is connect");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());


            Cat cat = null;
            try {
                cat = (Cat) in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println(cat);


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}