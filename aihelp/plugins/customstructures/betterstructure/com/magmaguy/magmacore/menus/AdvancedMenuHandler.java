/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.inventory.Inventory
 */
package com.magmaguy.magmacore.menus;

import com.magmaguy.magmacore.menus.AdvancedMenu;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class AdvancedMenuHandler {
    private static Map<Inventory, AdvancedMenu> advancedMenus;

    public AdvancedMenuHandler() {
        advancedMenus = new HashMap<Inventory, AdvancedMenu>();
    }

    public static void addAdvancedMenu(Inventory inventory, AdvancedMenu advancedMenu) {
        advancedMenus.put(inventory, advancedMenu);
    }

    public static class AdvancedMenuListeners
    implements Listener {
        @EventHandler
        public void onInventoryInteraction(InventoryClickEvent event) {
            AdvancedMenu advancedMenu = advancedMenus.get(event.getInventory());
            if (advancedMenu == null) {
                return;
            }
            event.setCancelled(true);
            advancedMenu.run(event.getSlot());
        }

        @EventHandler
        public void onClose(InventoryCloseEvent event) {
            advancedMenus.remove(event.getInventory());
        }
    }
}

