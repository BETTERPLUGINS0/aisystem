/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.world.ChunkLoadEvent
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;

public class ChunkLoad
implements Listener {
    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent e) {
        if (e == null) {
            return;
        }
        if (e.getChunk() == null) {
            return;
        }
        if (e.getChunk().getEntities() == null) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> {
            for (Entity entity : e.getChunk().getEntities()) {
                if (!(entity instanceof ArmorStand)) continue;
                if (entity.hasMetadata("bw1058-setup")) {
                    Bukkit.getScheduler().runTask((Plugin)BedWars.plugin, () -> ((Entity)entity).remove());
                    continue;
                }
                if (((ArmorStand)entity).isVisible() || !((ArmorStand)entity).isMarker() || !entity.isCustomNameVisible()) continue;
                if (!ChatColor.stripColor((String)entity.getCustomName()).contains(" SET")) {
                    if (!ChatColor.stripColor((String)entity.getCustomName()).contains(" set")) continue;
                }
                Bukkit.getScheduler().runTask((Plugin)BedWars.plugin, () -> ((Entity)entity).remove());
            }
        });
    }
}

