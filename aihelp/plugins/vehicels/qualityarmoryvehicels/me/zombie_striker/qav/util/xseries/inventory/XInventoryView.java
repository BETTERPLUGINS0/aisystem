/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.InventoryView
 */
package me.zombie_striker.qav.util.xseries.inventory;

import me.zombie_striker.qav.util.xseries.inventory.BukkitInventoryView;
import me.zombie_striker.qav.util.xseries.inventory.NewInventoryView;
import me.zombie_striker.qav.util.xseries.inventory.OldInventoryView;
import org.bukkit.inventory.InventoryView;

public final class XInventoryView {
    private static final boolean USE_INTERFACE = InventoryView.class.isInterface();

    private XInventoryView() {
    }

    public static BukkitInventoryView of(InventoryView inventoryView) {
        if (USE_INTERFACE) {
            return new NewInventoryView(inventoryView);
        }
        return new OldInventoryView(inventoryView);
    }
}

