/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 */
package com.magmaguy.magmacore.menus;

import com.magmaguy.magmacore.menus.AdvancedMenuHandler;
import com.magmaguy.magmacore.menus.MenuButton;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class AdvancedMenu {
    private final Player player;
    private final Map<Integer, MenuButton> advancedMenuItems = new HashMap<Integer, MenuButton>();
    private final Inventory inventory;

    public AdvancedMenu(Player player, int size) {
        this.player = player;
        this.inventory = Bukkit.createInventory((InventoryHolder)player, (int)size);
        AdvancedMenuHandler.addAdvancedMenu(this.inventory, this);
    }

    public void addAdvancedMenuItem(int slot, MenuButton advancedMenuItem) {
        this.advancedMenuItems.put(slot, advancedMenuItem);
        this.inventory.setItem(slot, advancedMenuItem.getItemStack());
    }

    public void removeAdvancedMenuItem(int slot, MenuButton advancedMenuItem) {
        this.advancedMenuItems.put(slot, advancedMenuItem);
        this.inventory.remove(advancedMenuItem.getItemStack());
    }

    public void openInventory(Player player) {
        player.openInventory(this.inventory);
    }

    public void run(int slot) {
        MenuButton advancedMenuItem = this.advancedMenuItems.get(slot);
        if (advancedMenuItem != null) {
            advancedMenuItem.onClick(this.player);
        }
    }
}

