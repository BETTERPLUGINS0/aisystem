/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.api.arena.stats;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.stats.DefaultStatistics;
import com.andrei1058.bedwars.api.arena.stats.GameStatisticProvider;
import com.andrei1058.bedwars.api.arena.stats.PlayerGameStats;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GameStatsHolder {
    public IArena getArena();

    public void register(@NotNull GameStatisticProvider<?> var1);

    public PlayerGameStats init(Player var1);

    public void unregisterPlayer(UUID var1);

    @NotNull
    public PlayerGameStats getCreate(@NotNull Player var1);

    public Optional<PlayerGameStats> get(@NotNull UUID var1);

    default public Optional<PlayerGameStats> get(@NotNull Player holder) {
        return this.get(holder.getUniqueId());
    }

    public Collection<Optional<PlayerGameStats>> getTrackedPlayers();

    default public Collection<Optional<PlayerGameStats>> getOrderedBy(@NotNull DefaultStatistics statistic) {
        return this.getOrderedBy(statistic.toString());
    }

    public List<Optional<PlayerGameStats>> getOrderedBy(@NotNull String var1);

    public boolean hasStatistic(String var1);

    public List<String> getRegistered();

    @Nullable
    public GameStatisticProvider<?> getProvider(String var1);
}

