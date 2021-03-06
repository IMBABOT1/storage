package com.imbabot.storage.server;
//
import com.imbabot.storage.common.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainHandler extends ChannelInboundHandlerAdapter {

    //  private AuthManager manager;
    private static List<String> names = new ArrayList<>();
    private Server server;

    public MainHandler(Server server){
        this.server = server;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof FileRequest) {
                sendFileToClient(ctx, msg);
            }
            if (msg instanceof RequestServerFiles) {
                sendServerFilesList(ctx, msg);
            }
            if (msg instanceof FileMessage) {
                getFileFromClient(ctx, msg);
            }
            if (msg instanceof DeleteFileFromServer) {
                deleteFile(ctx, msg);
            }
            if (msg instanceof CloseConnection) {
                closeConnection(ctx, msg);
            }
            if (msg instanceof TryToAuth) {
                tryToAuth(ctx, msg, server);
            }
            if (msg instanceof ServerStorage){
                createDirectory(ctx, msg);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void createDirectory(ChannelHandlerContext ctx, Object msg){
        ServerStorage storage = (ServerStorage) msg;
        File directory = new File(storage.getStorage());
        if (!directory.exists()){
            directory.mkdir();
        }
    }


    private void sendFileToClient(ChannelHandlerContext ctx, Object msg) throws IOException {
        FileRequest fr = (FileRequest) msg;
        if (Files.exists(Paths.get("server_storage_" + fr.user + "\\" + fr.getFileName()))) {
            FileMessage fm = new FileMessage(Paths.get("server_storage_" + fr.user + "\\" +fr.getFileName()), fr.user);
            ctx.writeAndFlush(fm);
        }
    }

    private void sendServerFilesList(ChannelHandlerContext ctx, Object msg) throws IOException {
        RequestServerFiles files = (RequestServerFiles) msg;
        List<String> list = new ArrayList<>();
        Files.list(Paths.get("server_Storage_" + files.getUser())).map(path -> path.getFileName().toString()).forEach(o -> list.add(o));
        ServerFiles serverFiles = new ServerFiles(list);
        ctx.writeAndFlush(serverFiles);
    }

    private void getFileFromClient(ChannelHandlerContext ctx, Object msg) throws IOException {
        FileMessage fm = (FileMessage) msg;
        Files.write(Paths.get("server_storage_" + fm.getName() + "\\" + fm.getFileName()), fm.getData());
    }

    private void deleteFile(ChannelHandlerContext ctx, Object msg) throws IOException {
        DeleteFileFromServer delete = (DeleteFileFromServer) msg;
        Files.delete(Paths.get("server_storage_" + delete.getUserName() + "\\" + delete.getName()));
    }

    private void closeConnection(ChannelHandlerContext ctx, Object msg) throws IOException {
        CloseConnection cs = (CloseConnection) msg;
        Iterator<String> iter = names.iterator();
        while (iter.hasNext()) {
            if (iter.next().equals(cs.getName())) {
                iter.remove();
            }
        }
        ctx.writeAndFlush(cs);
        ctx.close();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void tryToAuth(ChannelHandlerContext ctx, Object msg, Server server) {
        TryToAuth auth = (TryToAuth) msg;
        String name = server.getManager().getNickNameByLoginAndPassword(auth.getLogin(), auth.getPassword());
        if (!names.contains(name) && name != null) {
            names.add(name);
            AuthName authName = new AuthName();
            authName.setName(name);
            ctx.writeAndFlush(authName);
        } else if (names.contains(name) && name != null) {
            System.out.println("Username is busy");
        }
    }
}