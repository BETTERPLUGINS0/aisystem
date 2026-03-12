package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public class WrapperPlayClientSteerVehicle extends PacketWrapper<WrapperPlayClientSteerVehicle> {
   private float sideways;
   private float forward;
   private byte flags;

   public WrapperPlayClientSteerVehicle(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientSteerVehicle(float sideways, float forward, byte flags) {
      super((PacketTypeCommon)PacketType.Play.Client.STEER_VEHICLE);
      this.sideways = sideways;
      this.forward = forward;
      this.flags = flags;
   }

   public void read() {
      this.sideways = this.readFloat();
      this.forward = this.readFloat();
      this.flags = this.readByte();
   }

   public void write() {
      this.writeFloat(this.sideways);
      this.writeFloat(this.forward);
      this.writeByte(this.flags);
   }

   public void copy(WrapperPlayClientSteerVehicle wrapper) {
      this.sideways = wrapper.sideways;
      this.forward = wrapper.forward;
      this.flags = wrapper.flags;
   }

   public float getSideways() {
      return this.sideways;
   }

   public void setSideways(float sideways) {
      this.sideways = sideways;
   }

   public float getForward() {
      return this.forward;
   }

   public void setForward(float forward) {
      this.forward = forward;
   }

   public byte getFlags() {
      return this.flags;
   }

   public void setFlags(byte flags) {
      this.flags = flags;
   }

   public boolean isJump() {
      return (this.flags & 1) != 0;
   }

   public void setJump(boolean jump) {
      if (jump) {
         this.flags = (byte)(this.flags | 1);
      } else {
         this.flags &= -2;
      }

   }

   public boolean isUnmount() {
      return (this.flags & 2) != 0;
   }

   public void setUnmount(boolean unmount) {
      if (unmount) {
         this.flags = (byte)(this.flags | 2);
      } else {
         this.flags &= -3;
      }

   }
}
