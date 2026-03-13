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

import java.util.Locale;
import net.advancedplugins.as.impl.utils.commands.SimpleCommand;
import net.advancedplugins.as.impl.utils.text.Text;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.commands.sub.ASSubCommand;
import net.advancedplugins.seasons.enums.Season;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetSeasonSub
extends ASSubCommand {
    public SetSeasonSub(JavaPlugin javaPlugin, SimpleCommand simpleCommand) {
        super(javaPlugin, "advancedseasons.setseason", simpleCommand, true);
        this.setDescription("Set Season");
        this.addFlat("setseason");
        this.addArgument(String.class, "season", "spring", "summer", "autumn", "winter");
        this.addArgument(String.class, "world", (String[])Bukkit.getWorlds().stream().map(world -> world.getName().toLowerCase()).toArray(String[]::new)).asOptional();
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
        Season season = Season.fromName(this.parseArgument(stringArray, 1).toString().toUpperCase(Locale.ROOT));
        if (season == null) {
            commandSender.sendMessage(Text.modify("&6AdvancedSeasons &cInvalid season " + stringArray[1]));
            return;
        }
        String string = this.parseArgument(stringArray, 2, () -> ((Player)commandSender).getWorld().getName());
        if (string == null || !Core.getWorldHandler().isWorldEnabled(string)) {
            commandSender.sendMessage(Text.modify("&cPlease specify a valid world."));
            return;
        }
        Core.getSeasonHandler().setSeason(season, true, string);
        Core.getBiomesHandler().getRenderHandler().refreshVisualBiomes(true);
        commandSender.sendMessage(Text.modify("&6AdvancedSeasons &aSet season to " + season.name() + " in &6" + string + "&a."));
    }
}

