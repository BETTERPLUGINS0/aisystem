/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDropItemEvent
 *  org.bukkit.event.entity.EntityPickupItemEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerPickupArrowEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.player.PlayerGeneratorCollectEvent;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.support.version.common.VersionCommon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemDropPickListener {
    private static boolean managePickup(Item item, LivingEntity player) {
        if (!(player instanceof Player)) {
            return false;
        }
        if (VersionCommon.api.getServerType() == ServerType.MULTIARENA && player.getLocation().getWorld().getName().equalsIgnoreCase(VersionCommon.api.getLobbyWorld())) {
            return true;
        }
        IArena a = VersionCommon.api.getArenaUtil().getArenaByPlayer((Player)player);
        if (a == null) {
            return false;
        }
        if (!a.isPlayer((Player)player)) {
            return true;
        }
        if (a.getStatus() != GameState.playing) {
            return true;
        }
        if (a.getRespawnSessions().containsKey(player)) {
            return true;
        }
        if (item.getItemStack().getType() == Material.ARROW) {
            item.setItemStack(VersionCommon.api.getVersionSupport().createItemStack(item.getItemStack().getType().toString(), item.getItemStack().getAmount(), (short)0));
            return false;
        }
        if (item.getItemStack().getType().toString().equals("BED")) {
            item.remove();
            return true;
        }
        if (item.getItemStack().hasItemMeta() && item.getItemStack().getItemMeta().hasDisplayName() && item.getItemStack().getItemMeta().getDisplayName().contains("custom")) {
            Material material = item.getItemStack().getType();
            ItemMeta itemMeta = new ItemStack(material).getItemMeta();
            if (!VersionCommon.api.getAFKUtil().isPlayerAFK(((Player)player).getPlayer())) {
                PlayerGeneratorCollectEvent event = new PlayerGeneratorCollectEvent((Player)player, item, a);
                Bukkit.getPluginManager().callEvent((Event)event);
                if (event.isCancelled()) {
                    return true;
                }
                item.getItemStack().setItemMeta(itemMeta);
            } else {
                return true;
            }
        }
        return false;
    }

    private static boolean manageDrop(Entity player, Item item) {
        if (!(player instanceof Player)) {
            return false;
        }
        if (VersionCommon.api.getServerType() == ServerType.MULTIARENA && player.getLocation().getWorld().getName().equalsIgnoreCase(VersionCommon.api.getLobbyWorld())) {
            return true;
        }
        IArena a = VersionCommon.api.getArenaUtil().getArenaByPlayer((Player)player);
        if (a == null) {
            return false;
        }
        if (!a.isPlayer((Player)player)) {
            return true;
        }
        if (a.getStatus() != GameState.playing) {
            return true;
        }
        ItemStack i = item.getItemStack();
        if (i.getType() == Material.COMPASS) {
            return true;
        }
        return a.getRespawnSessions().containsKey(player);
    }

    public static class ArrowCollect
    implements Listener {
        @EventHandler
        public void onArrowPick(PlayerPickupArrowEvent e) {
            if (VersionCommon.api.getArenaUtil().isSpectating(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    public static class EntityPickup
    implements Listener {
        @EventHandler
        public void onPickup(EntityPickupItemEvent e) {
            if (ItemDropPickListener.managePickup(e.getItem(), e.getEntity())) {
                e.setCancelled(true);
            }
        }
    }

    public static class EntityDrop
    implements Listener {
        @EventHandler
        public void onDrop(EntityDropItemEvent e) {
            if (ItemDropPickListener.manageDrop(e.getEntity(), e.getItemDrop())) {
                e.setCancelled(true);
            }
        }
    }

    public static class PlayerPickup
    implements Listener {
        @EventHandler
        public void onDrop(PlayerPickupItemEvent e) {
            if (ItemDropPickListener.managePickup(e.getItem(), (LivingEntity)e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    public static class PlayerDrop
    implements Listener {
        @EventHandler
        public void onDrop(PlayerDropItemEvent e) {
            if (ItemDropPickListener.manageDrop((Entity)e.getPlayer(), e.getItemDrop())) {
                e.setCancelled(true);
            }
        }
    }
}

