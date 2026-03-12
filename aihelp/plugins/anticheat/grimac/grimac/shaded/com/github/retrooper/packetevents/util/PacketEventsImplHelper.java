package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserDisconnectEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.PacketSide;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.UUID;

public final class PacketEventsImplHelper {
   private PacketEventsImplHelper() {
   }

   @Nullable
   public static ProtocolPacketEvent handlePacket(Object channel, User user, Object player, Object buffer, boolean autoProtocolTranslation, PacketSide side) throws Exception {
      return (ProtocolPacketEvent)(side == PacketSide.SERVER ? handleClientBoundPacket(channel, user, player, buffer, autoProtocolTranslation) : handleServerBoundPacket(channel, user, player, buffer, autoProtocolTranslation));
   }

   @Nullable
   public static PacketSendEvent handleClientBoundPacket(Object channel, User user, Object player, Object buffer, boolean autoProtocolTranslation) throws Exception {
      if (!ByteBufHelper.isReadable(buffer)) {
         return null;
      } else {
         int preProcessIndex = ByteBufHelper.readerIndex(buffer);
         PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(channel, user, player, buffer, autoProtocolTranslation);
         int processIndex = ByteBufHelper.readerIndex(buffer);
         PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> {
            ByteBufHelper.readerIndex(buffer, processIndex);
         }, !autoProtocolTranslation);
         if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
               ByteBufHelper.clear(buffer);
               packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
               packetSendEvent.getLastUsedWrapper().write();
            } else {
               ByteBufHelper.readerIndex(buffer, preProcessIndex);
            }
         } else {
            ByteBufHelper.clear(buffer);
         }

         if (packetSendEvent.hasPostTasks()) {
            Iterator var8 = packetSendEvent.getPostTasks().iterator();

            while(var8.hasNext()) {
               Runnable task = (Runnable)var8.next();
               task.run();
            }
         }

         return packetSendEvent;
      }
   }

   @Nullable
   public static PacketReceiveEvent handleServerBoundPacket(Object channel, User user, Object player, Object buffer, boolean autoProtocolTranslation) throws Exception {
      if (!ByteBufHelper.isReadable(buffer)) {
         return null;
      } else {
         int preProcessIndex = ByteBufHelper.readerIndex(buffer);
         PacketReceiveEvent packetReceiveEvent = EventCreationUtil.createReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
         int processIndex = ByteBufHelper.readerIndex(buffer);
         PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent, () -> {
            ByteBufHelper.readerIndex(buffer, processIndex);
         }, !autoProtocolTranslation);
         if (!packetReceiveEvent.isCancelled()) {
            if (packetReceiveEvent.getLastUsedWrapper() != null) {
               ByteBufHelper.clear(buffer);
               packetReceiveEvent.getLastUsedWrapper().writeVarInt(packetReceiveEvent.getPacketId());
               packetReceiveEvent.getLastUsedWrapper().write();
            } else {
               ByteBufHelper.readerIndex(buffer, preProcessIndex);
            }
         } else {
            ByteBufHelper.clear(buffer);
         }

         if (packetReceiveEvent.hasPostTasks()) {
            Iterator var8 = packetReceiveEvent.getPostTasks().iterator();

            while(var8.hasNext()) {
               Runnable task = (Runnable)var8.next();
               task.run();
            }
         }

         return packetReceiveEvent;
      }
   }

   public static void handleDisconnection(Object channel, @Nullable UUID uuid) {
      synchronized(channel) {
         ProtocolManager protocolManager = PacketEvents.getAPI().getProtocolManager();
         User user = protocolManager.getUser(channel);
         if (user != null) {
            UserDisconnectEvent disconnectEvent = new UserDisconnectEvent(user);
            PacketEvents.getAPI().getEventManager().callEvent(disconnectEvent);
            protocolManager.removeUser(user.getChannel());
         }

         if (uuid == null) {
            protocolManager.removeChannel(channel);
         } else {
            protocolManager.removeChannelById(uuid);
         }

      }
   }
}
