package com.bergerkiller.bukkit.tc.controller.player;

import com.bergerkiller.bukkit.common.controller.VehicleMountController;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PlayerGameInfo;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.controller.player.network.PacketQueue;
import com.bergerkiller.bukkit.tc.controller.player.network.PlayerClientSynchronizer;
import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.NetworkInterface;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import org.bukkit.entity.Player;

public final class TrainCartsAttachmentViewer implements AttachmentViewer {
   private final TrainCarts plugin;
   private final Player player;
   private final VehicleMountController vmc;
   private final PlayerGameInfo playerGameInfo;
   private final double armorStandButtOffset;
   private final boolean supportsDisplayEntityLocationInterpolation;
   private final boolean supportsDisplayEntities;
   private final boolean supportRelativeRotationUpdate;
   private final PacketQueue packetQueue;
   private final PlayerClientSynchronizer playerClientSynchronizer;

   TrainCartsAttachmentViewer(TrainCarts plugin, Player player, PlayerGameInfo playerGameInfo, PacketQueue packetQueue) {
      this.plugin = plugin;
      this.player = player;
      this.vmc = PlayerUtil.getVehicleMountController(player);
      this.playerGameInfo = playerGameInfo;
      this.armorStandButtOffset = AttachmentViewer.super.getArmorStandButtOffset();
      this.supportsDisplayEntityLocationInterpolation = AttachmentViewer.super.supportsDisplayEntityLocationInterpolation();
      this.supportsDisplayEntities = AttachmentViewer.super.supportsDisplayEntities();
      this.supportRelativeRotationUpdate = AttachmentViewer.super.supportRelativeRotationUpdate();
      this.packetQueue = packetQueue;
      this.playerClientSynchronizer = plugin.getPlayerClientSynchronizerProvider().forViewer(this);
   }

   PacketQueue getPacketQueue() {
      return this.packetQueue;
   }

   public PlayerClientSynchronizer getClientSynchronizer() {
      return this.playerClientSynchronizer;
   }

   public TrainCarts getTrainCarts() {
      return this.plugin;
   }

   public Player getPlayer() {
      return this.player;
   }

   public boolean evaluateGameVersion(String operand, String rightSide) {
      return this.playerGameInfo.evaluateVersion(operand, rightSide);
   }

   public boolean supportsDisplayEntityLocationInterpolation() {
      return this.supportsDisplayEntityLocationInterpolation;
   }

   public boolean supportsDisplayEntities() {
      return this.supportsDisplayEntities;
   }

   public boolean supportRelativeRotationUpdate() {
      return this.supportRelativeRotationUpdate;
   }

   public double getArmorStandButtOffset() {
      return this.armorStandButtOffset;
   }

   public VehicleMountController getVehicleMountController() {
      return this.vmc;
   }

   public NetworkInterface getSmoothCoastersNetwork() {
      return this.packetQueue;
   }

   public void send(PacketHandle packet) {
      this.getPacketQueue().send(packet);
   }

   public void send(CommonPacket packet) {
      this.getPacketQueue().send(packet);
   }

   public void sendSilent(CommonPacket packet) {
      this.getPacketQueue().sendSilent(packet);
   }

   public void sendSilent(PacketHandle packet) {
      this.getPacketQueue().sendSilent(packet);
   }

   public int hashCode() {
      return this.player.hashCode();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (o instanceof AttachmentViewer) {
         return this.player == ((AttachmentViewer)o).getPlayer();
      } else {
         return false;
      }
   }

   public String toString() {
      return "TCAttachmentViewer{player=" + this.player.getName() + "}";
   }
}
