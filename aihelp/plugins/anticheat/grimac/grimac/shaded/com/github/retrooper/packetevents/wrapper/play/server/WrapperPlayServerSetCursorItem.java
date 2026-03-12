package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetCursorItem extends PacketWrapper<WrapperPlayServerSetCursorItem> {
   private ItemStack stack;

   public WrapperPlayServerSetCursorItem(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSetCursorItem(ItemStack stack) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_CURSOR_ITEM);
      this.stack = stack;
   }

   public void read() {
      this.stack = this.readItemStack();
   }

   public void write() {
      this.writeItemStack(this.stack);
   }

   public void copy(WrapperPlayServerSetCursorItem wrapper) {
      this.stack = wrapper.stack;
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public void setStack(ItemStack stack) {
      this.stack = stack;
   }
}
