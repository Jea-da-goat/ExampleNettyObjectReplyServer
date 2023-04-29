package ac.seven.client;


import ac.seven.utils.utils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;

public class NettyClient {
    public String HOST;// = System.getProperty("host", "127.0.0.1");
    public  int PORT;// = Integer.parseInt(System.getProperty("port", "8992"));

    private Channel channel;
    private final String Name;

    public Channel getConnection() {
        return channel;
    }

    public void send(HashMap<Integer, Object> stream) {
        channel.writeAndFlush(stream);
    }

    public NettyClient(String Name, String host, int port) {
        this.HOST = host;
        this.PORT = port;
        this.Name = Name;
    }

    public void run(utils.netty.reader reader) throws Exception {

        //final SslContext sslCtx = SslContextBuilder.forClient()
               // .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            while(true) {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new StreamInitializer(reader, this.HOST, this.PORT, this.Name));


                // Start the connection attempt.
                channel = b.connect(this.HOST, this.PORT).sync().channel();


                // Read commands from the stdin
                //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

                while (channel.isOpen()) {
                    Thread.sleep(30000);
                }
            }
            //while (!Main.ShutDown) onSpinWait();
            /*for (;;) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                // Sends the received line to the server.
                lastWriteFuture = channel.writeAndFlush(line + "\r\n");

                // If user typed the 'bye' command, wait until the server closes
                // the connection.
                if ("bye".equals(line.toLowerCase())) {
                    channel.closeFuture().sync();
                    break;
                }
            }*/

            // Wait until all messages are flushed before closing the channel.
        } finally {
            // The connection is closed automatically on shutdown.
            //group.shutdownGracefully();
        }
    }

}
