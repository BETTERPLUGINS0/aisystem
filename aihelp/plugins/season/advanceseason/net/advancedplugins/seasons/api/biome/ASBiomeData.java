/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.NamespacedKey
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.UnmodifiableView
 */
package net.advancedplugins.seasons.api.biome;

import java.util.List;
import java.util.Map;
import net.advancedplugins.seasons.api.biome.BiomeVisualKey;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

public record ASBiomeData(@NotNull NamespacedKey key, @NotNull @UnmodifiableView List<NamespacedKey> subBiomes, int temperature, @NotNull @UnmodifiableView Map<String, SeasonData> seasonDataMap) {
    @NotNull
    public NamespacedKey key() {
        return this.key;
    }

    @NotNull
    public @UnmodifiableView List<NamespacedKey> subBiomes() {
        return this.subBiomes;
    }

    @NotNull
    public @UnmodifiableView Map<String, SeasonData> seasonDataMap() {
        return this.seasonDataMap;
    }

    public record SeasonData(boolean snowy, @UnmodifiableView Map<BiomeVisualKey, String> visualValues) {
    }
}

