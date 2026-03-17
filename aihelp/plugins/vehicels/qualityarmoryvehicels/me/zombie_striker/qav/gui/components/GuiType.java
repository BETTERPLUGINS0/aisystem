/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.inventory.InventoryType
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.components;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public enum GuiType {
    CHEST(InventoryType.CHEST, 9),
    WORKBENCH(InventoryType.WORKBENCH, 9),
    HOPPER(InventoryType.HOPPER, 5),
    DISPENSER(InventoryType.DISPENSER, 8),
    BREWING(InventoryType.BREWING, 4);

    @NotNull
    private final InventoryType inventoryType;
    private final int limit;

    private GuiType(InventoryType inventoryType, int n2) {
        this.inventoryType = inventoryType;
        this.limit = n2;
    }

    @NotNull
    public InventoryType getInventoryType() {
        return this.inventoryType;
    }

    public int getLimit() {
        return this.limit;
    }
}

