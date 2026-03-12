package ac.grim.grimac.utils.inventory;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import lombok.Generated;

public class InventoryStorage {
   protected final ItemStack[] items;
   private final int size;

   public InventoryStorage(int size) {
      this.items = new ItemStack[size];
      this.size = size;

      for(int i = 0; i < size; ++i) {
         this.items[i] = ItemStack.EMPTY;
      }

   }

   public void setItem(int item, ItemStack stack) {
      this.items[item] = stack == null ? ItemStack.EMPTY : stack;
   }

   public ItemStack getItem(int index) {
      return this.items[index];
   }

   public ItemStack removeItem(int slot, int amount) {
      return slot >= 0 && slot < this.items.length && !this.items[slot].isEmpty() && amount > 0 ? this.items[slot].split(amount) : ItemStack.EMPTY;
   }

   public int getMaxStackSize() {
      return 64;
   }

   @Generated
   public int getSize() {
      return this.size;
   }
}
