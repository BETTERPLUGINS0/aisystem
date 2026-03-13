/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.seasons.handlers.sub;

import java.util.List;
import net.advancedplugins.seasons.enums.DayTime;

public class SeasonalEvent {
    private final String name;
    private final int day;
    private final DayTime time;
    private final List<String> messages;
    private final List<String> commands;

    public SeasonalEvent(String string, int n, DayTime dayTime, List<String> list, List<String> list2) {
        this.name = string;
        this.day = n;
        this.time = dayTime;
        this.messages = list;
        this.commands = list2;
    }

    public String getName() {
        return this.name;
    }

    public int getDay() {
        return this.day;
    }

    public DayTime getTime() {
        return this.time;
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public List<String> getCommands() {
        return this.commands;
    }
}

