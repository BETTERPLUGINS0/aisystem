package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayWorldBorderLerpSize extends PacketWrapper<WrapperPlayWorldBorderLerpSize> {
   private double oldDiameter;
   private double newDiameter;
   private long speed;

   public WrapperPlayWorldBorderLerpSize(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayWorldBorderLerpSize(double oldDiameter, double newDiameter, long speed) {
      super((PacketTypeCommon)PacketType.Play.Server.WORLD_BORDER_LERP_SIZE);
      this.oldDiameter = oldDiameter;
      this.newDiameter = newDiameter;
      this.speed = speed;
   }

   public void read() {
      this.oldDiameter = this.readDouble();
      this.newDiameter = this.readDouble();
      this.speed = this.readVarLong();
   }

   public void write() {
      this.writeDouble(this.oldDiameter);
      this.writeDouble(this.newDiameter);
      this.writeVarLong(this.speed);
   }

   public void copy(WrapperPlayWorldBorderLerpSize packet) {
      this.oldDiameter = packet.oldDiameter;
      this.newDiameter = packet.newDiameter;
      this.speed = packet.speed;
   }

   public double getOldDiameter() {
      return this.oldDiameter;
   }

   public double getNewDiameter() {
      return this.newDiameter;
   }

   public long getSpeed() {
      return this.speed;
   }

   public void setOldDiameter(double oldDiameter) {
      this.oldDiameter = oldDiameter;
   }

   public void setNewDiameter(double newDiameter) {
      this.newDiameter = newDiameter;
   }

   public void setSpeed(long speed) {
      this.speed = speed;
   }
}
