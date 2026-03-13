/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.Inventory
 */
package io.papermc.lib.features.inventoryholdersnapshot;

import io.papermc.lib.features.inventoryholdersnapshot.InventoryHolderSnapshot;
import io.papermc.lib.features.inventoryholdersnapshot.InventoryHolderSnapshotResult;
import org.bukkit.inventory.Inventory;

public class InventoryHolderSnapshotNoOption
implements InventoryHolderSnapshot {
    @Override
    public InventoryHolderSnapshotResult getHolder(Inventory inventory, boolean useSnapshot) {
        return new InventoryHolderSnapshotResult(true, inventory.getHolder());
    }
}

