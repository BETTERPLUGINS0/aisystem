package ac.grim.grimac.events.packets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerInitializeWorldBorder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWorldBorder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWorldBorderCenter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWorldBorderSize;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayWorldBorderLerpSize;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.utils.worldborder.BorderExtent;
import ac.grim.grimac.utils.worldborder.RealTimeMovingBorderExtent;
import ac.grim.grimac.utils.worldborder.StaticBorderExtent;
import ac.grim.grimac.utils.worldborder.TickBasedMovingBorderExtent;
import lombok.Generated;

public class PacketWorldBorder extends Check implements PacketCheck {
   private double centerX;
   private double centerZ;
   private double absoluteMaxSize;
   private BorderExtent extent = new StaticBorderExtent(5.999997E7D);
   private static final boolean SERVER_TICK_BASED;

   public PacketWorldBorder(GrimPlayer playerData) {
      super(playerData);
   }

   public double getCurrentDiameter() {
      return this.extent.size();
   }

   public double getMinX() {
      return this.extent.getMinX(this.centerX, this.absoluteMaxSize);
   }

   public double getMaxX() {
      return this.extent.getMaxX(this.centerX, this.absoluteMaxSize);
   }

   public double getMinZ() {
      return this.extent.getMinZ(this.centerZ, this.absoluteMaxSize);
   }

   public double getMaxZ() {
      return this.extent.getMaxZ(this.centerZ, this.absoluteMaxSize);
   }

   public void tickBorder() {
      this.extent = this.extent.tick();
   }

   public void onPacketSend(PacketSendEvent event) {
      double oldDiameter;
      double newDiameter;
      long speed;
      double newDiameter;
      long speed;
      int portalTeleportBoundary;
      double oldDiameter;
      if (event.getPacketType() == PacketType.Play.Server.WORLD_BORDER) {
         WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder(event);
         this.player.sendTransaction();
         if (packet.getAction() == WrapperPlayServerWorldBorder.WorldBorderAction.SET_SIZE) {
            oldDiameter = packet.getRadius();
            this.player.addRealTimeTaskNow(() -> {
               this.setSize(oldDiameter);
            });
         } else if (packet.getAction() == WrapperPlayServerWorldBorder.WorldBorderAction.LERP_SIZE) {
            oldDiameter = packet.getOldRadius();
            newDiameter = packet.getNewRadius();
            speed = packet.getSpeed();
            this.player.addRealTimeTaskNow(() -> {
               this.setLerp(oldDiameter, newDiameter, speed);
            });
         } else if (packet.getAction() == WrapperPlayServerWorldBorder.WorldBorderAction.SET_CENTER) {
            oldDiameter = packet.getCenterX();
            newDiameter = packet.getCenterZ();
            this.player.addRealTimeTaskNow(() -> {
               this.setCenter(oldDiameter, newDiameter);
            });
         } else if (packet.getAction() == WrapperPlayServerWorldBorder.WorldBorderAction.INITIALIZE) {
            oldDiameter = packet.getCenterX();
            newDiameter = packet.getCenterZ();
            oldDiameter = packet.getOldRadius();
            newDiameter = packet.getNewRadius();
            speed = packet.getSpeed();
            portalTeleportBoundary = packet.getPortalTeleportBoundary();
            this.player.addRealTimeTaskNow(() -> {
               this.setCenter(oldDiameter, newDiameter);
               this.setLerp(oldDiameter, newDiameter, speed);
               this.absoluteMaxSize = (double)portalTeleportBoundary;
            });
         }
      }

      if (event.getPacketType() == PacketType.Play.Server.INITIALIZE_WORLD_BORDER) {
         this.player.sendTransaction();
         WrapperPlayServerInitializeWorldBorder packet = new WrapperPlayServerInitializeWorldBorder(event);
         oldDiameter = packet.getX();
         newDiameter = packet.getZ();
         oldDiameter = packet.getOldDiameter();
         newDiameter = packet.getNewDiameter();
         speed = packet.getSpeed();
         portalTeleportBoundary = packet.getPortalTeleportBoundary();
         this.player.addRealTimeTaskNow(() -> {
            this.setCenter(oldDiameter, newDiameter);
            this.setLerp(oldDiameter, newDiameter, speed);
            this.absoluteMaxSize = (double)portalTeleportBoundary;
         });
      }

      if (event.getPacketType() == PacketType.Play.Server.WORLD_BORDER_CENTER) {
         this.player.sendTransaction();
         WrapperPlayServerWorldBorderCenter packet = new WrapperPlayServerWorldBorderCenter(event);
         oldDiameter = packet.getX();
         newDiameter = packet.getZ();
         this.player.addRealTimeTaskNow(() -> {
            this.setCenter(oldDiameter, newDiameter);
         });
      }

      if (event.getPacketType() == PacketType.Play.Server.WORLD_BORDER_SIZE) {
         this.player.sendTransaction();
         double size = (new WrapperPlayServerWorldBorderSize(event)).getDiameter();
         this.player.addRealTimeTaskNow(() -> {
            this.setSize(size);
         });
      }

      if (event.getPacketType() == PacketType.Play.Server.WORLD_BORDER_LERP_SIZE) {
         this.player.sendTransaction();
         WrapperPlayWorldBorderLerpSize packet = new WrapperPlayWorldBorderLerpSize(event);
         oldDiameter = packet.getOldDiameter();
         newDiameter = packet.getNewDiameter();
         speed = packet.getSpeed();
         this.player.addRealTimeTaskNow(() -> {
            this.setLerp(oldDiameter, newDiameter, speed);
         });
      }

   }

   @Contract(
      mutates = "this"
   )
   private void setCenter(double x, double z) {
      this.centerX = x;
      this.centerZ = z;
   }

   @Contract(
      mutates = "this"
   )
   private void setSize(double size) {
      this.extent = new StaticBorderExtent(size);
   }

   @Contract(
      mutates = "this"
   )
   private void setLerp(double oldDiameter, double newDiameter, long speed) {
      if (speed > 0L && oldDiameter != newDiameter) {
         this.extent = this.createMovingExtent(oldDiameter, newDiameter, speed);
      } else {
         this.extent = new StaticBorderExtent(newDiameter);
      }

   }

   private BorderExtent createMovingExtent(double from, double to, long speed) {
      long durationMs;
      if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_11)) {
         durationMs = SERVER_TICK_BASED ? speed : speed / 50L;
         return new TickBasedMovingBorderExtent(from, to, durationMs);
      } else {
         durationMs = SERVER_TICK_BASED ? speed * 50L : speed;
         return new RealTimeMovingBorderExtent(from, to, durationMs);
      }
   }

   @Generated
   public double getCenterX() {
      return this.centerX;
   }

   @Generated
   public double getCenterZ() {
      return this.centerZ;
   }

   @Generated
   public double getAbsoluteMaxSize() {
      return this.absoluteMaxSize;
   }

   @Generated
   public BorderExtent getExtent() {
      return this.extent;
   }

   static {
      SERVER_TICK_BASED = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11);
   }
}
