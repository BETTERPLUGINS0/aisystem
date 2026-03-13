/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.seasons.api.biome;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public enum BiomeVisualKey {
    SKY,
    WATER,
    WATERFOG,
    GRASS,
    TREE,
    FOG;


    @NotNull
    public String lowercasedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    @NotNull
    public BiomeVisualKey from(String string) {
        return BiomeVisualKey.valueOf(string.toLowerCase(Locale.ROOT));
    }
}

