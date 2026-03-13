/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.stats;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.stats.PlayerStats;
import com.andrei1058.bedwars.stats.StatsListener;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StatsManager {
    private final Map<UUID, PlayerStats> stats = new ConcurrentHashMap<UUID, PlayerStats>();

    public StatsManager() {
        this.registerListeners();
    }

    public void remove(UUID uuid) {
        this.stats.remove(uuid);
    }

    public void put(UUID uuid, PlayerStats playerStats) {
        this.stats.put(uuid, playerStats);
    }

    @NotNull
    public PlayerStats get(UUID uuid) {
        PlayerStats playerStats = this.stats.get(uuid);
        if (playerStats == null) {
            throw new IllegalStateException("Trying to get stats data of an unloaded player!");
        }
        return playerStats;
    }

    @Nullable
    public PlayerStats getUnsafe(UUID uuid) {
        return this.stats.get(uuid);
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents((Listener)new StatsListener(), (Plugin)BedWars.plugin);
    }
}

