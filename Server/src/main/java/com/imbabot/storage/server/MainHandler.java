package com.imbabot.storage.server;
//
import com.imbabot.storage.common.FileMessage;
import com.imbabot.storage.common.FileRequest;
import com.imbabot.storage.common.RequestServerFiles;
import com.imbabot.storage.common.ServerFiles;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import javafx.application.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainHandler extends ChannelInboundHandlerAdapter {




    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        try {
            if (msg instanceof FileRequest){
                sendFileToClient(ctx, msg);
            }
            if (msg instanceof RequestServerFiles) {
                List<String> list = new ArrayList<>();
                Files.list( Paths.get("server_Storage/")).map(path -> path.getFileName().toString()).forEach(o -> list.add(o));
                ServerFiles serverFiles = new ServerFiles(list);
                ctx.writeAndFlush(serverFiles);
            }

            if (msg instanceof FileMessage){
                FileMessage fm = (FileMessage) msg;
                Files.write(Paths.get("server_storage/" + fm.getFileName()), fm.getData());
            }

        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void sendFileToClient(ChannelHandlerContext ctx, Object msg) throws IOException{
        FileRequest fr = (FileRequest) msg;
        if (Files.exists(Paths.get("server_storage/" + fr.getFileName()))){
            FileMessage fm = new FileMessage(Paths.get("server_storage/" + fr.getFileName()));
            ctx.writeAndFlush(fm);
        }
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}