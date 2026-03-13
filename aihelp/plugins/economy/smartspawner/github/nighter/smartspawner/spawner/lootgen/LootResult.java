package github.nighter.smartspawner.spawner.lootgen;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public record LootResult(List<ItemStack> items, int experience) {
   public LootResult(List<ItemStack> items, int experience) {
      this.items = items;
      this.experience = experience;
   }

   public List<ItemStack> items() {
      return this.items;
   }

   public int experience() {
      return this.experience;
   }
}
