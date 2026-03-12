package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerPlayerAbilities extends PacketWrapper<WrapperPlayServerPlayerAbilities> {
   private boolean godMode;
   private boolean flying;
   private boolean flightAllowed;
   private boolean creativeMode;
   private float flySpeed;
   private float fovModifier;

   public WrapperPlayServerPlayerAbilities(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerPlayerAbilities(boolean godMode, boolean flying, boolean flightAllowed, boolean creativeMode, float flySpeed, float fovModifier) {
      super((PacketTypeCommon)PacketType.Play.Server.PLAYER_ABILITIES);
      this.godMode = godMode;
      this.flying = flying;
      this.flightAllowed = flightAllowed;
      this.creativeMode = creativeMode;
      this.flySpeed = flySpeed;
      this.fovModifier = fovModifier;
   }

   public void read() {
      byte mask = this.readByte();
      this.godMode = (mask & 1) != 0;
      this.flying = (mask & 2) != 0;
      this.flightAllowed = (mask & 4) != 0;
      this.creativeMode = (mask & 8) != 0;
      this.flySpeed = this.readFloat();
      this.fovModifier = this.readFloat();
   }

   public void write() {
      byte mask = 0;
      if (this.godMode) {
         mask = (byte)(mask | 1);
      }

      if (this.flying) {
         mask = (byte)(mask | 2);
      }

      if (this.flightAllowed) {
         mask = (byte)(mask | 4);
      }

      if (this.creativeMode) {
         mask = (byte)(mask | 8);
      }

      this.writeByte(mask);
      this.writeFloat(this.flySpeed);
      this.writeFloat(this.fovModifier);
   }

   public void copy(WrapperPlayServerPlayerAbilities wrapper) {
      this.godMode = wrapper.godMode;
      this.flying = wrapper.flying;
      this.flightAllowed = wrapper.flightAllowed;
      this.creativeMode = wrapper.creativeMode;
      this.flySpeed = wrapper.flySpeed;
      this.fovModifier = wrapper.fovModifier;
   }

   public boolean isInGodMode() {
      return this.godMode;
   }

   public void setInGodMode(boolean godMode) {
      this.godMode = godMode;
   }

   public boolean isFlying() {
      return this.flying;
   }

   public void setFlying(boolean flying) {
      this.flying = flying;
   }

   public boolean isFlightAllowed() {
      return this.flightAllowed;
   }

   public void setFlightAllowed(boolean flightAllowed) {
      this.flightAllowed = flightAllowed;
   }

   public boolean isInCreativeMode() {
      return this.creativeMode;
   }

   public void setInCreativeMode(boolean creativeMode) {
      this.creativeMode = creativeMode;
   }

   public float getFlySpeed() {
      return this.flySpeed;
   }

   public void setFlySpeed(float flySpeed) {
      this.flySpeed = flySpeed;
   }

   public float getFOVModifier() {
      return this.fovModifier;
   }

   public void setFOVModifier(float fovModifier) {
      this.fovModifier = fovModifier;
   }
}
