/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameRule
 *  org.bukkit.World
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.handlers.sub;

import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.enums.DayTime;
import net.advancedplugins.seasons.handlers.CalendarHandler;
import net.advancedplugins.seasons.handlers.sub.TimeData;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldTimeController
implements Listener {
    private final CalendarHandler handler;

    public WorldTimeController(JavaPlugin javaPlugin, CalendarHandler calendarHandler) {
        this.handler = calendarHandler;
        this.initWorlds(javaPlugin);
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        this.start(javaPlugin);
    }

    private void initWorlds(JavaPlugin javaPlugin) {
        Bukkit.getScheduler().runTaskLater((Plugin)javaPlugin, () -> {
            for (World world : Core.getWorldHandler().getWorlds()) {
                if (!((Boolean)world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)).booleanValue()) continue;
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, (Object)false);
            }
        }, 20L);
    }

    private void start(JavaPlugin javaPlugin) {
        this.handler.getActiveTasks().add(Bukkit.getScheduler().runTaskTimer((Plugin)javaPlugin, () -> {
            for (World world : Core.getWorldHandler().getWorlds()) {
                long l;
                TimeData timeData = this.getTimeData(world);
                long l2 = l = world.getTime();
                l2 = l < 12000L ? (l2 += (long)timeData.getDaySpeed()) : (l2 += (long)timeData.getNightSpeed());
                Core cfr_ignored_0 = (Core)ASManager.getInstance();
                if (!Core.Bukkit) {
                    l2 += 1000L;
                }
                world.setTime(l2);
                this.determineAndTriggerDayTimeChange(world, l, l2, timeData);
            }
        }, 30L, 2L).getTaskId());
    }

    private void determineAndTriggerDayTimeChange(World world, long l, long l2, TimeData timeData) {
        this.handler.addTime(l < 12000L ? timeData.getDaySpeed() : timeData.getNightSpeed(), world.getName());
        if (l < 18000L && l2 >= 18000L) {
            this.handler.triggerDateChange(world.getName());
        }
        long l3 = 18000L;
        long l4 = 1000L;
        long l5 = 6000L;
        long l6 = 12000L;
        if (l < 1000L && l2 >= 1000L) {
            this.triggerDayTimeEvent(DayTime.MORNING, world.getName());
        }
        if (l < 6000L && l2 >= 6000L) {
            this.triggerDayTimeEvent(DayTime.DAY, world.getName());
        }
        if (l < 12000L && l2 >= 12000L) {
            this.triggerDayTimeEvent(DayTime.EVENING, world.getName());
        }
        if (l < 18000L && l2 >= 18000L) {
            this.triggerDayTimeEvent(DayTime.NIGHT, world.getName());
        }
    }

    private void triggerDayTimeEvent(DayTime dayTime, String string) {
        Core.getEventsHandler().trigger(Core.getCalendarHandler().getCalendarData(string).getYearDay() + 1, dayTime, string);
    }

    public void sleepWorld(World world) {
        TimeData timeData = this.getTimeData(world);
        for (int i = 0; i < this.calculateTimeLoops(world); ++i) {
            int n = (int)world.getTime();
            world.setTime(world.getTime() + 1000L);
            this.determineAndTriggerDayTimeChange(world, n, world.getTime(), timeData);
        }
        world.setTime(0L);
        this.determineAndTriggerDayTimeChange(world, 23900L, 23999L, timeData);
    }

    private TimeData getTimeData(World world) {
        return this.handler.getCycleLengths(Core.getSeasonHandler().getSeason(world).getType());
    }

    private int calculateTimeLoops(World world) {
        long l = world.getTime();
        long l2 = 24000L;
        return (int)((24000L - l) / 1000L);
    }
}

