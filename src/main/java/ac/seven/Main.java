package ac.seven;

import ac.seven.client.NettyClient;
import ac.seven.server.NettyServer;
import ac.seven.server.PacketHandler;
import ac.seven.utils.utils;
import io.netty.channel.ChannelHandlerContext;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello world!");

        new Thread(() -> {
            try {
                NettyServer server = new NettyServer(12230);
                server.run();
            } catch (InterruptedException | CertificateException | SSLException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(1000);
        new Thread(() -> {
            for(int i = 1; i <= 5; i++) {
                int finalI = i;
                new Thread(() -> {
                    try {
                        NettyClient client = new NettyClient( "server" + finalI, "localhost", 12230);
                        utils.netty.reader reader = new utils.netty.reader() {
                            @Override
                            public void read(ChannelHandlerContext ctx, Object o) {
                                System.out.println("client");
                                System.out.println(o);
                                System.out.println(getName());
                            }

                            @Override
                            public String getName() {
                                return "config.name";
                            }
                        };
                        client.run(reader);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }).start();
        Thread.sleep(1000);
        PacketHandler.getChannels().writeAndFlush("ask");

    }
}
