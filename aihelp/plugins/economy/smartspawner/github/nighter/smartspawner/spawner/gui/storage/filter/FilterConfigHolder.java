package github.nighter.smartspawner.spawner.gui.storage.filter;

import github.nighter.smartspawner.spawner.properties.SpawnerData;
import lombok.Generated;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class FilterConfigHolder implements InventoryHolder {
   private final SpawnerData spawnerData;

   public FilterConfigHolder(SpawnerData spawnerData) {
      this.spawnerData = spawnerData;
   }

   public Inventory getInventory() {
      return null;
   }

   @Generated
   public SpawnerData getSpawnerData() {
      return this.spawnerData;
   }
}
