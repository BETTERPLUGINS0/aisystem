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

import me.zombie_striker.qav.util.xseries.inventory.BukkitInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

class NewInventoryView
extends BukkitInventoryView {
    private final InventoryView view;

    public NewInventoryView(Object object) {
        this.view = (InventoryView)object;
    }

    @Override
    public Inventory getTopInventory() {
        return this.view.getTopInventory();
    }

    @Override
    public Inventory getBottomInventory() {
        return this.view.getBottomInventory();
    }

    @Override
    public HumanEntity getPlayer() {
        return this.view.getPlayer();
    }

    @Override
    public InventoryType getType() {
        return this.view.getType();
    }

    @Override
    public void setItem(int n, ItemStack itemStack) {
        this.view.setItem(n, itemStack);
    }

    @Override
    public ItemStack getItem(int n) {
        return this.view.getItem(n);
    }

    @Override
    public void setCursor(ItemStack itemStack) {
        this.view.setCursor(itemStack);
    }

    @Override
    public ItemStack getCursor() {
        return this.view.getCursor();
    }

    @Override
    public int convertSlot(int n) {
        return this.view.convertSlot(n);
    }

    @Override
    public void close() {
        this.view.close();
    }

    @Override
    public int countSlots() {
        return this.view.countSlots();
    }

    @Override
    public String getTitle() {
        return this.view.getTitle();
    }

    @Override
    public InventoryView object() {
        return this.view;
    }
}

