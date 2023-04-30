package ac.seven.client;

import ac.seven.utils.utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class PacketHandler extends SimpleChannelInboundHandler<Object> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final String Name;
    private utils.netty.reader reader;
    public PacketHandler(String name, utils.netty.reader reader) {
        this.Name = name;
        this.reader = reader;
    }

    public static ChannelGroup getChannels() {
        return channels;
    }

    @Override
    public void channelActive(final ChannelHandlerContext channelHandlerContext) {
        channels.add(channelHandlerContext.channel());

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws InterruptedException {
        cause.printStackTrace();
        ctx.close().sync();
    }

    private boolean loaded = false;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if(!loaded) {
            String tempname = (String) o;
            loaded = true;
            this.reader = this.reader.newInstance(tempname);
            System.out.println(tempname);
            return;
        }
        this.reader.read(channelHandlerContext, o);
    }
}
