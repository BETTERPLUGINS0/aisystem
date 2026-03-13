package github.nighter.smartspawner.commands.list.gui.management;

import lombok.Generated;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class SpawnerManagementHolder implements InventoryHolder {
   private final String spawnerId;
   private final String worldName;
   private final int listPage;
   private final String targetServer;

   public SpawnerManagementHolder(String spawnerId, String worldName, int listPage) {
      this(spawnerId, worldName, listPage, (String)null);
   }

   public SpawnerManagementHolder(String spawnerId, String worldName, int listPage, String targetServer) {
      this.spawnerId = spawnerId;
      this.worldName = worldName;
      this.listPage = listPage;
      this.targetServer = targetServer;
   }

   public boolean isRemoteServer() {
      return this.targetServer != null;
   }

   public Inventory getInventory() {
      return null;
   }

   @Generated
   public String getSpawnerId() {
      return this.spawnerId;
   }

   @Generated
   public String getWorldName() {
      return this.worldName;
   }

   @Generated
   public int getListPage() {
      return this.listPage;
   }

   @Generated
   public String getTargetServer() {
      return this.targetServer;
   }
}
