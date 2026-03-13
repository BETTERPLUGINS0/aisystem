/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.utils;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.DataHandler;
import net.advancedplugins.seasons.enums.SeasonType;
import org.bukkit.plugin.java.JavaPlugin;

public class CalendarMenuConfig
extends DataHandler {
    private ImmutableMap<SeasonType, int[]> slots;
    private ImmutableMap<SeasonType, List<String>> slotNames;

    public CalendarMenuConfig(JavaPlugin javaPlugin) {
        super("menus/calendarMenu", javaPlugin);
        this.loadSlots();
        this.loadSlotNames();
    }

    private void loadSlots() {
        ImmutableMap.Builder<SeasonType, int[]> builder = ImmutableMap.builder();
        for (SeasonType seasonType : SeasonType.values()) {
            builder.put(seasonType, ASManager.getSlots(this.getString("settings." + seasonType.name() + ".slots")));
        }
        this.slots = builder.build();
    }

    private void loadSlotNames() {
        ImmutableMap.Builder<SeasonType, List<String>> builder = ImmutableMap.builder();
        for (SeasonType seasonType : SeasonType.values()) {
            builder.put(seasonType, this.getStringList("settings." + seasonType.name() + ".slotNames"));
        }
        this.slotNames = builder.build();
    }

    public ImmutableMap<SeasonType, int[]> getSlots() {
        return this.slots;
    }

    public ImmutableMap<SeasonType, List<String>> getSlotNames() {
        return this.slotNames;
    }
}

