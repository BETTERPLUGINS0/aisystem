/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.arena.stats;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.stats.DefaultStatistics;
import com.andrei1058.bedwars.api.arena.stats.GameStatistic;
import com.andrei1058.bedwars.api.arena.stats.GameStatisticProvider;
import com.andrei1058.bedwars.api.arena.stats.GameStatsHolder;
import com.andrei1058.bedwars.api.arena.stats.PlayerGameStats;
import com.andrei1058.bedwars.arena.stats.GenericStatistic;
import com.andrei1058.bedwars.arena.stats.defaults.PlayerGameStatsContainer;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GameStatsManager
implements GameStatsHolder {
    private final HashMap<String, GameStatisticProvider<?>> registeredStats = new HashMap();
    private final HashMap<UUID, Optional<PlayerGameStats>> playerSessionStats = new HashMap();
    private final IArena arena;

    public GameStatsManager(IArena arena) {
        this.arena = arena;
        for (final DefaultStatistics statistic : DefaultStatistics.values()) {
            if (!statistic.isIncrementable()) continue;
            this.register(new GenericStatistic(){

                @Override
                public String getIdentifier() {
                    return statistic.toString();
                }
            });
        }
    }

    @Override
    public void register(@NotNull GameStatisticProvider<?> statistic) {
        if (statistic.getIdentifier().isBlank()) {
            throw new RuntimeException("Identifier cannot be blank: " + statistic.getClass().getName());
        }
        if (!statistic.getIdentifier().trim().equals(statistic.getIdentifier())) {
            throw new RuntimeException("Identifier should not start/end with white spaces: " + statistic.getClass().getName());
        }
        if (null != this.registeredStats.getOrDefault(statistic.getIdentifier(), null)) {
            throw new RuntimeException("Statistic already registered: " + statistic.getIdentifier());
        }
        this.registeredStats.put(statistic.getIdentifier(), statistic);
        BedWars.debug("Registered new game statistic: " + statistic.getIdentifier());
    }

    @Override
    public IArena getArena() {
        return this.arena;
    }

    @Override
    public PlayerGameStats init(@NotNull Player player) {
        if (this.playerSessionStats.containsKey(player.getUniqueId())) {
            throw new RuntimeException(player.getName() + " is already registered for game stats!");
        }
        PlayerGameStatsContainer stats = new PlayerGameStatsContainer(player);
        this.registeredStats.forEach((id, provider) -> stats.registerStatistic((String)id, (GameStatistic<?>)provider.getDefault()));
        this.playerSessionStats.put(player.getUniqueId(), Optional.of(stats));
        return stats;
    }

    @Override
    public void unregisterPlayer(UUID uuid) {
        if (this.getArena().getStatus() == GameState.restarting) {
            throw new RuntimeException("You cannot unregister player stats during restarting phase!");
        }
        this.playerSessionStats.remove(uuid);
    }

    @Override
    @NotNull
    public PlayerGameStats getCreate(@NotNull Player holder) {
        Optional ps = this.playerSessionStats.getOrDefault(holder.getUniqueId(), Optional.empty());
        if (ps.isEmpty()) {
            PlayerGameStats stats = this.init(holder);
            this.playerSessionStats.put(holder.getUniqueId(), Optional.of(stats));
            return stats;
        }
        return (PlayerGameStats)ps.get();
    }

    @Override
    public Optional<PlayerGameStats> get(@NotNull UUID holder) {
        return this.playerSessionStats.getOrDefault(holder, Optional.empty());
    }

    @Override
    public Collection<Optional<PlayerGameStats>> getTrackedPlayers() {
        return Collections.unmodifiableCollection(this.playerSessionStats.values());
    }

    @Override
    public List<Optional<PlayerGameStats>> getOrderedBy(@NotNull String statistic) {
        List<Optional<PlayerGameStats>> list = this.playerSessionStats.values().stream().filter(Optional::isPresent).filter(st -> ((PlayerGameStats)st.get()).getStatistic(statistic).isPresent()).sorted(Comparator.comparing(a -> ((PlayerGameStats)a.get()).getStatistic(statistic).get())).collect(Collectors.toList());
        Collections.reverse(list);
        return list;
    }

    @Override
    public boolean hasStatistic(String orderBy) {
        return this.registeredStats.containsKey(orderBy);
    }

    @Override
    public List<String> getRegistered() {
        return (List)this.registeredStats.keySet().stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Nullable
    public GameStatisticProvider<?> getProvider(String statistic) {
        return this.registeredStats.getOrDefault(statistic, null);
    }
}

