package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerWorldBorder extends PacketWrapper<WrapperPlayServerWorldBorder> {
   private WrapperPlayServerWorldBorder.WorldBorderAction action;
   private double radius;
   private double oldRadius;
   private double newRadius;
   private long speed;
   private double centerX;
   private double centerZ;
   private int portalTeleportBoundary;
   private int warningTime;
   private int warningBlocks;

   public WrapperPlayServerWorldBorder(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerWorldBorder(double radius) {
      super((PacketTypeCommon)PacketType.Play.Server.WORLD_BORDER);
      this.action = WrapperPlayServerWorldBorder.WorldBorderAction.SET_SIZE;
      this.radius = radius;
   }

   public WrapperPlayServerWorldBorder(double oldRadius, double newRadius, long speed) {
      super((PacketTypeCommon)PacketType.Play.Server.WORLD_BORDER);
      this.action = WrapperPlayServerWorldBorder.WorldBorderAction.LERP_SIZE;
      this.oldRadius = oldRadius;
      this.newRadius = newRadius;
      this.speed = speed;
   }

   public WrapperPlayServerWorldBorder(double centerX, double centerZ) {
      super((PacketTypeCommon)PacketType.Play.Server.WORLD_BORDER);
      this.action = WrapperPlayServerWorldBorder.WorldBorderAction.SET_CENTER;
      this.centerX = centerX;
      this.centerZ = centerZ;
   }

   public WrapperPlayServerWorldBorder(double centerX, double centerZ, double oldRadius, double newRadius, long speed, int portalTeleportBoundary, int warningTime, int warningBlocks) {
      super((PacketTypeCommon)PacketType.Play.Server.WORLD_BORDER);
      this.action = WrapperPlayServerWorldBorder.WorldBorderAction.INITIALIZE;
      this.centerX = centerX;
      this.centerZ = centerZ;
      this.oldRadius = oldRadius;
      this.newRadius = newRadius;
      this.speed = speed;
      this.portalTeleportBoundary = portalTeleportBoundary;
      this.warningTime = warningTime;
      this.warningBlocks = warningBlocks;
   }

   public WrapperPlayServerWorldBorder(int warning, boolean time) {
      super((PacketTypeCommon)PacketType.Play.Server.WORLD_BORDER);
      if (time) {
         this.action = WrapperPlayServerWorldBorder.WorldBorderAction.SET_WARNING_TIME;
         this.warningTime = warning;
      } else {
         this.action = WrapperPlayServerWorldBorder.WorldBorderAction.SET_WARNING_BLOCKS;
         this.warningBlocks = warning;
      }

   }

   public void read() {
      this.action = (WrapperPlayServerWorldBorder.WorldBorderAction)this.readEnum(WrapperPlayServerWorldBorder.WorldBorderAction.class);
      if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.SET_SIZE) {
         this.radius = this.readDouble();
      } else if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.LERP_SIZE) {
         this.oldRadius = this.readDouble();
         this.newRadius = this.readDouble();
         this.speed = this.readVarLong();
      } else if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.SET_CENTER) {
         this.centerX = this.readDouble();
         this.centerZ = this.readDouble();
      } else if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.INITIALIZE) {
         this.centerX = this.readDouble();
         this.centerZ = this.readDouble();
         this.oldRadius = this.readDouble();
         this.newRadius = this.readDouble();
         this.speed = this.readVarLong();
         this.portalTeleportBoundary = this.readVarInt();
         this.warningTime = this.readVarInt();
         this.warningBlocks = this.readVarInt();
      } else if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.SET_WARNING_TIME) {
         this.warningTime = this.readVarInt();
      } else if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.SET_WARNING_BLOCKS) {
         this.warningBlocks = this.readVarInt();
      }

   }

   public void write() {
      this.writeEnum(this.action);
      if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.SET_SIZE) {
         this.writeDouble(this.radius);
      } else if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.LERP_SIZE) {
         this.writeDouble(this.oldRadius);
         this.writeDouble(this.newRadius);
         this.writeVarLong(this.speed);
      } else if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.SET_CENTER) {
         this.writeDouble(this.centerX);
         this.writeDouble(this.centerZ);
      } else if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.INITIALIZE) {
         this.writeDouble(this.centerX);
         this.writeDouble(this.centerZ);
         this.writeDouble(this.oldRadius);
         this.writeDouble(this.newRadius);
         this.writeVarLong(this.speed);
         this.writeVarInt(this.portalTeleportBoundary);
         this.writeVarInt(this.warningTime);
         this.writeVarInt(this.warningBlocks);
      } else if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.SET_WARNING_TIME) {
         this.writeVarInt(this.warningTime);
      } else if (this.action == WrapperPlayServerWorldBorder.WorldBorderAction.SET_WARNING_BLOCKS) {
         this.writeVarInt(this.warningBlocks);
      }

   }

   public void copy(WrapperPlayServerWorldBorder wrapper) {
      this.action = wrapper.action;
      this.radius = wrapper.radius;
      this.oldRadius = wrapper.oldRadius;
      this.newRadius = wrapper.newRadius;
      this.speed = wrapper.speed;
      this.centerX = wrapper.centerX;
      this.centerZ = wrapper.centerZ;
      this.portalTeleportBoundary = wrapper.portalTeleportBoundary;
      this.warningTime = wrapper.warningTime;
      this.warningBlocks = wrapper.warningBlocks;
   }

   public WrapperPlayServerWorldBorder.WorldBorderAction getAction() {
      return this.action;
   }

   public double getRadius() {
      return this.radius;
   }

   public double getOldRadius() {
      return this.oldRadius;
   }

   public double getNewRadius() {
      return this.newRadius;
   }

   public long getSpeed() {
      return this.speed;
   }

   public double getCenterX() {
      return this.centerX;
   }

   public double getCenterZ() {
      return this.centerZ;
   }

   public int getPortalTeleportBoundary() {
      return this.portalTeleportBoundary;
   }

   public int getWarningTime() {
      return this.warningTime;
   }

   public int getWarningBlocks() {
      return this.warningBlocks;
   }

   public void setAction(WrapperPlayServerWorldBorder.WorldBorderAction action) {
      this.action = action;
   }

   public void setRadius(double radius) {
      this.radius = radius;
   }

   public void setOldRadius(double oldRadius) {
      this.oldRadius = oldRadius;
   }

   public void setNewRadius(double newRadius) {
      this.newRadius = newRadius;
   }

   public void setSpeed(long speed) {
      this.speed = speed;
   }

   public void setCenterX(double centerX) {
      this.centerX = centerX;
   }

   public void setCenterZ(double centerZ) {
      this.centerZ = centerZ;
   }

   public void setPortalTeleportBoundary(int portalTeleportBoundary) {
      this.portalTeleportBoundary = portalTeleportBoundary;
   }

   public void setWarningTime(int warningTime) {
      this.warningTime = warningTime;
   }

   public void setWarningBlocks(int warningBlocks) {
      this.warningBlocks = warningBlocks;
   }

   public static enum WorldBorderAction {
      SET_SIZE,
      LERP_SIZE,
      SET_CENTER,
      INITIALIZE,
      SET_WARNING_TIME,
      SET_WARNING_BLOCKS;

      public int getId() {
         return this.ordinal();
      }

      public static WrapperPlayServerWorldBorder.WorldBorderAction fromId(int id) {
         return values()[id];
      }

      // $FF: synthetic method
      private static WrapperPlayServerWorldBorder.WorldBorderAction[] $values() {
         return new WrapperPlayServerWorldBorder.WorldBorderAction[]{SET_SIZE, LERP_SIZE, SET_CENTER, INITIALIZE, SET_WARNING_TIME, SET_WARNING_BLOCKS};
      }
   }
}
