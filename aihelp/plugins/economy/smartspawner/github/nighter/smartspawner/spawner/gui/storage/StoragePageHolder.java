package github.nighter.smartspawner.spawner.gui.storage;

import github.nighter.smartspawner.spawner.gui.SpawnerHolder;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import lombok.Generated;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class StoragePageHolder implements InventoryHolder, SpawnerHolder {
   private final SpawnerData spawnerData;
   private int currentPage;
   private int totalPages;
   private int oldUsedSlots;
   public static final int ROWS_PER_PAGE = 5;
   public static final int MAX_ITEMS_PER_PAGE = 45;
   private Inventory inventory;

   public StoragePageHolder(SpawnerData spawnerData, int currentPage, int totalPages) {
      this.spawnerData = spawnerData;
      this.currentPage = Math.max(1, Math.min(currentPage, totalPages));
      this.totalPages = Math.max(1, totalPages);
      this.oldUsedSlots = spawnerData.getVirtualInventory().getUsedSlots();
   }

   @NotNull
   public Inventory getInventory() {
      return this.inventory;
   }

   public void setCurrentPage(int page) {
      this.currentPage = Math.max(1, Math.min(page, this.totalPages));
   }

   public void setTotalPages(int totalPages) {
      this.totalPages = Math.max(1, totalPages);
   }

   public void updateOldUsedSlots() {
      this.oldUsedSlots = this.spawnerData.getVirtualInventory().getUsedSlots();
   }

   @Generated
   public SpawnerData getSpawnerData() {
      return this.spawnerData;
   }

   @Generated
   public int getCurrentPage() {
      return this.currentPage;
   }

   @Generated
   public int getTotalPages() {
      return this.totalPages;
   }

   @Generated
   public int getOldUsedSlots() {
      return this.oldUsedSlots;
   }
}
