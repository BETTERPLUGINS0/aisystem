package github.nighter.smartspawner.commands.list.gui.adminstacker;

import github.nighter.smartspawner.spawner.properties.SpawnerData;
import lombok.Generated;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class AdminStackerHolder implements InventoryHolder {
   private final SpawnerData spawnerData;
   private final String worldName;
   private final int listPage;

   public AdminStackerHolder(SpawnerData spawnerData, String worldName, int listPage) {
      this.spawnerData = spawnerData;
      this.worldName = worldName;
      this.listPage = listPage;
   }

   public Inventory getInventory() {
      return null;
   }

   @Generated
   public SpawnerData getSpawnerData() {
      return this.spawnerData;
   }

   @Generated
   public String getWorldName() {
      return this.worldName;
   }

   @Generated
   public int getListPage() {
      return this.listPage;
   }
}
