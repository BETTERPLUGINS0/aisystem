/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.seasons.handlers.sub;

public class TimeData {
    private int dayTime;
    private int nightTime;
    private int daySpeed;
    private int nightSpeed;

    public TimeData setDayTime(int n) {
        this.dayTime = n;
        this.daySpeed = 12000 / (n * 10);
        return this;
    }

    public TimeData setNightTime(int n) {
        this.nightTime = n;
        this.nightSpeed = 12000 / (n * 10);
        return this;
    }

    public int getDayTime() {
        return this.dayTime;
    }

    public int getNightTime() {
        return this.nightTime;
    }

    public int getDaySpeed() {
        return this.daySpeed;
    }

    public int getNightSpeed() {
        return this.nightSpeed;
    }
}

