/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.commands.sub;

import java.util.Locale;
import java.util.Objects;
import net.advancedplugins.as.impl.utils.commands.SimpleCommand;
import net.advancedplugins.as.impl.utils.text.Text;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.commands.sub.ASSubCommand;
import net.advancedplugins.seasons.handlers.WeatherHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WeatherSubCommand
extends ASSubCommand {
    public WeatherSubCommand(JavaPlugin javaPlugin, SimpleCommand simpleCommand) {
        super(javaPlugin, "advancedseasons.weather", simpleCommand, false);
        this.setDescription("Change the weather");
        this.addFlat("weather");
        this.addArgument(String.class, "weather", "clear", "rain", "thunder");
        this.addArgument(String.class, "world", (String[])Bukkit.getWorlds().stream().map(world -> world.getName()).toArray(String[]::new)).asOptional();
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
        String string = this.parseArgument(stringArray, 2, () -> ((Player)commandSender).getWorld().getName());
        World world = Bukkit.getWorld((String)string);
        if (world == null) {
            commandSender.sendMessage(Text.modify("&6AdvancedSeasons &cInvalid world " + string));
            return;
        }
        String string2 = (String)this.parseArgument(stringArray, 1);
        WeatherHandler weatherHandler = Core.getWeatherHandler();
        switch (Objects.toString(string2).toLowerCase(Locale.ROOT)) {
            case "rain": {
                weatherHandler.ignoreNextWeatherChange(world, true);
                world.setStorm(true);
                break;
            }
            case "storm": {
                weatherHandler.ignoreNextWeatherChange(world, true);
                world.setThundering(true);
                break;
            }
            case "sun": {
                weatherHandler.ignoreNextWeatherChange(world, true);
                world.setStorm(false);
                break;
            }
            default: {
                commandSender.sendMessage(Text.modify("&cInvalid weather operation, use &oday, rain or storm&r&c."));
            }
        }
    }
}

