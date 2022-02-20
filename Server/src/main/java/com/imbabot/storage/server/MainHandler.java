package com.imbabot.storage.server;
//
import com.imbabot.storage.common.FileMessage;
import com.imbabot.storage.common.FileRequest;
import com.imbabot.storage.common.RequestServerFiles;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MainHandler extends ChannelInboundHandlerAdapter {


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
