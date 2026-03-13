/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.handlers;

import java.util.HashSet;
import java.util.Locale;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.DataHandler;
import net.advancedplugins.as.impl.utils.text.Text;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.enums.DayTime;
import net.advancedplugins.seasons.handlers.sub.SeasonalEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SeasonEventsHandler
extends DataHandler {
    private final HashSet<SeasonalEvent> seasonalEvents = new HashSet();

    public SeasonEventsHandler(JavaPlugin javaPlugin) {
        super("events", javaPlugin);
        this.load();
    }

    private void load() {
        for (String string : this.getKeys("events")) {
            this.seasonalEvents.add(new SeasonalEvent(this.getString("events." + string + ".name"), this.getInt("events." + string + ".day"), DayTime.fromString(this.getString("events." + string + ".time")), this.getStringList("events." + string + ".broadcast"), this.getStringList("events." + string + ".commands")));
        }
    }

    public void trigger(int n, DayTime dayTime, String string3) {
        for (SeasonalEvent seasonalEvent2 : (SeasonalEvent[])this.seasonalEvents.stream().filter(seasonalEvent -> seasonalEvent.getDay() == n && seasonalEvent.getTime() == dayTime).toArray(SeasonalEvent[]::new)) {
            seasonalEvent2.getCommands().forEach(string2 -> {
                if (string2.startsWith("[FOR ALL PLAYERS]")) {
                    for (Player player2 : (Player[])Bukkit.getOnlinePlayers().stream().filter(player -> player.getWorld().getName().equals(string3)).toArray(Player[]::new)) {
                        Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), string2.replace("[FOR ALL PLAYERS] ", "").replace("[FOR ALL PLAYERS]", "").replace("%player%", player2.getName()));
                    }
                } else {
                    Bukkit.getServer().getOnlinePlayers().forEach(player -> Bukkit.getServer().dispatchCommand((CommandSender)player, string2));
                }
            });
            seasonalEvent2.getMessages().forEach(string -> Bukkit.getServer().broadcastMessage(Text.modify(string)));
        }
    }

    public String formatAllEvents(String string, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        SeasonalEvent[] seasonalEventArray = (SeasonalEvent[])this.seasonalEvents.stream().filter(seasonalEvent -> seasonalEvent.getDay() == n).toArray(SeasonalEvent[]::new);
        if (seasonalEventArray.length == 0) {
            return Core.getCalendarHandler().getMenuConfig().getString("settings.eventFormat.none");
        }
        for (SeasonalEvent seasonalEvent2 : seasonalEventArray) {
            stringBuilder.append(this.formatEventString(seasonalEvent2, string)).append("\n");
        }
        return stringBuilder.toString();
    }

    public String formatEventString(SeasonalEvent seasonalEvent, String string) {
        return string.replace("%event name%", seasonalEvent.getName()).replace("%day%", String.valueOf(Core.getLocaleHandler().getString("times." + seasonalEvent.getTime().name()))).replace("%time%", ASManager.capitalize(seasonalEvent.getTime().toString().toLowerCase(Locale.ROOT)));
    }
}

