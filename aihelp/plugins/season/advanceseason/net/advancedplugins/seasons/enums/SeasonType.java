/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.seasons.enums;

public enum SeasonType {
    SPRING(1),
    SUMMER(2),
    FALL(3),
    WINTER(4);

    private final int id;

    private SeasonType(int n2) {
        this.id = n2;
    }

    public int getId() {
        return this.id;
    }
}

