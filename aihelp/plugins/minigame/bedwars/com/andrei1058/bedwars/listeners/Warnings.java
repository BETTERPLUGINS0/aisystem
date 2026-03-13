/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class Warnings
implements Listener {
    private final BedWars plugin;

    public Warnings(BedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!player.isOp()) {
            return;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> player.sendMessage(String.valueOf(ChatColor.RED) + "[BedWars1058] Multiverse-Core detected! Please remove it or make sure it won't touch BedWars maps!"), 5L);
        }
        if (Bukkit.getServer().getSpawnRadius() > 0) {
            Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> player.sendMessage(String.valueOf(ChatColor.RED) + "[BedWars1058] Your spawn-protection in server.properties is enabled. " + String.valueOf(ChatColor.YELLOW) + "This might mess with BedWars arenas!" + String.valueOf(ChatColor.GRAY) + " It is highly reccomend setting it to 0."), 5L);
        }
    }
}

