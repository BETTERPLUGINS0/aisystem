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

public class NextSeasonSub
extends ASSubCommand {
    public NextSeasonSub(JavaPlugin javaPlugin, SimpleCommand simpleCommand) {
        super(javaPlugin, "advancedseasons.nextseason", simpleCommand, true);
        this.setDescription("Forward a Season");
        this.addFlat("nextseason");
        this.addArgument(String.class, "world", (String[])Bukkit.getWorlds().stream().map(world -> world.getName().toLowerCase()).toArray(String[]::new)).asOptional();
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
        String string = this.parseArgument(stringArray, 1, () -> ((Player)commandSender).getWorld().getName());
        if (string == null || !Core.getWorldHandler().isWorldEnabled(string)) {
            commandSender.sendMessage(Text.modify("&cPlease specify a valid world."));
            return;
        }
        Core.getSeasonHandler().changeSeason(string, true);
        commandSender.sendMessage(Text.modify("&6AdvancedSeasons &aForwarded the season in &6" + string + "&a to &6" + Core.getSeasonHandler().getSeason(string).name().toLowerCase().replace("_", " ") + "&a."));
    }
}

