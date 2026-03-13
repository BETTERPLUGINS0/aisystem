/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import com.magmaguy.magmacore.command.arguments.IntegerCommandArgument;
import com.magmaguy.magmacore.command.arguments.WorldCommandArgument;
import com.magmaguy.magmacore.util.Logger;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class TeleportCommand
extends AdvancedCommand {
    public TeleportCommand() {
        super(List.of((Object)"teleport", (Object)"tp"));
        this.addArgument("world", new WorldCommandArgument("<world>"));
        this.addArgument("x", new IntegerCommandArgument("<x>"));
        this.addArgument("y", new IntegerCommandArgument("<y>"));
        this.addArgument("z", new IntegerCommandArgument("<z>"));
        this.setUsage("/teleport <worldname> <x> <y> <z>");
        this.setPermission("betterstructures.*");
        this.setDescription("Teleports a player to specific coordinates.");
        this.setSenderType(SenderType.PLAYER);
    }

    @Override
    public void execute(CommandData commandData) {
        try {
            World world = Bukkit.getWorld((String)commandData.getStringArgument("world"));
            double x = Double.parseDouble(commandData.getStringArgument("x"));
            double y = Double.parseDouble(commandData.getStringArgument("y"));
            double z = Double.parseDouble(commandData.getStringArgument("z"));
            commandData.getPlayerSender().teleport(new Location(world, x, y, z));
        } catch (Exception ex) {
            Logger.sendMessage(commandData.getCommandSender(), "Failed to teleport to location because the location wasn't valid!");
        }
    }
}

