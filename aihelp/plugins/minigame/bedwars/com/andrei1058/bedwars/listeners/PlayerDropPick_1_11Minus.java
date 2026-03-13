/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.player.PlayerGeneratorCollectEvent;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.support.version.common.VersionCommon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerDropPick_1_11Minus
implements Listener {
    private static BedWars api;

    public PlayerDropPick_1_11Minus(BedWars bedWars) {
        api = bedWars;
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (api.getServerType() == ServerType.MULTIARENA && e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(api.getLobbyWorld())) {
            e.setCancelled(true);
            return;
        }
        IArena a = api.getArenaUtil().getArenaByPlayer(e.getPlayer());
        if (a == null) {
            return;
        }
        if (!a.isPlayer(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }
        if (a.getStatus() != GameState.playing) {
            e.setCancelled(true);
            return;
        }
        if (a.getRespawnSessions().containsKey(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }
        if (e.getItem().getItemStack().getType() == Material.ARROW) {
            e.getItem().setItemStack(api.getVersionSupport().createItemStack(e.getItem().getItemStack().getType().toString(), e.getItem().getItemStack().getAmount(), (short)0));
            return;
        }
        if (VersionCommon.api.getVersionSupport().isBed(e.getItem().getItemStack().getType())) {
            e.setCancelled(true);
            e.getItem().remove();
        } else if (e.getItem().getItemStack().hasItemMeta() && e.getItem().getItemStack().getItemMeta().hasDisplayName() && e.getItem().getItemStack().getItemMeta().getDisplayName().contains("custom")) {
            Material material = e.getItem().getItemStack().getType();
            ItemMeta itemMeta = new ItemStack(material).getItemMeta();
            if (!api.getAFKUtil().isPlayerAFK(e.getPlayer())) {
                PlayerGeneratorCollectEvent event = new PlayerGeneratorCollectEvent(e.getPlayer(), e.getItem(), a);
                Bukkit.getPluginManager().callEvent((Event)event);
                if (event.isCancelled()) {
                    e.setCancelled(true);
                } else {
                    e.getItem().getItemStack().setItemMeta(itemMeta);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (api.getServerType() == ServerType.MULTIARENA && e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(api.getLobbyWorld())) {
            e.setCancelled(true);
            return;
        }
        IArena a = api.getArenaUtil().getArenaByPlayer(e.getPlayer());
        if (a == null) {
            return;
        }
        if (!a.isPlayer(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }
        if (a.getStatus() != GameState.playing) {
            e.setCancelled(true);
        } else {
            ItemStack i = e.getItemDrop().getItemStack();
            if (i.getType() == Material.COMPASS) {
                e.setCancelled(true);
                return;
            }
        }
        if (a.getRespawnSessions().containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}

