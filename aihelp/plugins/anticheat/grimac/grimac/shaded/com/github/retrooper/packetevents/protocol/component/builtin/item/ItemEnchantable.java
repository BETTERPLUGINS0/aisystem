package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemEnchantable {
   private int value;

   public ItemEnchantable(int value) {
      this.value = value;
   }

   public static ItemEnchantable read(PacketWrapper<?> wrapper) {
      int value = wrapper.readVarInt();
      return new ItemEnchantable(value);
   }

   public static void write(PacketWrapper<?> wrapper, ItemEnchantable enchantable) {
      wrapper.writeVarInt(enchantable.value);
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemEnchantable)) {
         return false;
      } else {
         ItemEnchantable that = (ItemEnchantable)obj;
         return this.value == that.value;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.value);
   }

   public String toString() {
      return "ItemEnchantable{value=" + this.value + '}';
   }
}
