/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.seasons.objects;

public class CustomWorld {
    private final String name;
    private final boolean generateCustomGrass;

    public CustomWorld(String string, boolean bl) {
        this.name = string;
        this.generateCustomGrass = bl;
    }

    public String getName() {
        return this.name;
    }

    public boolean isGenerateCustomGrass() {
        return this.generateCustomGrass;
    }
}

