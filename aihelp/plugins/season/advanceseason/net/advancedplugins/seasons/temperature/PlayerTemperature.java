/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.seasons.temperature;

import net.advancedplugins.seasons.enums.TemperatureEvent;

public class PlayerTemperature {
    public boolean init = false;
    public int realTemperature = 0;
    public int displayTemperature = 0;
    public long lastShow = System.currentTimeMillis();
    public TemperatureEvent lastEvent = null;
    public int boostTemperature;

    public int getDisplayTemperature() {
        return this.displayTemperature + this.boostTemperature;
    }
}

