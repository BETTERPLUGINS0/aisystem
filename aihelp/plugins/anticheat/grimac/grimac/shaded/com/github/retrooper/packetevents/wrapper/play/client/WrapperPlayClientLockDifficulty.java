package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientLockDifficulty extends PacketWrapper<WrapperPlayClientLockDifficulty> {
   private boolean locked;

   public WrapperPlayClientLockDifficulty(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientLockDifficulty(boolean locked) {
      super((PacketTypeCommon)PacketType.Play.Client.LOCK_DIFFICULTY);
      this.locked = locked;
   }

   public void read() {
      this.locked = this.readBoolean();
   }

   public void write() {
      this.writeBoolean(this.locked);
   }

   public void copy(WrapperPlayClientLockDifficulty wrapper) {
      this.locked = wrapper.locked;
   }

   public boolean isLocked() {
      return this.locked;
   }

   public void setLocked(boolean locked) {
      this.locked = locked;
   }
}
