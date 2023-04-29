package ac.seven.server;

import ac.seven.utils.utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class PacketHandler extends SimpleChannelInboundHandler<Object> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ChannelGroup getChannels() {
        return channels;
    }

    public static HashMap<String, ChannelId> id = new HashMap<>();
    public static HashMap<ChannelId, String> id2 = new HashMap<>();

    private static String toIP(String address) {
        return address.split("/")[1].split(":")[0];
    }

    private static ArrayList<String> Whitelist = new ArrayList<>();

    private final utils.netty.reader reader;

    private static Boolean isWhitelisted(SocketAddress ip) {
        String onlyIP = toIP(ip.toString());
        return Whitelist.contains(onlyIP);
    }

    public PacketHandler(utils.netty.reader reader) {
        this.reader = reader;
    }

    @Override
    public void channelActive(final ChannelHandlerContext channelHandlerContext) {
        try {
            if(channelHandlerContext.channel().remoteAddress().toString() == null) {
                channelHandlerContext.close();
            } else {
                //id.put(channelHandlerContext.channel().id().toString(), channelHandlerContext.channel().id());
                channels.add(channelHandlerContext.channel());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        String name = (String) o;
        id.put(name, ctx.channel().id());
        id2.put(ctx.channel().id(), name);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws InterruptedException {
        cause.printStackTrace();
        ctx.close().sync();
    }
}
