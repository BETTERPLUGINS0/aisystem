package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSelectTrade extends PacketWrapper<WrapperPlayClientSelectTrade> {
   private int slot;

   public WrapperPlayClientSelectTrade(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientSelectTrade(int slot) {
      super((PacketTypeCommon)PacketType.Play.Client.SELECT_TRADE);
      this.slot = slot;
   }

   public void read() {
      this.slot = this.readVarInt();
   }

   public void write() {
      this.writeVarInt(this.slot);
   }

   public void copy(WrapperPlayClientSelectTrade wrapper) {
      this.slot = wrapper.slot;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }
}
