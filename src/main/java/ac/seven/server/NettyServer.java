package ac.seven.server;


import ac.seven.utils.utils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class NettyServer {


    private ServerBootstrap bootstrap;
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException, CertificateException, SSLException {
        //SelfSignedCertificate ssc = new SelfSignedCertificate();
        //SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
         //       .build();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            utils.netty.reader reader = new utils.netty.reader() {
                @Override
                public void read(ChannelHandlerContext ctx, Object o) {
                    System.out.println("====");
                    System.out.println(o);
                    System.out.println(getName());
                }

                @Override
                public String getName() {
                    return "config.Name";
                }
            };
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new StreamInitializer(reader));

            b.bind(port).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        /*bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()
                )
        );

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline p = Channels.pipeline(
                        new ObjectEncoder(),
                        new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())),
                        new StreamInitializer()
                );
                return p;
            }
        });

        bootstrap.bind(new InetSocketAddress(port));*/
    }


}
