package com.lenis0012.bukkit.loginsecurity.libs.paper.features.inventoryholdersnapshot;

import org.bukkit.inventory.Inventory;

public class InventoryHolderSnapshotNoOption implements InventoryHolderSnapshot {
   public InventoryHolderSnapshotResult getHolder(Inventory inventory, boolean useSnapshot) {
      return new InventoryHolderSnapshotResult(true, inventory.getHolder());
   }
}
