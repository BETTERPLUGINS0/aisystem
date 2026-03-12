package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class ItemContainerContents {
   private List<ItemStack> items;

   public ItemContainerContents(List<ItemStack> items) {
      this.items = items;
   }

   public static ItemContainerContents read(PacketWrapper<?> wrapper) {
      List<ItemStack> items = wrapper.readList(PacketWrapper::readItemStack);
      return new ItemContainerContents(items);
   }

   public static void write(PacketWrapper<?> wrapper, ItemContainerContents contents) {
      wrapper.writeList(contents.items, PacketWrapper::writeItemStack);
   }

   public void addItem(ItemStack itemStack) {
      this.items.add(itemStack);
   }

   public List<ItemStack> getItems() {
      return this.items;
   }

   public void setItems(List<ItemStack> items) {
      this.items = items;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemContainerContents)) {
         return false;
      } else {
         ItemContainerContents that = (ItemContainerContents)obj;
         return this.items.equals(that.items);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.items);
   }
}
