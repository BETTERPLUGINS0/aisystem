/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.shop.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDropListener
implements Listener {
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        IArena a = Arena.getArenaByPlayer(e.getPlayer());
        if (a == null) {
            return;
        }
        String identifier = BedWars.nms.getShopUpgradeIdentifier(e.getItemDrop().getItemStack());
        if (identifier == null) {
            return;
        }
        if (identifier.isEmpty() || identifier.equals(" ")) {
            return;
        }
        if (identifier.equals("null")) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e instanceof Player)) {
            return;
        }
        IArena a = Arena.getArenaByPlayer((Player)e.getPlayer());
        if (a == null) {
            return;
        }
        for (ItemStack i : e.getInventory()) {
            String identifier;
            if (i == null || i.getType() == Material.AIR || !(identifier = BedWars.nms.getShopUpgradeIdentifier(i)).isEmpty() && !identifier.equals(" ")) continue;
            return;
        }
    }
}

