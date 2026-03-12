package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Optional;

public class WrapperPlayClientPlayerAbilities extends PacketWrapper<WrapperPlayClientPlayerAbilities> {
   private boolean flying;
   private Optional<Boolean> godMode;
   private Optional<Boolean> flightAllowed;
   private Optional<Boolean> creativeMode;
   private Optional<Float> flySpeed;
   private Optional<Float> walkSpeed;

   public WrapperPlayClientPlayerAbilities(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPlayerAbilities(boolean flying, Optional<Boolean> godMode, Optional<Boolean> flightAllowed, Optional<Boolean> creativeMode, Optional<Float> flySpeed, Optional<Float> walkSpeed) {
      super((PacketTypeCommon)PacketType.Play.Client.PLAYER_ABILITIES);
      this.flying = flying;
      this.godMode = godMode;
      this.flightAllowed = flightAllowed;
      this.creativeMode = creativeMode;
      this.flySpeed = flySpeed;
      this.walkSpeed = walkSpeed;
   }

   public WrapperPlayClientPlayerAbilities(boolean flying) {
      this(flying, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
   }

   public void read() {
      byte mask = this.readByte();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         this.flying = (mask & 2) != 0;
         this.godMode = Optional.empty();
         this.flightAllowed = Optional.empty();
         this.creativeMode = Optional.empty();
         this.flySpeed = Optional.empty();
         this.walkSpeed = Optional.empty();
      } else {
         this.godMode = Optional.of((mask & 1) != 0);
         this.flying = (mask & 2) != 0;
         this.flightAllowed = Optional.of((mask & 4) != 0);
         this.creativeMode = Optional.of((mask & 8) != 0);
         this.flySpeed = Optional.of(this.readFloat());
         this.walkSpeed = Optional.of(this.readFloat());
      }

   }

   public void write() {
      byte mask;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         mask = (byte)(this.flying ? 2 : 0);
         this.writeByte(mask);
      } else {
         mask = 0;
         if ((Boolean)this.godMode.orElse(false)) {
            mask = (byte)(mask | 1);
         }

         if (this.flying) {
            mask = (byte)(mask | 2);
         }

         if ((Boolean)this.flightAllowed.orElse(false)) {
            mask = (byte)(mask | 4);
         }

         if ((Boolean)this.creativeMode.orElse(false)) {
            mask = (byte)(mask | 8);
         }

         this.writeByte(mask);
         this.writeFloat((Float)this.flySpeed.orElse(0.1F));
         this.writeFloat((Float)this.walkSpeed.orElse(0.2F));
      }

   }

   public void copy(WrapperPlayClientPlayerAbilities wrapper) {
      this.godMode = wrapper.godMode;
      this.flying = wrapper.flying;
      this.flightAllowed = wrapper.flightAllowed;
      this.creativeMode = wrapper.creativeMode;
      this.flySpeed = wrapper.flySpeed;
      this.walkSpeed = wrapper.walkSpeed;
   }

   public boolean isFlying() {
      return this.flying;
   }

   public void setFlying(boolean flying) {
      this.flying = flying;
   }

   public Optional<Boolean> isInGodMode() {
      return this.godMode;
   }

   public void setInGodMode(Optional<Boolean> godMode) {
      this.godMode = godMode;
   }

   public Optional<Boolean> isFlightAllowed() {
      return this.flightAllowed;
   }

   public void setFlightAllowed(Optional<Boolean> flightAllowed) {
      this.flightAllowed = flightAllowed;
   }

   public Optional<Boolean> isInCreativeMode() {
      return this.creativeMode;
   }

   public void setCreativeMode(Optional<Boolean> creativeMode) {
      this.creativeMode = creativeMode;
   }

   public Optional<Float> getFlySpeed() {
      return this.flySpeed;
   }

   public void setFlySpeed(Optional<Float> flySpeed) {
      this.flySpeed = flySpeed;
   }

   public Optional<Float> getWalkSpeed() {
      return this.walkSpeed;
   }

   public void setWalkSpeed(Optional<Float> walkSpeed) {
      this.walkSpeed = walkSpeed;
   }
}
