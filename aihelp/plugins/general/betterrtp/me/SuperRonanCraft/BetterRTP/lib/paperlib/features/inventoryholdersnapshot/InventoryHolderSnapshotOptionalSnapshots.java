package me.SuperRonanCraft.BetterRTP.lib.paperlib.features.inventoryholdersnapshot;

import org.bukkit.inventory.Inventory;

public class InventoryHolderSnapshotOptionalSnapshots implements InventoryHolderSnapshot {
   public InventoryHolderSnapshotResult getHolder(Inventory inventory, boolean useSnapshot) {
      return new InventoryHolderSnapshotResult(useSnapshot, inventory.getHolder(useSnapshot));
   }
}
