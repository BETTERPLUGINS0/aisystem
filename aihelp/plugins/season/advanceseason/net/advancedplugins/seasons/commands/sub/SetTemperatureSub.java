/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.commands.sub;

import net.advancedplugins.as.impl.utils.commands.SimpleCommand;
import net.advancedplugins.as.impl.utils.text.Text;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.commands.sub.ASSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetTemperatureSub
extends ASSubCommand {
    public SetTemperatureSub(JavaPlugin javaPlugin, SimpleCommand simpleCommand) {
        super(javaPlugin, "advancedseasons.temperature", simpleCommand);
        this.setDescription("Set your current temperature");
        this.addFlat("temp");
        this.addArgument(Integer.class, "temperature", "1");
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
        int n = Integer.parseInt(stringArray[1]);
        Core.getTemperatureHandler().setTemperature((Player)commandSender, n);
        commandSender.sendMessage(Text.modify("&6AdvancedSeasons &fSet your temperature to &a" + n + "&f."));
        commandSender.sendMessage(Text.modify("&6AdvancedSeasons &fTo reset, use &f`/as temp 0`&f to reset your temperature."));
    }
}

