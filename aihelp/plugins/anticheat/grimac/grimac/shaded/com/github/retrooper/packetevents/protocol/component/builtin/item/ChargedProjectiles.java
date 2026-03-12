package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class ChargedProjectiles {
   private List<ItemStack> items;

   public ChargedProjectiles(List<ItemStack> items) {
      this.items = items;
   }

   public static ChargedProjectiles read(PacketWrapper<?> wrapper) {
      List<ItemStack> items = wrapper.readList(PacketWrapper::readPresentItemStack);
      return new ChargedProjectiles(items);
   }

   public static void write(PacketWrapper<?> wrapper, ChargedProjectiles projectiles) {
      wrapper.writeList(projectiles.items, PacketWrapper::writePresentItemStack);
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
      } else if (!(obj instanceof ChargedProjectiles)) {
         return false;
      } else {
         ChargedProjectiles that = (ChargedProjectiles)obj;
         return this.items.equals(that.items);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.items);
   }
}
