package github.nighter.smartspawner.commands.list.gui.adminstacker;

import github.nighter.smartspawner.commands.list.gui.CrossServerSpawnerData;
import lombok.Generated;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class RemoteAdminStackerHolder implements InventoryHolder {
   private final CrossServerSpawnerData spawnerData;
   private final String targetServer;
   private final String worldName;
   private final int listPage;
   private int currentStackSize;

   public RemoteAdminStackerHolder(CrossServerSpawnerData spawnerData, String targetServer, String worldName, int listPage) {
      this.spawnerData = spawnerData;
      this.targetServer = targetServer;
      this.worldName = worldName;
      this.listPage = listPage;
      this.currentStackSize = spawnerData.getStackSize();
   }

   public void adjustStackSize(int amount) {
      this.currentStackSize = Math.max(1, this.currentStackSize + amount);
   }

   public void setCurrentStackSize(int size) {
      this.currentStackSize = Math.max(1, size);
   }

   public String getSpawnerId() {
      return this.spawnerData.getSpawnerId();
   }

   public Inventory getInventory() {
      return null;
   }

   @Generated
   public CrossServerSpawnerData getSpawnerData() {
      return this.spawnerData;
   }

   @Generated
   public String getTargetServer() {
      return this.targetServer;
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
   public int getCurrentStackSize() {
      return this.currentStackSize;
   }
}
