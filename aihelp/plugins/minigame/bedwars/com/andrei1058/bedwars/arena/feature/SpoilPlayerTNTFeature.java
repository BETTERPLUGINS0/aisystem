/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffectType
 */
package com.andrei1058.bedwars.arena.feature;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.metrics.MetricsManager;
import java.util.LinkedList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

public class SpoilPlayerTNTFeature {
    private static SpoilPlayerTNTFeature instance;
    private final LinkedList<Player> playersWithTnt = new LinkedList();

    private SpoilPlayerTNTFeature() {
        Bukkit.getPluginManager().registerEvents((Listener)new TNTListener(), (Plugin)BedWars.plugin);
        Bukkit.getScheduler().runTaskTimer((Plugin)BedWars.plugin, (Runnable)new ParticleTask(), 20L, 1L);
    }

    public static void init() {
        boolean enable = BedWars.config.getBoolean("performance-settings.spoil-tnt-players");
        if (enable && instance == null) {
            instance = new SpoilPlayerTNTFeature();
        }
        MetricsManager.appendPie("tnt_spoil_enable", () -> String.valueOf(enable));
    }

    private static class TNTListener
    implements Listener {
        private TNTListener() {
        }

        @EventHandler
        public void onDie(PlayerKillEvent event) {
            SpoilPlayerTNTFeature.instance.playersWithTnt.remove(event.getVictim());
        }

        @EventHandler
        public void onLeave(PlayerLeaveArenaEvent event) {
            SpoilPlayerTNTFeature.instance.playersWithTnt.remove(event.getPlayer());
        }

        @EventHandler(ignoreCancelled=true)
        public void onPickUp(PlayerPickupItemEvent event) {
            if (event.getItem().getItemStack().getType() == Material.TNT) {
                IArena arena = Arena.getArenaByPlayer(event.getPlayer());
                if (arena == null || !arena.isPlayer(event.getPlayer()) || arena.isSpectator(event.getPlayer())) {
                    return;
                }
                if (SpoilPlayerTNTFeature.instance.playersWithTnt.contains(event.getPlayer())) {
                    return;
                }
                SpoilPlayerTNTFeature.instance.playersWithTnt.add(event.getPlayer());
            }
        }

        @EventHandler(ignoreCancelled=true)
        public void onDrop(PlayerDropItemEvent event) {
            if (event.getItemDrop().getItemStack().getType() == Material.TNT) {
                IArena arena = Arena.getArenaByPlayer(event.getPlayer());
                if (arena == null || !arena.isPlayer(event.getPlayer()) || arena.isSpectator(event.getPlayer())) {
                    return;
                }
                if (!SpoilPlayerTNTFeature.instance.playersWithTnt.contains(event.getPlayer())) {
                    return;
                }
                if (event.getPlayer().getInventory().contains(Material.TNT)) {
                    return;
                }
                SpoilPlayerTNTFeature.instance.playersWithTnt.remove(event.getPlayer());
            }
        }

        @EventHandler(ignoreCancelled=true)
        public void onPlace(BlockPlaceEvent event) {
            ItemStack inHand = event.getItemInHand();
            IArena arena = Arena.getArenaByPlayer(event.getPlayer());
            if (arena == null || !arena.isPlayer(event.getPlayer()) || arena.isSpectator(event.getPlayer())) {
                return;
            }
            if (inHand.getType() == Material.TNT) {
                if (!SpoilPlayerTNTFeature.instance.playersWithTnt.contains(event.getPlayer())) {
                    return;
                }
                Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                    if (!event.getPlayer().getInventory().contains(Material.TNT)) {
                        SpoilPlayerTNTFeature.instance.playersWithTnt.remove(event.getPlayer());
                    }
                }, 1L);
            }
        }

        @EventHandler(ignoreCancelled=true)
        public void inventorySwitch(InventoryCloseEvent event) {
            Player player = (Player)event.getPlayer();
            IArena arena = Arena.getArenaByPlayer(player);
            if (arena == null || !arena.isPlayer(player) || arena.isSpectator(player)) {
                return;
            }
            boolean hasTnt = player.getInventory().contains(Material.TNT);
            if (SpoilPlayerTNTFeature.instance.playersWithTnt.contains(player)) {
                if (!hasTnt) {
                    SpoilPlayerTNTFeature.instance.playersWithTnt.remove(player);
                }
            } else if (hasTnt) {
                SpoilPlayerTNTFeature.instance.playersWithTnt.add(player);
            }
        }
    }

    private static class ParticleTask
    implements Runnable {
        private ParticleTask() {
        }

        @Override
        public void run() {
            for (Player player : SpoilPlayerTNTFeature.instance.playersWithTnt) {
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    return;
                }
                BedWars.nms.playRedStoneDot(player);
            }
        }
    }
}

