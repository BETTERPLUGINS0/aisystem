/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.Inventory
 */
package io.papermc.lib.features.inventoryholdersnapshot;

import io.papermc.lib.features.inventoryholdersnapshot.InventoryHolderSnapshotResult;
import org.bukkit.inventory.Inventory;

public interface InventoryHolderSnapshot {
    public InventoryHolderSnapshotResult getHolder(Inventory var1, boolean var2);
}

