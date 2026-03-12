package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemStackSlotDisplay extends SlotDisplay<ItemStackSlotDisplay> {
   private ItemStack stack;

   public ItemStackSlotDisplay(ItemStack stack) {
      super(SlotDisplayTypes.ITEM_STACK);
      this.stack = stack;
   }

   public static ItemStackSlotDisplay read(PacketWrapper<?> wrapper) {
      ItemStack stack = wrapper.readItemStack();
      return new ItemStackSlotDisplay(stack);
   }

   public static void write(PacketWrapper<?> wrapper, ItemStackSlotDisplay display) {
      wrapper.writeItemStack(display.stack);
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public void setStack(ItemStack stack) {
      this.stack = stack;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemStackSlotDisplay)) {
         return false;
      } else {
         ItemStackSlotDisplay that = (ItemStackSlotDisplay)obj;
         return this.stack.equals(that.stack);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.stack);
   }

   public String toString() {
      return "ItemStackSlotDisplay{stack=" + this.stack + '}';
   }
}
