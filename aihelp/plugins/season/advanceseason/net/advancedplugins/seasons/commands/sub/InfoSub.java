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

public class InfoSub
extends ASSubCommand {
    public InfoSub(JavaPlugin javaPlugin, SimpleCommand simpleCommand) {
        super(javaPlugin, "advancedseasons.info", simpleCommand, false);
        this.setDescription("Information of current season");
        this.addFlat("info");
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
        int n = Core.getTemperatureHandler().getPlayerTemperature((Player)commandSender);
        Core.getBiomesHandler().getAdvancedBiomeAt(((Player)commandSender).getLocation()).ifPresentOrElse(advancedBiomeBase -> {
            for (String string : Core.getLocaleHandler().getStringList("messages.biomeInfo")) {
                commandSender.sendMessage(Text.modify(Core.getTemperatureHandler().parseTemperatureDisplay(n, string, ((Player)commandSender).getWorld().getName(), (Player)commandSender)).replace("%season%", Core.getSeasonHandler().getSeason(((Player)commandSender).getWorld().getName()).getType().name()).replace("%biome%", advancedBiomeBase.getName()));
            }
        }, () -> commandSender.sendMessage(Text.modify("&cError: &7Could not find biome / seasons are not enabled in this world!")));
    }
}

