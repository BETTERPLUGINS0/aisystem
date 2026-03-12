package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientHeldItemChange extends PacketWrapper<WrapperPlayClientHeldItemChange> {
   private int slot;

   public WrapperPlayClientHeldItemChange(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientHeldItemChange(int slot) {
      super((PacketTypeCommon)PacketType.Play.Client.HELD_ITEM_CHANGE);
      this.slot = slot;
   }

   public void read() {
      this.slot = this.readShort();
   }

   public void write() {
      this.writeShort(this.slot);
   }

   public void copy(WrapperPlayClientHeldItemChange wrapper) {
      this.slot = wrapper.slot;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }
}
