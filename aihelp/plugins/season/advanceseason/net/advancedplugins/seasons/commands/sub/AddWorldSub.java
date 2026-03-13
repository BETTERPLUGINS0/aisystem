/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.commands.sub;

import net.advancedplugins.as.impl.utils.commands.SimpleCommand;
import net.advancedplugins.as.impl.utils.text.Text;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.commands.sub.ASSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AddWorldSub
extends ASSubCommand {
    public AddWorldSub(JavaPlugin javaPlugin, SimpleCommand simpleCommand) {
        super(javaPlugin, "advancedseasons.addworld", simpleCommand);
        this.setDescription("Add Seasons to world");
        this.addFlat("addworld");
        this.addArgument(String.class, "world", (String[])Bukkit.getWorlds().stream().map(world -> world.getName()).toArray(String[]::new));
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
        String string = this.parseArgument(stringArray, 1, () -> ((Player)commandSender).getWorld().getName());
        if (Bukkit.getWorld((String)string) == null) {
            commandSender.sendMessage(Text.modify("&6AdvancedSeasons &cInvalid world " + string));
            return;
        }
        Core.getWorldHandler().addWorld(string);
        Core.getBiomesHandler().getRenderHandler().refreshVisualBiomes(true);
        commandSender.sendMessage(Text.modify("&6AdvancedSeasons &aAdded world &e" + string + "&a to AdvancedSeasons"));
    }
}

