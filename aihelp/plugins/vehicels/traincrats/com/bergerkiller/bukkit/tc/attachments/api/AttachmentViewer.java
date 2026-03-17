package com.bergerkiller.bukkit.tc.attachments.api;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.controller.VehicleMountController;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.player.network.PlayerClientSynchronizer;
import com.bergerkiller.bukkit.tc.controller.player.network.PlayerPacketListener;
import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.NetworkInterface;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawnHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLivingHandle;
import java.util.Iterator;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public interface AttachmentViewer extends TrainCarts.Provider {
   Player getPlayer();

   default String getName() {
      return this.getPlayer().getName();
   }

   TrainCarts getTrainCarts();

   VehicleMountController getVehicleMountController();

   NetworkInterface getSmoothCoastersNetwork();

   void send(CommonPacket var1);

   void send(PacketHandle var1);

   void sendSilent(CommonPacket var1);

   void sendSilent(PacketHandle var1);

   default void sendEntityLivingSpawnPacket(PacketPlayOutSpawnEntityLivingHandle packet, DataWatcher metadata) {
      if (packet.hasDataWatcherSupport()) {
         packet.setDataWatcher(metadata);
         this.send((PacketHandle)packet);
      } else {
         this.send((PacketHandle)packet);
         this.send((PacketHandle)PacketPlayOutEntityMetadataHandle.createNew(packet.getEntityId(), metadata, true));
      }

   }

   default void sendNamedEntitySpawnPacket(PacketPlayOutNamedEntitySpawnHandle packet, DataWatcher metadata) {
      if (packet.hasDataWatcherSupport()) {
         packet.setDataWatcher(metadata);
         this.send((PacketHandle)packet);
      } else {
         this.send((PacketHandle)packet);
         this.send((PacketHandle)PacketPlayOutEntityMetadataHandle.createNew(packet.getEntityId(), metadata, true));
      }

   }

   default void sendDisableCollision(UUID entityUUID) {
      this.getTrainCarts().getTeamProvider().noCollisionTeam().join(this, entityUUID);
   }

   default void sendDisableCollision(Iterable<UUID> entityUUIDs) {
      this.getTrainCarts().getTeamProvider().noCollisionTeam().join(this, entityUUIDs);
   }

   default boolean isValid() {
      return this.getPlayer().isValid();
   }

   default boolean isConnected() {
      Player player = this.getPlayer();
      return player.isValid() || Bukkit.getPlayer(player.getUniqueId()) == player;
   }

   default PlayerClientSynchronizer getClientSynchronizer() {
      return this.getTrainCarts().getPlayerClientSynchronizerProvider().forPlayer(this.getPlayer());
   }

   default <L extends PacketListener> PlayerPacketListener<L> createPacketListener(L packetListener, PacketType... packetTypes) {
      TrainCarts trainCarts = this.getTrainCarts();
      return trainCarts.getPlayerPacketListenerProvider().create(this.getPlayer(), packetListener, packetTypes);
   }

   default int getEntityId() {
      return this.getPlayer().getEntityId();
   }

   default boolean evaluateGameVersion(String operand, String rightSide) {
      return PlayerUtil.evaluateGameVersion(this.getPlayer(), operand, rightSide);
   }

   default boolean supportsDisplayEntities() {
      return CommonCapabilities.HAS_DISPLAY_ENTITY && this.evaluateGameVersion(">=", "1.19.4");
   }

   default boolean supportsDisplayEntityLocationInterpolation() {
      return CommonCapabilities.HAS_DISPLAY_ENTITY_LOCATION_INTERPOLATION && this.evaluateGameVersion(">=", "1.20.2");
   }

   default boolean supportRelativeRotationUpdate() {
      return (Common.evaluateMCVersion("<", "1.21.2") || Common.evaluateMCVersion(">=", "1.21.9")) && (this.evaluateGameVersion("<", "1.21.2") || this.evaluateGameVersion(">=", "1.21.9"));
   }

   default double getArmorStandButtOffset() {
      return this.evaluateGameVersion(">=", "1.20.2") ? 0.0D : 0.27D;
   }

   default void resetGlowColor(UUID entityUUID) {
      this.getTrainCarts().getGlowColorTeamProvider().reset(this, entityUUID);
   }

   default void updateGlowColor(UUID entityUUID, ChatColor color) {
      this.getTrainCarts().getGlowColorTeamProvider().update(this, entityUUID, color);
   }

   default void updateGlowColor(Iterable<UUID> entityUUIDs, ChatColor color) {
      this.getTrainCarts().getGlowColorTeamProvider().update(this, entityUUIDs, color);
   }

   static AttachmentViewer forPlayer(Player player) {
      TrainCarts trainCarts = TrainCarts.plugin;
      return trainCarts != null && trainCarts.isEnabled() ? trainCarts.getAttachmentViewer(player) : fallback(player);
   }

   static AttachmentViewer fallback(final Player player) {
      return new AttachmentViewer() {
         public TrainCarts getTrainCarts() {
            return TrainCarts.plugin;
         }

         public Player getPlayer() {
            return player;
         }

         public VehicleMountController getVehicleMountController() {
            return PlayerUtil.getVehicleMountController(player);
         }

         public NetworkInterface getSmoothCoastersNetwork() {
            return null;
         }

         public void send(CommonPacket packet) {
            PacketUtil.sendPacket(player, packet);
         }

         public void send(PacketHandle packet) {
            PacketUtil.sendPacket(player, packet);
         }

         public void sendSilent(CommonPacket packet) {
            PacketUtil.sendPacket(player, packet, false);
         }

         public void sendSilent(PacketHandle packet) {
            PacketUtil.sendPacket(player, packet, false);
         }

         public int hashCode() {
            return player.hashCode();
         }

         public boolean equals(Object o) {
            if (o == this) {
               return true;
            } else if (o instanceof AttachmentViewer) {
               return ((AttachmentViewer)o).getPlayer() == player;
            } else {
               return false;
            }
         }
      };
   }

   static Iterable<AttachmentViewer> fallbackIterable(Iterable<Player> players) {
      return () -> {
         return new Iterator<AttachmentViewer>() {
            private final Iterator<Player> baseIter = players.iterator();

            public boolean hasNext() {
               return this.baseIter.hasNext();
            }

            public AttachmentViewer next() {
               return AttachmentViewer.fallback((Player)this.baseIter.next());
            }

            public void remove() {
               this.baseIter.remove();
            }

            public void forEachRemaining(Consumer<? super AttachmentViewer> action) {
               this.baseIter.forEachRemaining((p) -> {
                  action.accept(AttachmentViewer.fallback(p));
               });
            }
         };
      };
   }
}
