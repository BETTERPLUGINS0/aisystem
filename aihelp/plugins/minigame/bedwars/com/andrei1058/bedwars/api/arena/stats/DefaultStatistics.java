/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.api.arena.stats;

import org.jetbrains.annotations.NotNull;

public enum DefaultStatistics {
    KILLS("kills", true),
    KILLS_FINAL("finalKills", true),
    DEATHS("deaths", true),
    DEATHS_FINAL("finalDeaths", true),
    BEDS_DESTROYED("bedsDestroyed", true);

    private final String id;
    private final boolean incrementable;

    private DefaultStatistics(String id, boolean incrementable) {
        this.id = id;
        this.incrementable = incrementable;
    }

    @NotNull
    public String toString() {
        return this.id;
    }

    public boolean isIncrementable() {
        return this.incrementable;
    }
}

