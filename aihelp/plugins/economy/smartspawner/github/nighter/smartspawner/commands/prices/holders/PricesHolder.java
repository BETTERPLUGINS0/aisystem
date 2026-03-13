package github.nighter.smartspawner.commands.prices.holders;

import lombok.Generated;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PricesHolder implements InventoryHolder {
   private final int currentPage;
   private final int totalPages;

   public PricesHolder(int currentPage, int totalPages) {
      this.currentPage = currentPage;
      this.totalPages = totalPages;
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
}
