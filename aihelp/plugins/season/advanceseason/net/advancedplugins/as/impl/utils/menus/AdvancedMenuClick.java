/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.inventory.Inventory
 */
package net.advancedplugins.as.impl.utils.menus;

import net.advancedplugins.as.impl.utils.menus.AdvancedMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class AdvancedMenuClick
implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        Inventory inventory = inventoryClickEvent.getInventory();
        ClickType clickType = inventoryClickEvent.getClick();
        if (!(inventory.getHolder() instanceof AdvancedMenu)) {
            return;
        }
        AdvancedMenu advancedMenu = (AdvancedMenu)inventory.getHolder();
        inventoryClickEvent.setCancelled(true);
        int n = inventoryClickEvent.getRawSlot();
        if (clickType.equals((Object)ClickType.UNKNOWN) || n > inventory.getSize()) {
            return;
        }
        advancedMenu.onClick((Player)inventoryClickEvent.getWhoClicked(), n, clickType);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {
        Inventory inventory = inventoryCloseEvent.getInventory();
        if (!(inventory.getHolder() instanceof AdvancedMenu)) {
            return;
        }
        AdvancedMenu advancedMenu = (AdvancedMenu)inventory.getHolder();
        advancedMenu.onClose((Player)inventoryCloseEvent.getPlayer());
    }
}

