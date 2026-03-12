package ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.player;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface PlayerManager {
   int getPing(@NotNull Object player);

   @NotNull
   ClientVersion getClientVersion(@NotNull Object player);

   Object getChannel(@NotNull Object player);

   User getUser(@NotNull Object player);

   @ApiStatus.Obsolete
   default ConnectionState getConnectionState(@NotNull Object player) throws IllegalStateException {
      return this.getUser(player).getConnectionState();
   }

   default void sendPacket(@NotNull Object player, @NotNull Object byteBuf) {
      PacketEvents.getAPI().getProtocolManager().sendPacket(this.getChannel(player), byteBuf);
   }

   default void sendPacket(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().sendPacket(this.getChannel(player), wrapper);
   }

   default void sendPacketSilently(@NotNull Object player, @NotNull Object byteBuf) {
      PacketEvents.getAPI().getProtocolManager().sendPacketSilently(this.getChannel(player), byteBuf);
   }

   default void sendPacketSilently(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().sendPacketSilently(this.getChannel(player), wrapper);
   }

   default void writePacket(@NotNull Object player, @NotNull Object byteBuf) {
      PacketEvents.getAPI().getProtocolManager().writePacket(this.getChannel(player), byteBuf);
   }

   default void writePacket(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().writePacket(this.getChannel(player), wrapper);
   }

   default void writePacketSilently(@NotNull Object player, @NotNull Object byteBuf) {
      PacketEvents.getAPI().getProtocolManager().writePacketSilently(this.getChannel(player), byteBuf);
   }

   default void writePacketSilently(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().writePacketSilently(this.getChannel(player), wrapper);
   }

   default void receivePacket(Object player, Object byteBuf) {
      PacketEvents.getAPI().getProtocolManager().receivePacket(this.getChannel(player), byteBuf);
   }

   default void receivePacket(Object player, PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().receivePacket(this.getChannel(player), wrapper);
   }

   default void receivePacketSilently(Object player, Object byteBuf) {
      PacketEvents.getAPI().getProtocolManager().receivePacketSilently(this.getChannel(player), byteBuf);
   }

   default void receivePacketSilently(Object player, PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().receivePacketSilently(this.getChannel(player), wrapper);
   }
}
