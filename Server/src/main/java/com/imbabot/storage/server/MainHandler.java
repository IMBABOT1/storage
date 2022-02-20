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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private Path serverStorage = Paths.get("server_Storage/");


    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        try {
            if (msg instanceof FileRequest){
                FileRequest fr = (FileRequest) msg;

                if (Files.exists(Paths.get("server_storage/" + fr.getFileName()))){
                    FileMessage fm = new FileMessage(Paths.get("server_storage/" + fr.getFileName()));
                    ctx.writeAndFlush(fm);
                }
            }
            if (msg instanceof RequestServerFiles) {
                List<String> list = new ArrayList<>();
                Files.list(serverStorage).map(path -> path.getFileName().toString()).forEach(o -> list.add(o));
                ServerFiles serverFiles = new ServerFiles(list);
                ctx.writeAndFlush(serverFiles);
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
