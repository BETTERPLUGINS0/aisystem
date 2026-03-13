/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.handlers;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.DataHandler;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.data.StorageHandler;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.handlers.sub.CalendarData;
import net.advancedplugins.seasons.handlers.sub.TimeData;
import net.advancedplugins.seasons.handlers.sub.WorldTimeController;
import net.advancedplugins.seasons.utils.CalendarMenuConfig;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class CalendarHandler
extends DataHandler {
    private ImmutableMap<SeasonType, TimeData> seasonCalendar;
    private final HashMap<String, CalendarData> calendarData = new HashMap();
    private final WorldTimeController worldTimeController;
    private int seasonDays;
    private int transitionDays;
    private int fullSeasonDays;
    private int yearDays;
    private int timePerTransition;
    private final CalendarMenuConfig menuConfig;

    public CalendarHandler(JavaPlugin javaPlugin) {
        super("calendar", javaPlugin);
        this.menuConfig = new CalendarMenuConfig(javaPlugin);
        this.load(javaPlugin);
        this.worldTimeController = new WorldTimeController(javaPlugin, this);
    }

    private void load(JavaPlugin javaPlugin) {
        HashMap<SeasonType, TimeData> hashMap = new HashMap<SeasonType, TimeData>();
        for (World object : Core.getWorldHandler().getWorlds()) {
            this.calendarData.put(object.getName(), CalendarData.builder().yearDay(StorageHandler.getYearDay(object.getName())).transitionPassedTime(StorageHandler.getTransitionTime(object.getName())).build());
        }
        this.seasonDays = this.getInt("calendar.seasonDays");
        this.transitionDays = this.getInt("calendar.transitionDays");
        this.timePerTransition = this.transitionDays * 24000 / 4;
        this.fullSeasonDays = this.seasonDays + this.transitionDays;
        this.yearDays = this.fullSeasonDays * 4;
        for (String string : this.getKeys("time")) {
            SeasonType seasonType = SeasonType.valueOf(string.toUpperCase());
            hashMap.put(seasonType, new TimeData().setDayTime(this.getInt("time." + string + ".dayLength") * 60).setNightTime(this.getInt("time." + string + ".nightLength") * 60));
        }
        this.seasonCalendar = ASManager.toImmutable(hashMap);
    }

    public TimeData getCycleLengths(SeasonType seasonType) {
        return this.seasonCalendar.get((Object)seasonType);
    }

    public void addTime(int n, String string) {
        if (!this.isTransitionDay(string)) {
            return;
        }
        if (this.getBoolean("progression.paused", false)) {
            return;
        }
        CalendarData calendarData = this.getCalendarData(string);
        calendarData.setTransitionPassedTime(calendarData.getTransitionPassedTime() + n);
        if (calendarData.getTransitionPassedTime() < this.timePerTransition) {
            return;
        }
        calendarData.setTransitionPassedTime(0);
        if (Core.getSeasonHandler().getSeason(string).getTransition() < 3) {
            Core.getSeasonHandler().changeSeason(string);
        }
    }

    public void triggerDateChange(String string) {
        if (this.getBoolean("progression.paused", false)) {
            return;
        }
        CalendarData calendarData = this.getCalendarData(string);
        if (calendarData.getYearDay() + 1 >= this.yearDays) {
            calendarData.setYearDay(0);
        } else {
            calendarData.increaseYearDay();
        }
        int n = calendarData.getYearDay() % this.fullSeasonDays;
        if (n == 0) {
            Core.getSeasonHandler().changeSeason(string);
        }
    }

    public boolean isTransitionDay(String string) {
        CalendarData calendarData = this.getCalendarData(string);
        int n = calendarData.getYearDay() % this.fullSeasonDays;
        return n >= this.seasonDays && n < this.fullSeasonDays;
    }

    public void recalc(String string) {
        CalendarData calendarData = this.getCalendarData(string);
        SeasonType seasonType = SeasonType.valueOf(Core.getSeasonHandler().getSeason(string).name().split("_")[0]);
        int n = ASManager.minmax(Core.getSeasonHandler().getSeason(string).getTransition() - 1, 0, 2);
        int n2 = this.fullSeasonDays / 3 * n;
        calendarData.setYearDay((seasonType.getId() - 1) * this.fullSeasonDays + n2);
        calendarData.setTransitionPassedTime(0);
    }

    public CalendarData getCalendarData(String string2) {
        return this.calendarData.computeIfAbsent(string2, string -> CalendarData.builder().build());
    }

    public CalendarData getCalendarData(World world) {
        return this.getCalendarData(world.getName());
    }

    public int[] getSlots(SeasonType seasonType) {
        return this.menuConfig.getSlots().get((Object)seasonType);
    }

    public String getSlotName(SeasonType seasonType, int n) {
        return this.menuConfig.getSlotNames().isEmpty() || this.menuConfig.getSlotNames().get((Object)seasonType).size() < n ? "" : this.menuConfig.getSlotNames().get((Object)seasonType).get(n);
    }

    @Override
    public void unload() {
        for (World world : Core.getWorldHandler().getWorlds()) {
            CalendarData calendarData = this.getCalendarData(world);
            StorageHandler.setYearDay(calendarData.getYearDay(), world.getName());
            StorageHandler.setTransitionTime(calendarData.getTransitionPassedTime(), world.getName());
        }
        super.unload();
    }

    public ImmutableMap<SeasonType, TimeData> getSeasonCalendar() {
        return this.seasonCalendar;
    }

    public HashMap<String, CalendarData> getCalendarData() {
        return this.calendarData;
    }

    public WorldTimeController getWorldTimeController() {
        return this.worldTimeController;
    }

    public int getSeasonDays() {
        return this.seasonDays;
    }

    public int getTransitionDays() {
        return this.transitionDays;
    }

    public int getFullSeasonDays() {
        return this.fullSeasonDays;
    }

    public int getYearDays() {
        return this.yearDays;
    }

    public int getTimePerTransition() {
        return this.timePerTransition;
    }

    public CalendarMenuConfig getMenuConfig() {
        return this.menuConfig;
    }
}

