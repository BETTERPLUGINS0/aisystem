package github.nighter.smartspawner.spawner.lootgen.loot;

import java.util.List;

public record EntityLootConfig(int experience, List<LootItem> possibleItems) {
   public EntityLootConfig(int experience, List<LootItem> possibleItems) {
      this.experience = experience;
      this.possibleItems = possibleItems;
   }

   public List<LootItem> getAllItems() {
      return this.possibleItems;
   }

   public int experience() {
      return this.experience;
   }

   public List<LootItem> possibleItems() {
      return this.possibleItems;
   }
}
