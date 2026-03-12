package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetPlayerInventory extends PacketWrapper<WrapperPlayServerSetPlayerInventory> {
   private int slot;
   private ItemStack stack;

   public WrapperPlayServerSetPlayerInventory(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSetPlayerInventory(int slot, ItemStack stack) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_PLAYER_INVENTORY);
      this.slot = slot;
      this.stack = stack;
   }

   public void read() {
      this.slot = this.readVarInt();
      this.stack = this.readItemStack();
   }

   public void write() {
      this.writeVarInt(this.slot);
      this.writeItemStack(this.stack);
   }

   public void copy(WrapperPlayServerSetPlayerInventory wrapper) {
      this.slot = wrapper.slot;
      this.stack = wrapper.stack;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public void setStack(ItemStack stack) {
      this.stack = stack;
   }
}
