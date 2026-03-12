package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemSlotDisplay extends SlotDisplay<ItemSlotDisplay> {
   private ItemType item;

   public ItemSlotDisplay(ItemType item) {
      super(SlotDisplayTypes.ITEM);
      this.item = item;
   }

   public static ItemSlotDisplay read(PacketWrapper<?> wrapper) {
      ItemType item = (ItemType)wrapper.readMappedEntity((IRegistry)ItemTypes.getRegistry());
      return new ItemSlotDisplay(item);
   }

   public static void write(PacketWrapper<?> wrapper, ItemSlotDisplay display) {
      wrapper.writeMappedEntity(display.getItem());
   }

   public ItemType getItem() {
      return this.item;
   }

   public void setItem(ItemType item) {
      this.item = item;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemSlotDisplay)) {
         return false;
      } else {
         ItemSlotDisplay that = (ItemSlotDisplay)obj;
         return this.item.equals(that.item);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.item);
   }

   public String toString() {
      return "ItemSlotDisplay{item=" + this.item + '}';
   }
}
