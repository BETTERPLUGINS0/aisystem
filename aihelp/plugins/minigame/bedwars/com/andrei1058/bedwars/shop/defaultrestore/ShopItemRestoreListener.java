/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDropItemEvent
 *  org.bukkit.event.entity.EntityPickupItemEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.shop.defaultrestore;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.support.version.common.VersionCommon;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ShopItemRestoreListener {
    public static boolean managePickup(Item item, LivingEntity player) {
        if (!(player instanceof Player)) {
            return false;
        }
        if (VersionCommon.api.getArenaUtil().getArenaByPlayer((Player)player) == null) {
            return false;
        }
        if (VersionCommon.api.getArenaUtil().getArenaByPlayer((Player)player).getStatus() != GameState.playing) {
            return false;
        }
        if (!VersionCommon.api.getArenaUtil().getArenaByPlayer((Player)player).isPlayer((Player)player)) {
            return false;
        }
        if (VersionCommon.api.getVersionSupport().isSword(item.getItemStack())) {
            for (ItemStack is : ((Player)player).getInventory()) {
                if (is == null || is.getType() == Material.AIR || !VersionCommon.api.getVersionSupport().isCustomBedWarsItem(is) || !VersionCommon.api.getVersionSupport().getCustomData(is).equalsIgnoreCase("DEFAULT_ITEM")) continue;
                ((Player)player).getInventory().remove(is);
                ((Player)player).updateInventory();
                return false;
            }
        }
        return false;
    }

    private static boolean manageDrop(Entity player, Item item) {
        if (!(player instanceof Player)) {
            return false;
        }
        if (VersionCommon.api.getArenaUtil().getArenaByPlayer((Player)player) == null) {
            return false;
        }
        IArena a = VersionCommon.api.getArenaUtil().getArenaByPlayer((Player)player);
        if (a.getStatus() != GameState.playing) {
            return false;
        }
        if (!a.isPlayer((Player)player)) {
            return false;
        }
        if (VersionCommon.api.getVersionSupport().isCustomBedWarsItem(item.getItemStack()) && VersionCommon.api.getVersionSupport().getCustomData(item.getItemStack()).equalsIgnoreCase("DEFAULT_ITEM") && VersionCommon.api.getVersionSupport().isSword(item.getItemStack())) {
            boolean hasSword = false;
            for (ItemStack is : ((Player)player).getInventory()) {
                if (is == null || !VersionCommon.api.getVersionSupport().isSword(is) || !(VersionCommon.api.getVersionSupport().getDamage(is) >= VersionCommon.api.getVersionSupport().getDamage(item.getItemStack()))) continue;
                hasSword = true;
                break;
            }
            return !hasSword;
        }
        boolean sword = false;
        for (ItemStack is : ((Player)player).getInventory()) {
            if (is == null || !VersionCommon.api.getVersionSupport().isSword(is)) continue;
            sword = true;
            break;
        }
        if (!sword) {
            a.getTeam((Player)player).defaultSword((Player)player, true);
        }
        return false;
    }

    public static class DefaultRestoreInvClose
    implements Listener {
        @EventHandler
        public void onInventoryClose(InventoryCloseEvent e) {
            ITeam team;
            if (e.getInventory().getType() == InventoryType.PLAYER) {
                return;
            }
            if (VersionCommon.api.getArenaUtil().getArenaByPlayer((Player)e.getPlayer()) == null) {
                return;
            }
            IArena a = VersionCommon.api.getArenaUtil().getArenaByPlayer((Player)e.getPlayer());
            if (a.getStatus() != GameState.playing) {
                return;
            }
            if (!a.isPlayer((Player)e.getPlayer())) {
                return;
            }
            boolean sword = false;
            for (ItemStack is : e.getPlayer().getInventory()) {
                if (is == null || is.getType() == Material.AIR || !VersionCommon.api.getVersionSupport().isSword(is)) continue;
                sword = true;
            }
            if (!sword && (team = a.getTeam((Player)e.getPlayer())) != null && !a.isReSpawning((Player)e.getPlayer())) {
                team.defaultSword((Player)e.getPlayer(), true);
            }
        }
    }

    public static class EntityPickup
    implements Listener {
        @EventHandler
        public void onDrop(EntityPickupItemEvent e) {
            if (ShopItemRestoreListener.managePickup(e.getItem(), e.getEntity())) {
                e.setCancelled(true);
            }
        }
    }

    public static class EntityDrop
    implements Listener {
        @EventHandler
        public void onDrop(EntityDropItemEvent e) {
            if (ShopItemRestoreListener.manageDrop(e.getEntity(), e.getItemDrop())) {
                e.setCancelled(true);
            }
        }
    }

    public static class PlayerPickup
    implements Listener {
        @EventHandler
        public void onDrop(PlayerPickupItemEvent e) {
            if (ShopItemRestoreListener.managePickup(e.getItem(), (LivingEntity)e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    public static class PlayerDrop
    implements Listener {
        @EventHandler
        public void onDrop(PlayerDropItemEvent e) {
            if (ShopItemRestoreListener.manageDrop((Entity)e.getPlayer(), e.getItemDrop())) {
                e.setCancelled(true);
            }
        }
    }
}

