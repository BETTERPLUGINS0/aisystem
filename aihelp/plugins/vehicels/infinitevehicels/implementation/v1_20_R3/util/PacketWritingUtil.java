package implementation.v1_20_R3.util;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.network.EnumProtocol;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.Packet;

public final class PacketWritingUtil {
   public static void compressAndWriteToPipelines(Packet<?> var0, Collection<ChannelPipeline> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ChannelPipeline var3 = (ChannelPipeline)var2.next();
         compressAndWriteToPipeline(var0, var3);
      }

   }

   public static void compressAndWriteToPipeline(Packet<?> var0, ChannelPipeline var1) {
      var1.write(serialize(var0));
   }

   public static PacketDataSerializer serialize(Packet<?> var0) {
      PacketDataSerializer var1 = new PacketDataSerializer(Unpooled.buffer());
      int var2 = EnumProtocol.b.b(EnumProtocolDirection.b).a(var0);
      var1.k(var2);
      var0.a(var1);
      return var1;
   }
}
