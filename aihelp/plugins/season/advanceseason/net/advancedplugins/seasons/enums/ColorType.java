/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.seasons.enums;

public enum ColorType {
    SKY("sky"),
    WATER("water"),
    WATERFOG("waterFog"),
    GRASS("grass"),
    TREE("tree"),
    FOG("fog");

    private final String path;

    public String getPath() {
        return this.path;
    }

    private ColorType(String string2) {
        this.path = string2;
    }
}

