package com.imbabot.storage.client;

import com.imbabot.storage.common.AbstractMessage;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.*;
import java.net.Socket;

public class Network {
    private static Socket socket;
    private static ObjectEncoderOutputStream out;
    private static ObjectDecoderInputStream in;


    public static void start(int port){
        try {
            socket = new Socket("localhost", port);
            in = new ObjectDecoderInputStream(socket.getInputStream());
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static boolean sendMsg(AbstractMessage message){
        try {
            out.writeObject(message);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static AbstractMessage readObj() throws ClassNotFoundException, IOException {
        AbstractMessage msg = (AbstractMessage) in.readObject();
        return msg;
    }

    public static void stop(){
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}