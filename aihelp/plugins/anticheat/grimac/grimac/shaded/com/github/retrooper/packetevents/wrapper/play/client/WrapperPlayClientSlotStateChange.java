package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSlotStateChange extends PacketWrapper<WrapperPlayClientSlotStateChange> {
   private int slot;
   private int windowId;
   private boolean state;

   public WrapperPlayClientSlotStateChange(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientSlotStateChange(int slot, int windowId, boolean state) {
      super((PacketTypeCommon)PacketType.Play.Client.SLOT_STATE_CHANGE);
      this.slot = slot;
      this.windowId = windowId;
      this.state = state;
   }

   public void read() {
      this.slot = this.readVarInt();
      this.windowId = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2) ? this.readContainerId() : this.readVarInt();
      this.state = this.readBoolean();
   }

   public void write() {
      this.writeVarInt(this.slot);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.writeContainerId(this.windowId);
      } else {
         this.writeVarInt(this.windowId);
      }

      this.writeBoolean(this.state);
   }

   public void copy(WrapperPlayClientSlotStateChange wrapper) {
      this.slot = wrapper.slot;
      this.windowId = wrapper.windowId;
      this.state = wrapper.state;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }

   public int getWindowId() {
      return this.windowId;
   }

   public void setWindowId(int windowId) {
      this.windowId = windowId;
   }

   public boolean isState() {
      return this.state;
   }

   public void setState(boolean state) {
      this.state = state;
   }
}
