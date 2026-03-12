package me.SuperRonanCraft.BetterRTP.lib.paperlib.features.inventoryholdersnapshot;

import org.bukkit.inventory.Inventory;

public class InventoryHolderSnapshotBeforeSnapshots implements InventoryHolderSnapshot {
   public InventoryHolderSnapshotResult getHolder(Inventory inventory, boolean useSnapshot) {
      return new InventoryHolderSnapshotResult(false, inventory.getHolder());
   }
}
