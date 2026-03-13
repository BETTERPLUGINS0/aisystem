/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.persistence.PersistentDataHolder
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.commands;

import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.commands.SimpleCommand;
import net.advancedplugins.as.impl.utils.pdc.PDCHandler;
import net.advancedplugins.as.impl.utils.text.Text;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.utils.PDCKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.java.JavaPlugin;

public class ToggleUnitsCommand
extends SimpleCommand<CommandSender> {
    public ToggleUnitsCommand(JavaPlugin javaPlugin) {
        super(javaPlugin, "toggleunits", "advancedseasons.toggleunits", false);
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
        Player player = (Player)commandSender;
        String string = Core.getTemperatureHandler().getPlayerUnits(player);
        String string2 = "fahrenheit".equalsIgnoreCase(string) ? "celsius" : "fahrenheit";
        PDCHandler.setString((PersistentDataHolder)player, PDCKey.TEMPERATURE_UNITS.getKey(), string2);
        Text.sendMessage((CommandSender)player, Core.getLocaleHandler().getString("messages.toggleUnits", "%prefix% &fYou have toggled your temperature units to &b%unit%!").replace("%unit%", ASManager.capitalize(string2)));
    }
}

