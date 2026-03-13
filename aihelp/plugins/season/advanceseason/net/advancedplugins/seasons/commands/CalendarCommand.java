/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.commands;

import net.advancedplugins.as.impl.utils.commands.SimpleCommand;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.menus.CalendarMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CalendarCommand
extends SimpleCommand<CommandSender> {
    public CalendarCommand(JavaPlugin javaPlugin) {
        super(javaPlugin, "calendar", "advancedseasons.calendar", true);
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
        Player player = (Player)commandSender;
        new CalendarMenu(player, Core.getCalendarHandler()).openInventory();
    }
}

