package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetSlot extends PacketWrapper<WrapperPlayServerSetSlot> {
   private int windowID;
   private int stateID;
   private int slot;
   private ItemStack item;

   public WrapperPlayServerSetSlot(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSetSlot(int windowID, int stateID, int slot, ItemStack item) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_SLOT);
      this.windowID = windowID;
      this.stateID = stateID;
      this.slot = slot;
      this.item = item;
   }

   public void read() {
      this.windowID = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2) ? this.readContainerId() : this.readByte();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
         this.stateID = this.readVarInt();
      }

      this.slot = this.readShort();
      this.item = this.readItemStack();
   }

   public void write() {
      this.writeContainerId(this.windowID);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
         this.writeVarInt(this.stateID);
      }

      this.writeShort(this.slot);
      this.writeItemStack(this.item);
   }

   public void copy(WrapperPlayServerSetSlot wrapper) {
      this.windowID = wrapper.windowID;
      this.stateID = wrapper.stateID;
      this.slot = wrapper.slot;
      this.item = wrapper.item;
   }

   public int getWindowId() {
      return this.windowID;
   }

   public void setWindowId(int windowID) {
      this.windowID = windowID;
   }

   public int getStateId() {
      return this.stateID;
   }

   public void setStateId(int stateID) {
      this.stateID = stateID;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }

   public ItemStack getItem() {
      return this.item;
   }

   public void setItem(ItemStack item) {
      this.item = item;
   }
}
