/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.arena.stats.defaults;

import com.andrei1058.bedwars.api.arena.stats.GameStatistic;
import com.andrei1058.bedwars.api.arena.stats.PlayerGameStats;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerGameStatsContainer
implements PlayerGameStats {
    private final UUID player;
    private final String username;
    private final String lastDisplayName;
    private final HashMap<String, Optional<GameStatistic<?>>> statsById = new HashMap();

    public PlayerGameStatsContainer(@NotNull Player player) {
        this.player = player.getUniqueId();
        this.username = player.getName();
        this.lastDisplayName = player.getDisplayName();
    }

    @Override
    @NotNull
    public UUID getPlayer() {
        return this.player;
    }

    @Override
    @NotNull
    public String getDisplayPlayer() {
        Player online = Bukkit.getPlayer((UUID)this.getPlayer());
        if (null == online) {
            return this.lastDisplayName;
        }
        return online.getDisplayName();
    }

    @Override
    @NotNull
    public String getUsername() {
        return this.username;
    }

    @Override
    public void registerStatistic(@NotNull String id, @NotNull GameStatistic<?> defaultValue) {
        if (this.statsById.containsKey(id)) {
            throw new RuntimeException("Statistic " + id + " already registered for player " + String.valueOf(this.getPlayer()));
        }
        this.statsById.put(id, Optional.of(defaultValue));
    }

    @Override
    @Nullable
    public Optional<GameStatistic<?>> getStatistic(@NotNull String id) {
        return this.statsById.getOrDefault(id, Optional.empty());
    }

    @Override
    public List<String> getRegistered() {
        return (List)this.statsById.keySet().stream().collect(Collectors.toUnmodifiableList());
    }
}

