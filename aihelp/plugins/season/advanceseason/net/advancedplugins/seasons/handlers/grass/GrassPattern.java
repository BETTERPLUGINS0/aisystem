/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package net.advancedplugins.seasons.handlers.grass;

import com.google.common.collect.ImmutableList;
import org.bukkit.Material;

public class GrassPattern {
    private ImmutableList<Material> materials;
    private final int patchInterval;
    private final int patchRadius;
    private final int offset;
    private final int chance;

    public GrassPattern(ImmutableList<Material> immutableList, int n, int n2, int n3, int n4) {
        this.materials = immutableList;
        this.patchInterval = n;
        this.patchRadius = n2;
        this.offset = n3;
        this.chance = n4;
    }

    public ImmutableList<Material> getMaterials() {
        return this.materials;
    }

    public int getPatchInterval() {
        return this.patchInterval;
    }

    public int getPatchRadius() {
        return this.patchRadius;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getChance() {
        return this.chance;
    }
}

