package implementation.v1_21_R3.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import net.minecraft.server.MinecraftServer;

public final class PacketWritingUtil {
   private static final StreamCodec<ByteBuf, Packet<? super PacketListenerPlayOut>> CLIENTBOUND_CODEC;

   public static void compressAndWriteToPipelines(Packet var0, Collection<ChannelPipeline> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ChannelPipeline var3 = (ChannelPipeline)var2.next();
         compressAndWriteToPipeline(var0, var3);
      }

   }

   public static void compressAndWriteToPipeline(Packet var0, ChannelPipeline var1) {
      PacketDataSerializer var2 = new PacketDataSerializer(Unpooled.buffer());
      CLIENTBOUND_CODEC.encode(var2, var0);
      var1.write(var2);
      var1.flush();
   }

   static {
      MinecraftServer var0 = MinecraftServer.getServer();
      CLIENTBOUND_CODEC = GameProtocols.b.a(RegistryFriendlyByteBuf.a(var0.ba())).c();
   }
}
