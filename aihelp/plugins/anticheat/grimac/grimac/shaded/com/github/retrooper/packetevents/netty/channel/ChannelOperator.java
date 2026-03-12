package ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel;

import java.net.SocketAddress;
import java.util.List;

public interface ChannelOperator {
   SocketAddress remoteAddress(Object channel);

   SocketAddress localAddress(Object channel);

   boolean isOpen(Object channel);

   Object close(Object channel);

   Object write(Object channel, Object buffer);

   Object flush(Object channel);

   Object writeAndFlush(Object channel, Object buffer);

   Object fireChannelRead(Object channel, Object buffer);

   Object writeInContext(Object channel, String ctx, Object buffer);

   Object flushInContext(Object channel, String ctx);

   Object writeAndFlushInContext(Object channel, String ctx, Object buffer);

   Object fireChannelReadInContext(Object channel, String ctx, Object buffer);

   List<String> pipelineHandlerNames(Object channel);

   Object getPipelineHandler(Object channel, String name);

   Object getPipelineContext(Object channel, String name);

   Object getPipeline(Object channel);

   void runInEventLoop(Object channel, Runnable runnable);

   Object pooledByteBuf(Object channel);
}
