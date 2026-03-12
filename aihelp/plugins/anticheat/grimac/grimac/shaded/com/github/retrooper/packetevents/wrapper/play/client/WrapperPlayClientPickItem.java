package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public class WrapperPlayClientPickItem extends PacketWrapper<WrapperPlayClientPickItem> {
   private int slot;

   public WrapperPlayClientPickItem(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPickItem(int slot) {
      super((PacketTypeCommon)PacketType.Play.Client.PICK_ITEM);
      this.slot = slot;
   }

   public void read() {
      this.slot = this.readVarInt();
   }

   public void write() {
      this.writeVarInt(this.slot);
   }

   public void copy(WrapperPlayClientPickItem wrapper) {
      this.slot = wrapper.slot;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }
}
