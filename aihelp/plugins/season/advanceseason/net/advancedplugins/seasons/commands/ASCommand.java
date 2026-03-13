/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.commands;

import net.advancedplugins.as.impl.utils.commands.SimpleCommand;
import net.advancedplugins.localization.LocaleHandler;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.commands.sub.AddWorldSub;
import net.advancedplugins.seasons.commands.sub.BlockInfoSub;
import net.advancedplugins.seasons.commands.sub.InfoSub;
import net.advancedplugins.seasons.commands.sub.NextSeasonSub;
import net.advancedplugins.seasons.commands.sub.ReloadSub;
import net.advancedplugins.seasons.commands.sub.RemoveWorldSub;
import net.advancedplugins.seasons.commands.sub.SetSeasonSub;
import net.advancedplugins.seasons.commands.sub.SetTemperatureSub;
import net.advancedplugins.seasons.commands.sub.WeatherSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ASCommand
extends SimpleCommand<CommandSender> {
    private final LocaleHandler locale = Core.getLocaleHandler();

    public ASCommand(JavaPlugin javaPlugin) {
        super(javaPlugin, "advancedseasons", "advancedseasons.use", true);
        this.setSubCommands(new AddWorldSub(javaPlugin, this), new RemoveWorldSub(javaPlugin, this), new NextSeasonSub(javaPlugin, this), new SetSeasonSub(javaPlugin, this), new ReloadSub(javaPlugin, this), new InfoSub(javaPlugin, this), new SetTemperatureSub(javaPlugin, this), new BlockInfoSub(javaPlugin, this), new WeatherSubCommand(javaPlugin, this));
        this.addShowcaseCommand("calendar", "Open the calendar menu");
        this.addShowcaseCommand("seasonshop", "Open the season shop menu");
    }

    public LocaleHandler getLocale() {
        return this.locale;
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
        this.sendHelpPage(commandSender, "&e", stringArray);
    }
}

