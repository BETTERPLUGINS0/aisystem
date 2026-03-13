package com.dfsek.terra.lib.paperlib.features.inventoryholdersnapshot;

import org.bukkit.inventory.Inventory;

public class InventoryHolderSnapshotNoOption implements InventoryHolderSnapshot {
   public InventoryHolderSnapshotResult getHolder(Inventory inventory, boolean useSnapshot) {
      return new InventoryHolderSnapshotResult(true, inventory.getHolder());
   }
}
