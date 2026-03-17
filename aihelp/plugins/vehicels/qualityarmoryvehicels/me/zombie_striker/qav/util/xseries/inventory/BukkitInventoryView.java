/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 */
package me.zombie_striker.qav.util.xseries.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public abstract class BukkitInventoryView {
    public abstract Inventory getTopInventory();

    public abstract Inventory getBottomInventory();

    public abstract HumanEntity getPlayer();

    public abstract InventoryType getType();

    public abstract void setItem(int var1, ItemStack var2);

    public abstract ItemStack getItem(int var1);

    public abstract void setCursor(ItemStack var1);

    public abstract ItemStack getCursor();

    public abstract int convertSlot(int var1);

    public abstract void close();

    public abstract int countSlots();

    public abstract String getTitle();

    public abstract InventoryView object();

    public int hashCode() {
        return this.object().hashCode();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (object instanceof BukkitInventoryView) {
            return this.object().equals((Object)((BukkitInventoryView)object).object());
        }
        return this.object().equals(object);
    }

    public String toString() {
        return this.getClass().getSimpleName() + '(' + this.object().toString() + ')';
    }
}

