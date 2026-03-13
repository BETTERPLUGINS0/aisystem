/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.entity.ItemSpawnEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.event.weather.WeatherChangeEvent
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.Plugin;

public class HungerWeatherSpawn
implements Listener {
    private final boolean hungerWaiting = BedWars.config.getYml().getBoolean("allow-hunger-depletion.waiting");
    private final boolean hungerIngame = BedWars.config.getYml().getBoolean("allow-hunger-depletion.ingame");

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player player = (Player)e.getEntity();
        IArena arena = Arena.getArenaByPlayer(player);
        if (arena == null && BedWars.getServerType() == ServerType.SHARED) {
            return;
        }
        if (arena == null) {
            e.setCancelled(true);
            return;
        }
        if (arena.isSpectator(player)) {
            e.setCancelled(true);
            return;
        }
        switch (arena.getStatus()) {
            case waiting: 
            case starting: 
            case restarting: {
                e.setCancelled(!this.hungerWaiting);
                break;
            }
            case playing: {
                e.setCancelled(!this.hungerIngame);
            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.toWeatherState()) {
            if (BedWars.getServerType() == ServerType.SHARED) {
                if (Arena.getArenaByIdentifier(e.getWorld().getName()) != null) {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            if (BedWars.getServerType() != ServerType.BUNGEE) {
                if (Arena.getArenaByIdentifier(e.getEntity().getWorld().getName()) != null) {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent e) {
        IArena a = Arena.getArenaByPlayer(e.getPlayer());
        if (a == null) {
            return;
        }
        switch (e.getItem().getType()) {
            case GLASS_BOTTLE: {
                BedWars.nms.minusAmount(e.getPlayer(), e.getItem(), 1);
                break;
            }
            case MILK_BUCKET: {
                e.setCancelled(true);
                BedWars.nms.minusAmount(e.getPlayer(), e.getItem(), 1);
                int task = Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                    Arena.magicMilk.remove(e.getPlayer().getUniqueId());
                    BedWars.debug("PlayerItemConsumeEvent player " + String.valueOf(e.getPlayer()) + " was removed from magicMilk");
                }, 600L).getTaskId();
                Arena.magicMilk.put(e.getPlayer().getUniqueId(), task);
            }
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        Location l = e.getEntity().getLocation();
        IArena a = Arena.getArenaByIdentifier(l.getWorld().getName());
        if (a == null) {
            return;
        }
        if (a.getStatus() != GameState.playing) {
            e.setCancelled(true);
        }
    }
}

