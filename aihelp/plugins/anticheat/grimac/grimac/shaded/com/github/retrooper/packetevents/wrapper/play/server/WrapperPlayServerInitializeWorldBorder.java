package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerInitializeWorldBorder extends PacketWrapper<WrapperPlayServerInitializeWorldBorder> {
   private double x;
   private double z;
   private double oldDiameter;
   private double newDiameter;
   private long speed;
   private int portalTeleportBoundary;
   private int warningBlocks;
   private int warningTime;

   public WrapperPlayServerInitializeWorldBorder(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerInitializeWorldBorder(double x, double z, double oldDiameter, double newDiameter, long speed, int portalTeleportBoundary, int warningBlocks, int warningTime) {
      super((PacketTypeCommon)PacketType.Play.Server.INITIALIZE_WORLD_BORDER);
      this.x = x;
      this.z = z;
      this.oldDiameter = oldDiameter;
      this.newDiameter = newDiameter;
      this.speed = speed;
      this.portalTeleportBoundary = portalTeleportBoundary;
      this.warningBlocks = warningBlocks;
      this.warningTime = warningTime;
   }

   public void read() {
      this.x = this.readDouble();
      this.z = this.readDouble();
      this.oldDiameter = this.readDouble();
      this.newDiameter = this.readDouble();
      this.speed = this.readVarLong();
      this.portalTeleportBoundary = this.readVarInt();
      this.warningBlocks = this.readVarInt();
      this.warningTime = this.readVarInt();
   }

   public void write() {
      this.writeDouble(this.x);
      this.writeDouble(this.z);
      this.writeDouble(this.oldDiameter);
      this.writeDouble(this.newDiameter);
      this.writeVarLong(this.speed);
      this.writeVarInt(this.portalTeleportBoundary);
      this.writeVarInt(this.warningBlocks);
      this.writeVarInt(this.warningTime);
   }

   public void copy(WrapperPlayServerInitializeWorldBorder wrapper) {
      this.x = wrapper.x;
      this.z = wrapper.z;
      this.oldDiameter = wrapper.oldDiameter;
      this.newDiameter = wrapper.newDiameter;
      this.speed = wrapper.speed;
      this.portalTeleportBoundary = wrapper.portalTeleportBoundary;
      this.warningBlocks = wrapper.warningBlocks;
      this.warningTime = wrapper.warningTime;
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getZ() {
      return this.z;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public double getOldDiameter() {
      return this.oldDiameter;
   }

   public void setOldDiameter(double oldDiameter) {
      this.oldDiameter = oldDiameter;
   }

   public double getNewDiameter() {
      return this.newDiameter;
   }

   public void setNewDiameter(double newDiameter) {
      this.newDiameter = newDiameter;
   }

   public long getSpeed() {
      return this.speed;
   }

   public void setSpeed(long speed) {
      this.speed = speed;
   }

   public int getPortalTeleportBoundary() {
      return this.portalTeleportBoundary;
   }

   public void setPortalTeleportBoundary(int portalTeleportBoundary) {
      this.portalTeleportBoundary = portalTeleportBoundary;
   }

   public int getWarningBlocks() {
      return this.warningBlocks;
   }

   public void setWarningBlocks(int warningBlocks) {
      this.warningBlocks = warningBlocks;
   }

   public int getWarningTime() {
      return this.warningTime;
   }

   public void setWarningTime(int warningTime) {
      this.warningTime = warningTime;
   }
}
