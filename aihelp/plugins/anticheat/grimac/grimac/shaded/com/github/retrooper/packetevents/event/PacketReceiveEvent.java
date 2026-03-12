package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.exception.PacketProcessException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.PacketSide;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PacketReceiveEvent extends ProtocolPacketEvent {
   protected PacketReceiveEvent(Object channel, User user, @UnknownNullability Object player, Object rawByteBuf, boolean autoProtocolTranslation) throws PacketProcessException {
      super(PacketSide.CLIENT, channel, user, player, rawByteBuf, autoProtocolTranslation);
   }

   protected PacketReceiveEvent(int packetID, PacketTypeCommon packetType, ServerVersion serverVersion, Object channel, User user, @UnknownNullability Object player, Object byteBuf) throws PacketProcessException {
      super(packetID, packetType, serverVersion, channel, user, player, byteBuf);
   }

   public void call(PacketListenerCommon listener) {
      listener.onPacketReceive(this);
   }

   public PacketReceiveEvent clone() {
      Object clonedBuffer = ByteBufHelper.retainedDuplicate(this.getByteBuf());
      return new PacketReceiveEvent(this.getPacketId(), this.getPacketType(), this.getServerVersion(), this.getChannel(), this.getUser(), this.getPlayer(), clonedBuffer);
   }
}
