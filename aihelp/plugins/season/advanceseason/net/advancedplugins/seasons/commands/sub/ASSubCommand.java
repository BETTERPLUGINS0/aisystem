/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.commands.sub;

import net.advancedplugins.as.impl.utils.commands.SimpleCommand;
import net.advancedplugins.as.impl.utils.commands.SubCommand;
import net.advancedplugins.localization.LocaleHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ASSubCommand
extends SubCommand<CommandSender> {
    private SimpleCommand command;

    public ASSubCommand(JavaPlugin javaPlugin, String string, SimpleCommand simpleCommand) {
        super(javaPlugin, string);
        super.noPermissionLang(commandSender -> simpleCommand.getNoPermissionLang((CommandSender)commandSender));
        super.setNotOnlineLang(commandSender -> simpleCommand.getNotOnlineLang((CommandSender)commandSender));
        this.command = simpleCommand;
    }

    public ASSubCommand(JavaPlugin javaPlugin, String string, SimpleCommand simpleCommand, boolean bl) {
        super(javaPlugin, string, bl);
        super.noPermissionLang(commandSender -> simpleCommand.getNoPermissionLang((CommandSender)commandSender));
        super.setNotOnlineLang(commandSender -> simpleCommand.getNotOnlineLang((CommandSender)commandSender));
        this.command = simpleCommand;
    }

    public LocaleHandler lang() {
        return null;
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
    }
}

