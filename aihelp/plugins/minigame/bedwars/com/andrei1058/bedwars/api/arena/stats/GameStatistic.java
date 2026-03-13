/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.api.arena.stats;

import com.andrei1058.bedwars.api.language.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GameStatistic<T>
extends Comparable<GameStatistic<T>> {
    public T getValue();

    public String getDisplayValue(@Nullable Language var1);

    @Override
    public int compareTo(@NotNull GameStatistic<T> var1);
}

