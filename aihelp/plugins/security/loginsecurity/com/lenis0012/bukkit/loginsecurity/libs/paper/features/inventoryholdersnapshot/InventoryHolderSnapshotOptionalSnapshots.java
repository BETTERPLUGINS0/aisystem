package com.lenis0012.bukkit.loginsecurity.libs.paper.features.inventoryholdersnapshot;

import org.bukkit.inventory.Inventory;

public class InventoryHolderSnapshotOptionalSnapshots implements InventoryHolderSnapshot {
   public InventoryHolderSnapshotResult getHolder(Inventory inventory, boolean useSnapshot) {
      return new InventoryHolderSnapshotResult(useSnapshot, inventory.getHolder(useSnapshot));
   }
}
