/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.api.arena.stats;

import com.andrei1058.bedwars.api.arena.stats.DefaultStatistics;
import com.andrei1058.bedwars.api.arena.stats.GameStatistic;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface PlayerGameStats {
    @NotNull
    public UUID getPlayer();

    @NotNull
    public String getDisplayPlayer();

    @NotNull
    public String getUsername();

    public void registerStatistic(@NotNull String var1, @NotNull GameStatistic<?> var2);

    public Optional<GameStatistic<?>> getStatistic(@NotNull String var1);

    default public Optional<GameStatistic<?>> getStatistic(@NotNull DefaultStatistics id) {
        return this.getStatistic(id.toString());
    }

    public List<String> getRegistered();
}

