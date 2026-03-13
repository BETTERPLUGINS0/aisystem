/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.util.Logger;
import java.util.List;
import org.bukkit.Bukkit;

public class VersionCommand
extends AdvancedCommand {
    public VersionCommand() {
        super(List.of((Object)"version"));
        this.setPermission("betterstructures.*");
        this.setUsage("/betterstructures version");
        this.setDescription("Shows the version of the plugin");
    }

    @Override
    public void execute(CommandData commandData) {
        Logger.sendMessage(commandData.getCommandSender(), "&aVersion " + Bukkit.getPluginManager().getPlugin(MetadataHandler.PLUGIN.getName()).getDescription().getVersion());
    }
}

