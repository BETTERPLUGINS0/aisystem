package github.nighter.smartspawner.spawner.gui.sell;

import github.nighter.smartspawner.spawner.properties.SpawnerData;
import lombok.Generated;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class SpawnerSellConfirmHolder implements InventoryHolder {
   private final SpawnerData spawnerData;
   private final SpawnerSellConfirmUI.PreviousGui previousGui;
   private final boolean collectExp;

   public SpawnerSellConfirmHolder(SpawnerData spawnerData, SpawnerSellConfirmUI.PreviousGui previousGui, boolean collectExp) {
      this.spawnerData = spawnerData;
      this.previousGui = previousGui;
      this.collectExp = collectExp;
   }

   @NotNull
   public Inventory getInventory() {
      return null;
   }

   @Generated
   public SpawnerData getSpawnerData() {
      return this.spawnerData;
   }

   @Generated
   public SpawnerSellConfirmUI.PreviousGui getPreviousGui() {
      return this.previousGui;
   }

   @Generated
   public boolean isCollectExp() {
      return this.collectExp;
   }
}
