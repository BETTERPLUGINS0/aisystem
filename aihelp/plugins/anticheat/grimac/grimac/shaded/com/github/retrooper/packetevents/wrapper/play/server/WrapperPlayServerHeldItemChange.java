package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerHeldItemChange extends PacketWrapper<WrapperPlayServerHeldItemChange> {
   private int slot;

   public WrapperPlayServerHeldItemChange(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerHeldItemChange(int slot) {
      super((PacketTypeCommon)PacketType.Play.Server.HELD_ITEM_CHANGE);
      this.slot = slot;
   }

   public void read() {
      this.slot = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4) ? this.readVarInt() : this.readByte();
   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
         this.writeVarInt(this.slot);
      } else {
         this.writeByte(this.slot);
      }

   }

   public void copy(WrapperPlayServerHeldItemChange wrapper) {
      this.slot = wrapper.slot;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }
}
