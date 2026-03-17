package advancedplugins.pm2.cv.util.inventory;

import java.util.Objects;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HotbarBackup {
   protected final ItemStack[] contents = new ItemStack[9];

   public HotbarBackup(ItemStack[] contents) {
      for(int var2 = 0; var2 < ((ItemStack[])Objects.requireNonNull(var1, "contents cannot be null")).length && var2 < this.contents.length; ++var2) {
         ItemStack var3 = var1[var2];
         if (var3 != null) {
            this.contents[var2] = var3.clone();
         }
      }

   }

   public HotbarBackup(PlayerInventory playerInventory) {
      for(int var2 = 0; var2 < this.contents.length; ++var2) {
         ItemStack var3 = var1.getItem(var2);
         if (var3 != null) {
            this.contents[var2] = var3.clone();
         }
      }

   }

   public void restore(PlayerInventory inventory) {
      for(int var2 = 0; var2 < this.contents.length; ++var2) {
         var1.setItem(var2, this.contents[var2]);
      }

   }

   public ItemStack[] getContents() {
      return this.contents;
   }
}
