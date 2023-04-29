package ac.seven.client;

import ac.seven.utils.utils;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class StreamInitializer extends ChannelInitializer<SocketChannel> {
    //private final SslContext sslCtx;

    private final String HOST;
    private final Integer PORT;

    private final String Name;

    private final utils.netty.reader reader;

    public StreamInitializer(utils.netty.reader reader, String HOST, Integer PORT, String name) {
        this.HOST = HOST;
        this.PORT = PORT;
        //this.sslCtx = sslCtx;
        this.Name = name;
        this.reader = reader;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //pipeline.addLast(sslCtx.newHandler(ch.alloc(), this.HOST, this.PORT));
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));

        // and then business logic.
        pipeline.addLast(new PacketHandler(this.Name, this.reader));
    }
}
