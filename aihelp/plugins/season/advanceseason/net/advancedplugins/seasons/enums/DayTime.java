/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.seasons.enums;

import java.util.Locale;

public enum DayTime {
    NIGHT,
    MORNING,
    DAY,
    EVENING;


    public static DayTime fromString(String string) {
        try {
            return DayTime.valueOf(string.toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            return null;
        }
    }
}

