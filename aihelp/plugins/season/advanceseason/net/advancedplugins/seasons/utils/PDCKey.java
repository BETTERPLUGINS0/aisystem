/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.seasons.utils;

public enum PDCKey {
    WORLD_SEASON("world_season"),
    YEAR_DAY("year_day"),
    TEMPERATURE_UNITS("temperature_units"),
    TRANSITION_TIME("transition_time");

    private final String key;

    private PDCKey(String string2) {
        this.key = string2;
    }

    public String getKey() {
        return this.key;
    }
}

