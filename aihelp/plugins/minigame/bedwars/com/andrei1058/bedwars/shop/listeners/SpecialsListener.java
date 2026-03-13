/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.shop.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.player.PlayerBedBugSpawnEvent;
import com.andrei1058.bedwars.api.events.player.PlayerDreamDefenderSpawnEvent;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpecialsListener
implements Listener {
    @EventHandler(priority=EventPriority.LOWEST)
    public void onSpecialInteract(PlayerInteractEvent e) {
        Event event;
        ITeam playerTeam;
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        ItemStack i = e.getItem();
        if (i == null) {
            return;
        }
        if (i.getType() == Material.AIR) {
            return;
        }
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) {
            return;
        }
        if (a.getRespawnSessions().containsKey(e.getPlayer())) {
            return;
        }
        if (!a.isPlayer(p)) {
            return;
        }
        Block b = e.getClickedBlock();
        if (b == null) {
            return;
        }
        Location l = b.getLocation();
        if (BedWars.shop.getYml().getBoolean("shop-specials.silverfish.enable") && !Misc.isProjectile(Material.valueOf((String)BedWars.shop.getYml().getString("shop-specials.silverfish.material"))) && i.getType() == Material.valueOf((String)BedWars.shop.getYml().getString("shop-specials.silverfish.material")) && BedWars.nms.itemStackDataCompare(i, (short)BedWars.shop.getYml().getInt("shop-specials.silverfish.data"))) {
            e.setCancelled(true);
            playerTeam = a.getTeam(p);
            event = new PlayerBedBugSpawnEvent(p, playerTeam, a);
            BedWars.nms.spawnSilverfish(l.add(0.0, 1.0, 0.0), playerTeam, BedWars.shop.getYml().getDouble("shop-specials.silverfish.speed"), BedWars.shop.getYml().getDouble("shop-specials.silverfish.health"), BedWars.shop.getInt("shop-specials.silverfish.despawn"), BedWars.shop.getYml().getDouble("shop-specials.silverfish.damage"));
            Bukkit.getPluginManager().callEvent(event);
            if (!BedWars.nms.isProjectile(i)) {
                BedWars.nms.minusAmount(p, i, 1);
                p.updateInventory();
            }
        }
        if (BedWars.shop.getYml().getBoolean("shop-specials.iron-golem.enable") && !Misc.isProjectile(Material.valueOf((String)BedWars.shop.getYml().getString("shop-specials.iron-golem.material"))) && i.getType() == Material.valueOf((String)BedWars.shop.getYml().getString("shop-specials.iron-golem.material")) && BedWars.nms.itemStackDataCompare(i, (short)BedWars.shop.getYml().getInt("shop-specials.iron-golem.data"))) {
            e.setCancelled(true);
            playerTeam = a.getTeam(p);
            event = new PlayerDreamDefenderSpawnEvent(p, playerTeam, a);
            BedWars.nms.spawnIronGolem(l.add(0.0, 1.0, 0.0), playerTeam, BedWars.shop.getYml().getDouble("shop-specials.iron-golem.speed"), BedWars.shop.getYml().getDouble("shop-specials.iron-golem.health"), BedWars.shop.getInt("shop-specials.iron-golem.despawn"));
            Bukkit.getPluginManager().callEvent(event);
            if (!BedWars.nms.isProjectile(i)) {
                BedWars.nms.minusAmount(p, i, 1);
                p.updateInventory();
            }
        }
    }
}

