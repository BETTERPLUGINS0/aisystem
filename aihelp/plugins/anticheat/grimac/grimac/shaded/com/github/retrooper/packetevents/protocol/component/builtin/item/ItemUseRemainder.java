package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemUseRemainder {
   private ItemStack target;

   public ItemUseRemainder(ItemStack target) {
      this.target = target;
   }

   public static ItemUseRemainder read(PacketWrapper<?> wrapper) {
      ItemStack target = wrapper.readItemStack();
      return new ItemUseRemainder(target);
   }

   public static void write(PacketWrapper<?> wrapper, ItemUseRemainder remainder) {
      wrapper.writeItemStack(remainder.target);
   }

   public ItemStack getTarget() {
      return this.target;
   }

   public void setTarget(ItemStack target) {
      this.target = target;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemUseRemainder)) {
         return false;
      } else {
         ItemUseRemainder that = (ItemUseRemainder)obj;
         return this.target.equals(that.target);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.target);
   }

   public String toString() {
      return "ItemUseRemainder{target=" + this.target + '}';
   }
}
