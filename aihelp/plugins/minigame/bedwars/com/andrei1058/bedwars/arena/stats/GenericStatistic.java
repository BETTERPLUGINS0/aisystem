/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.arena.stats;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.stats.GameStatistic;
import com.andrei1058.bedwars.api.arena.stats.GameStatisticProvider;
import com.andrei1058.bedwars.api.arena.stats.Incrementable;
import com.andrei1058.bedwars.api.language.Language;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GenericStatistic
implements GameStatisticProvider<Value> {
    @Override
    public Value getDefault() {
        return new Value();
    }

    @Override
    public String getVoidReplacement(@Nullable Language language) {
        return "0";
    }

    @Override
    public Plugin getOwner() {
        return BedWars.plugin;
    }

    public static class Value
    implements GameStatistic<Integer>,
    Incrementable,
    Comparable<GameStatistic<Integer>> {
        private int count = 0;

        @Override
        public Integer getValue() {
            return this.count;
        }

        @Override
        public String getDisplayValue(Language language) {
            return String.valueOf(this.getValue());
        }

        @Override
        public int compareTo(@NotNull GameStatistic<Integer> o) {
            return Integer.compare(this.count, o.getValue());
        }

        @Override
        public void increment() {
            ++this.count;
        }
    }
}

