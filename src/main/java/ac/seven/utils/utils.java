package ac.seven.utils;

import ac.seven.client.PacketHandler;
import io.netty.channel.ChannelHandlerContext;

public class utils {

    public class network {

        public class server {
            public static void send(String name, Object o) {
                ac.seven.server.PacketHandler.getChannels().find(ac.seven.server.PacketHandler.id.get(name)).writeAndFlush(o);
            }
        }

        public class client {

            public static void send(Object o) {
                PacketHandler.getChannels().writeAndFlush(o);
            }
        }
    }

    public class netty {
        public interface reader {

            String name = null;

            public void read(ChannelHandlerContext ctx, Object o);

            public String getName();
        }
    }
}
