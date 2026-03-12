package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerProjectilePower extends PacketWrapper<WrapperPlayServerProjectilePower> {
   private int entityId;
   private double powerX;
   private double powerY;
   private double powerZ;

   public WrapperPlayServerProjectilePower(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerProjectilePower(int entityId, double power) {
      this(entityId, power, power, power);
   }

   public WrapperPlayServerProjectilePower(int entityId, double powerX, double powerY, double powerZ) {
      super((PacketTypeCommon)PacketType.Play.Server.PROJECTILE_POWER);
      this.entityId = entityId;
      this.powerX = powerX;
      this.powerY = powerY;
      this.powerZ = powerZ;
   }

   public void read() {
      this.entityId = this.readVarInt();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
         this.setPower(this.readDouble());
      } else {
         this.powerX = this.readDouble();
         this.powerY = this.readDouble();
         this.powerZ = this.readDouble();
      }

   }

   public void write() {
      this.writeVarInt(this.entityId);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
         this.writeDouble(this.getPower());
      } else {
         this.writeDouble(this.powerX);
         this.writeDouble(this.powerY);
         this.writeDouble(this.powerZ);
      }

   }

   public void copy(WrapperPlayServerProjectilePower wrapper) {
      this.entityId = wrapper.entityId;
      this.powerX = wrapper.powerX;
      this.powerY = wrapper.powerY;
      this.powerZ = wrapper.powerZ;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public double getPower() {
      return this.powerX;
   }

   public void setPower(double power) {
      this.powerX = power;
      this.powerY = power;
      this.powerZ = power;
   }

   public double getPowerX() {
      return this.powerX;
   }

   public void setPowerX(double powerX) {
      this.powerX = powerX;
   }

   public double getPowerY() {
      return this.powerY;
   }

   public void setPowerY(double powerY) {
      this.powerY = powerY;
   }

   public double getPowerZ() {
      return this.powerZ;
   }

   public void setPowerZ(double powerZ) {
      this.powerZ = powerZ;
   }
}
