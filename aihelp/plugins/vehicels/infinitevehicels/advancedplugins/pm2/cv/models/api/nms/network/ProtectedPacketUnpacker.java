package advancedplugins.pm2.cv.models.api.nms.network;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class ProtectedPacketUnpacker extends ChannelDuplexHandler {
   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) {
      if (var2 instanceof ProtectedPacket) {
         ProtectedPacket var4 = (ProtectedPacket)var2;
         var2 = var4.packet();
      }

      super.write(var1, var2, var3);
   }
}
