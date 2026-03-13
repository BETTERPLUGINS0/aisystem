/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.seasons.menus;

import java.util.LinkedList;
import net.advancedplugins.as.impl.utils.menus.AdvancedMenu;
import net.advancedplugins.as.impl.utils.menus.item.AdvancedMenuItem;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.handlers.CalendarHandler;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class CalendarMenu
extends AdvancedMenu {
    public CalendarMenu(Player player, CalendarHandler calendarHandler) {
        super(player, (ConfigurationSection)calendarHandler.getMenuConfig().getConfig(), null);
        World world = player.getWorld();
        int n = 0;
        for (SeasonType seasonType : SeasonType.values()) {
            int n2 = 0;
            for (int n3 : calendarHandler.getSlots(seasonType)) {
                int n4 = n2 + 1;
                int n5 = n;
                AdvancedMenuItem advancedMenuItem = new AdvancedMenuItem(n3, calendarHandler.getMenuConfig().getConfig().getConfigurationSection("settings." + seasonType.name()), replacer -> replacer.set("day", "" + n4).set("slot name", calendarHandler.getSlotName(seasonType, n4 - 1)).set("events", Core.getEventsHandler().formatAllEvents(calendarHandler.getMenuConfig().getConfig().getString("settings.eventFormat.event"), n5 + 1)));
                if (n == calendarHandler.getCalendarData(world).getYearDay()) {
                    advancedMenuItem.setGlow();
                }
                this.addItem(advancedMenuItem, n3);
                ++n2;
                ++n;
            }
        }
    }

    public static String getMonth(CalendarHandler calendarHandler, String string) {
        if (!Core.getWorldHandler().isWorldEnabled(string)) {
            return "None";
        }
        int n = calendarHandler.getCalendarData(string).getYearDay();
        LinkedList linkedList = new LinkedList();
        for (SeasonType seasonType : SeasonType.values()) {
            linkedList.addAll(calendarHandler.getMenuConfig().getConfig().getStringList("settings." + seasonType.name() + ".slotNames"));
        }
        return (String)linkedList.get(n);
    }

    @Override
    public void openInventory() {
        super.openInventory();
    }
}

