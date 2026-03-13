package github.nighter.smartspawner.spawner.gui.stacker;

import github.nighter.smartspawner.spawner.gui.SpawnerHolder;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class SpawnerStackerHolder implements InventoryHolder, SpawnerHolder {
   private final SpawnerData spawnerData;

   public SpawnerStackerHolder(SpawnerData spawnerData) {
      this.spawnerData = spawnerData;
   }

   public Inventory getInventory() {
      return null;
   }

   public SpawnerData getSpawnerData() {
      return this.spawnerData;
   }
}
