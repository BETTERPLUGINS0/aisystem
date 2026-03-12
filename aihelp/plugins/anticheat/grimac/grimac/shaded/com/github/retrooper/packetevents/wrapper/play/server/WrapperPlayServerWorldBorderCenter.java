package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerWorldBorderCenter extends PacketWrapper<WrapperPlayServerWorldBorderCenter> {
   private double x;
   private double z;

   public WrapperPlayServerWorldBorderCenter(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerWorldBorderCenter(double x, double z) {
      super((PacketTypeCommon)PacketType.Play.Server.WORLD_BORDER_CENTER);
      this.x = x;
      this.z = z;
   }

   public void read() {
      this.x = this.readDouble();
      this.z = this.readDouble();
   }

   public void write() {
      this.writeDouble(this.x);
      this.writeDouble(this.z);
   }

   public void copy(WrapperPlayServerWorldBorderCenter wrapper) {
      this.x = wrapper.x;
      this.z = wrapper.z;
   }

   public double getX() {
      return this.x;
   }

   public double getZ() {
      return this.z;
   }

   public void setX(double x) {
      this.x = x;
   }

   public void setZ(double z) {
      this.z = z;
   }
}
