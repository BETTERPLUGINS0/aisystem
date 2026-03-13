/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.betterstructures.util.ChunkPregenerator;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import com.magmaguy.magmacore.util.Logger;
import java.util.List;
import org.bukkit.World;

public class CancelPregenerateCommand
extends AdvancedCommand {
    public CancelPregenerateCommand() {
        super(List.of((Object)"cancelPregenerate", (Object)"cancelpregenerate"));
        this.setUsage("/betterstructures cancelPregenerate");
        this.setPermission("betterstructures.*");
        this.setDescription("Cancels active chunk pregeneration in your current world.");
        this.setSenderType(SenderType.PLAYER);
    }

    @Override
    public void execute(CommandData commandData) {
        World world = commandData.getPlayerSender().getWorld();
        List worldPregenerators = ChunkPregenerator.activePregenerators.stream().filter(p -> p.getWorld().equals((Object)world)).toList();
        if (worldPregenerators.isEmpty()) {
            Logger.sendMessage(commandData.getCommandSender(), "&cNo active pregeneration found in world: " + world.getName());
            return;
        }
        int cancelled = 0;
        for (ChunkPregenerator pregenerator : worldPregenerators) {
            pregenerator.cancel();
            ++cancelled;
        }
        Logger.sendMessage(commandData.getCommandSender(), "&2Cancelled " + cancelled + " pregeneration process(es) in world: " + world.getName());
    }
}

