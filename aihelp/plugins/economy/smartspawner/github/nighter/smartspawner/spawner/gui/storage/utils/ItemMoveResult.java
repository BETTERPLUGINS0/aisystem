package github.nighter.smartspawner.spawner.gui.storage.utils;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public record ItemMoveResult(int amountMoved, List<ItemStack> movedItems) {
   public ItemMoveResult(int amountMoved, List<ItemStack> movedItems) {
      this.amountMoved = amountMoved;
      this.movedItems = movedItems;
   }

   public int amountMoved() {
      return this.amountMoved;
   }

   public List<ItemStack> movedItems() {
      return this.movedItems;
   }
}
