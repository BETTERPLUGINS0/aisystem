/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import java.util.List;
import org.bukkit.command.CommandSender;

public class ReloadCommand
extends AdvancedCommand {
    public ReloadCommand() {
        super(List.of((Object)"reload"));
        this.setPermission("betterstructures.*");
        this.setUsage("/betterstructures reload");
        this.setDescription("Reloads the plugin.");
    }

    @Override
    public void execute(CommandData commandData) {
        ReloadCommand.reload(commandData.getCommandSender());
    }

    public static void reload(CommandSender commandSender) {
        MetadataHandler.pendingReloadSender = commandSender;
        MetadataHandler.PLUGIN.onDisable();
        MetadataHandler.PLUGIN.onLoad();
        MetadataHandler.PLUGIN.onEnable();
    }
}

