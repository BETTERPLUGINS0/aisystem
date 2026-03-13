/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.expansion.PlaceholderExpansion
 *  org.bukkit.OfflinePlayer
 */
package net.advancedplugins.seasons.utils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.advancedplugins.localization.LocaleHandler;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.enums.Season;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.handlers.CalendarHandler;
import net.advancedplugins.seasons.handlers.SeasonHandler;
import net.advancedplugins.seasons.handlers.WorldHandler;
import net.advancedplugins.seasons.menus.CalendarMenu;
import org.bukkit.OfflinePlayer;

public class PAPIPlaceholders
extends PlaceholderExpansion {
    public PAPIPlaceholders() {
        this.register();
    }

    public String onRequest(OfflinePlayer offlinePlayer, String string) {
        String[] stringArray = string.split("_");
        ArrayDeque<String> arrayDeque = new ArrayDeque<String>(Arrays.asList(stringArray).subList(1, stringArray.length));
        if (string.startsWith("season_")) {
            return this.resolveSeasonPlaceholder(offlinePlayer, arrayDeque);
        }
        if (string.startsWith("yearday_")) {
            String string2 = string.split("_")[1];
            return "" + Core.getCalendarHandler().getCalendarData(string2).getYearDay();
        }
        if (string.startsWith("month_")) {
            String string3 = string.split("_")[1];
            return CalendarMenu.getMonth(Core.getCalendarHandler(), string3);
        }
        if (string.equalsIgnoreCase("temperature_icon")) {
            return Core.getTemperatureHandler().getWeatherIcon(offlinePlayer.getPlayer().getWorld().getName());
        }
        if (string.equalsIgnoreCase("temperature")) {
            return String.valueOf(Core.getTemperatureHandler().getPlayerTemperature(offlinePlayer.getPlayer()));
        }
        if (string.startsWith("month")) {
            if (!offlinePlayer.isOnline()) {
                return "None";
            }
            String string4 = offlinePlayer.getPlayer().getWorld().getName();
            return CalendarMenu.getMonth(Core.getCalendarHandler(), string4);
        }
        if (string.startsWith("season")) {
            if (!offlinePlayer.isOnline()) {
                return "None";
            }
            String string5 = offlinePlayer.getPlayer().getWorld().getName();
            if (!Core.getWorldHandler().getEnabledWorlds().contains(string5)) {
                return "None";
            }
            return Core.getLocaleHandler().getString("seasons." + Core.getSeasonHandler().getSeason(string5).getType().name());
        }
        if (string.startsWith("yearday")) {
            if (!offlinePlayer.isOnline()) {
                return "0";
            }
            String string6 = offlinePlayer.getPlayer().getWorld().getName();
            if (!Core.getWorldHandler().getEnabledWorlds().contains(string6)) {
                return "";
            }
            return "" + Core.getCalendarHandler().getCalendarData(string6).getYearDay();
        }
        return super.onRequest(offlinePlayer, string);
    }

    public String getIdentifier() {
        return "advancedseasons";
    }

    public String getAuthor() {
        return "AdvancedPlugins";
    }

    public String getVersion() {
        return "1.0.0";
    }

    private String resolveSeasonPlaceholder(OfflinePlayer offlinePlayer, Queue<String> queue) {
        WorldHandler worldHandler;
        int n = queue.size();
        if (n == 0) {
            return null;
        }
        String string = queue.poll();
        SeasonHandler seasonHandler = Core.getSeasonHandler();
        if ("current".equalsIgnoreCase(string) && (worldHandler = offlinePlayer.getPlayer()) != null) {
            string = worldHandler.getWorld().getName();
        }
        if (!(worldHandler = Core.getWorldHandler()).isWorldEnabled(string)) {
            return "N/A";
        }
        Season season = seasonHandler.getSeason(string);
        SeasonType[] seasonTypeArray = SeasonType.values();
        int n2 = season.getType().ordinal();
        String string2 = queue.poll();
        if (string2 == null) {
            return this.translateSeason(season.getType());
        }
        if ("next".equalsIgnoreCase(string2)) {
            int n3 = n2 + 1 >= seasonTypeArray.length ? 0 : n2 + 1;
            SeasonType seasonType = seasonTypeArray[n3];
            return this.translateSeason(seasonType);
        }
        if ("remainingdays".equalsIgnoreCase(string2)) {
            return "" + this.remainingDays(string);
        }
        return null;
    }

    private int remainingDays(String string) {
        CalendarHandler calendarHandler = Core.getCalendarHandler();
        Season season = Core.getSeasonHandler().getSeason(string);
        int n = calendarHandler.getFullSeasonDays();
        int n2 = season.getTransition();
        if (n2 == 3) {
            return n;
        }
        int n3 = calendarHandler.getCalendarData(string).getYearDay();
        int n4 = n3 < n ? n3 : n3 % n;
        return n - n4;
    }

    private String translateSeason(SeasonType seasonType) {
        LocaleHandler localeHandler = Core.getLocaleHandler();
        return localeHandler.getString("seasons." + seasonType.name());
    }
}

