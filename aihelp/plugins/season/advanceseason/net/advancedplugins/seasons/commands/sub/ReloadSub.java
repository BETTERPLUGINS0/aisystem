/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.commands.sub;

import net.advancedplugins.as.impl.utils.commands.SimpleCommand;
import net.advancedplugins.as.impl.utils.text.Text;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.commands.sub.ASSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadSub
extends ASSubCommand {
    public ReloadSub(JavaPlugin javaPlugin, SimpleCommand simpleCommand) {
        super(javaPlugin, "advancedseasons.reload", simpleCommand);
        this.setDescription("Reload configuration files");
        this.addFlat("reload");
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
        long l = System.currentTimeMillis();
        Core.getInstance().reloadConfig();
        ((Core)Core.getInstance()).init();
        long l2 = System.currentTimeMillis();
        commandSender.sendMessage(Text.modify("&6AdvancedSeasons &aReloaded configuration files in &e" + (l2 - l) / 1000L + "s"));
        commandSender.sendMessage(Text.modify("&6AdvancedSeasons &fCustom seasons colors never reload, you must restart the server to apply changes."));
    }
}

