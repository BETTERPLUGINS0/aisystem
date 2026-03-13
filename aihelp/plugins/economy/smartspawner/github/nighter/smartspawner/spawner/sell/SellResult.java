package github.nighter.smartspawner.spawner.sell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Generated;
import org.bukkit.inventory.ItemStack;

public class SellResult {
   private final double totalValue;
   private final long itemsSold;
   private final List<ItemStack> itemsToRemove;
   private final long timestamp;
   private final boolean successful;

   public SellResult(double totalValue, long itemsSold, List<ItemStack> itemsToRemove) {
      this.totalValue = totalValue;
      this.itemsSold = itemsSold;
      this.itemsToRemove = new ArrayList(itemsToRemove);
      this.timestamp = System.currentTimeMillis();
      this.successful = totalValue > 0.0D && !itemsToRemove.isEmpty();
   }

   public static SellResult empty() {
      return new SellResult(0.0D, 0L, Collections.emptyList());
   }

   public boolean hasItems() {
      return !this.itemsToRemove.isEmpty();
   }

   @Generated
   public double getTotalValue() {
      return this.totalValue;
   }

   @Generated
   public long getItemsSold() {
      return this.itemsSold;
   }

   @Generated
   public List<ItemStack> getItemsToRemove() {
      return this.itemsToRemove;
   }

   @Generated
   public long getTimestamp() {
      return this.timestamp;
   }

   @Generated
   public boolean isSuccessful() {
      return this.successful;
   }
}
