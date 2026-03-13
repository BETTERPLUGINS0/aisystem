/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.api.arena.stats;

import com.andrei1058.bedwars.api.arena.stats.GameStatistic;
import com.andrei1058.bedwars.api.language.Language;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public interface GameStatisticProvider<T extends GameStatistic<?>> {
    public String getIdentifier();

    public Plugin getOwner();

    public T getDefault();

    public String getVoidReplacement(@Nullable Language var1);
}

