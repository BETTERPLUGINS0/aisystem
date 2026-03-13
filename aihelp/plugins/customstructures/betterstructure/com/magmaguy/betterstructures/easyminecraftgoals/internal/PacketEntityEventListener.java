/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 */
package com.magmaguy.betterstructures.easyminecraftgoals.internal;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketEntityTracker;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PacketEntityEventListener
implements Listener {
    private final PacketEntityTracker tracker;

    public PacketEntityEventListener(PacketEntityTracker tracker) {
        this.tracker = tracker;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.tracker.onPlayerJoin(event.getPlayer());
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.tracker.onPlayerQuit(event.getPlayer());
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        this.tracker.onPlayerRespawn(event.getPlayer());
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        World fromWorld = event.getFrom();
        this.tracker.onPlayerChangedWorld(event.getPlayer(), fromWorld);
    }
}

