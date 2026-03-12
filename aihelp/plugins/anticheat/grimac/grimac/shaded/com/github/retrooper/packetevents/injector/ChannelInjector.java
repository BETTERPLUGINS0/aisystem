package ac.grim.grimac.shaded.com.github.retrooper.packetevents.injector;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.PacketSide;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;

public interface ChannelInjector {
   default boolean isServerBound() {
      return true;
   }

   void inject();

   void uninject();

   void updateUser(Object channel, User user);

   void setPlayer(Object channel, Object player);

   boolean isPlayerSet(Object channel);

   boolean isProxy();

   default PacketSide getPacketSide() {
      return PacketSide.SERVER;
   }
}
