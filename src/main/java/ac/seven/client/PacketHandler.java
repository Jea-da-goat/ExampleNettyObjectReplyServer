package com.itndev.factions.SocketConnection.Client;

import com.itndev.factions.SocketConnection.IO.PacketProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;

public class PacketHandler extends SimpleChannelInboundHandler<Object> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ChannelGroup getChannels() {
        return channels;
    }

    @Override
    public void channelActive(final ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                (GenericFutureListener<Future<Channel>>) future -> {
                    channels.add(channelHandlerContext.channel());
                });
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws InterruptedException {
        cause.printStackTrace();
        ctx.close().sync();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        HashMap<Integer, Object> stream = (HashMap<Integer, Object>) o;
        if(stream.isEmpty()) {
            channelHandlerContext.channel().close();
        }
        new Thread(() -> PacketProcessor.run(stream)).start();
    }
}
