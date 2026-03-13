package github.nighter.smartspawner.commands.list.gui.list;

import github.nighter.smartspawner.commands.list.gui.list.enums.FilterOption;
import github.nighter.smartspawner.commands.list.gui.list.enums.SortOption;
import lombok.Generated;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class SpawnerListHolder implements InventoryHolder {
   private final int currentPage;
   private final int totalPages;
   private final String worldName;
   private final FilterOption filterOption;
   private final SortOption sortType;
   private final String targetServer;

   public SpawnerListHolder(int currentPage, int totalPages, String worldName, FilterOption filterOption, SortOption sortType) {
      this(currentPage, totalPages, worldName, filterOption, sortType, (String)null);
   }

   public SpawnerListHolder(int currentPage, int totalPages, String worldName, FilterOption filterOption, SortOption sortType, String targetServer) {
      this.currentPage = currentPage;
      this.totalPages = totalPages;
      this.worldName = worldName;
      this.filterOption = filterOption;
      this.sortType = sortType;
      this.targetServer = targetServer;
   }

   public boolean isRemoteServer() {
      return this.targetServer != null;
   }

   public Inventory getInventory() {
      return null;
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
   public String getWorldName() {
      return this.worldName;
   }

   @Generated
   public FilterOption getFilterOption() {
      return this.filterOption;
   }

   @Generated
   public SortOption getSortType() {
      return this.sortType;
   }

   @Generated
   public String getTargetServer() {
      return this.targetServer;
   }
}
