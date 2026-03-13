/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.betterstructures.util.ChunkPregenerator;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import com.magmaguy.magmacore.command.arguments.IntegerCommandArgument;
import com.magmaguy.magmacore.command.arguments.ListStringCommandArgument;
import com.magmaguy.magmacore.util.Logger;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;

public class PregenerateCommand
extends AdvancedCommand {
    public PregenerateCommand() {
        super(List.of((Object)"pregenerate"));
        this.addArgument("center", new ListStringCommandArgument(List.of((Object)"HERE", (Object)"WORLD_CENTER", (Object)"WORLD_SPAWN"), "Center of the generation"));
        this.addArgument("shape", new ListStringCommandArgument(List.of((Object)"SQUARE", (Object)"CIRCLE"), "Shape of the generation"));
        this.addArgument("radius", new IntegerCommandArgument("Radius in blocks to generate"));
        this.addArgument("setWorldBorder", new ListStringCommandArgument(List.of((Object)"TRUE", (Object)"FALSE"), "Set a world border at the end?"));
        this.setUsage("/betterstructures pregenerate <centerType> <shape> <radiusInBlocks> <applyWorldBorder>");
        this.setPermission("betterstructures.*");
        this.setDescription("Pregenerates chunks from a center point outward in either a square or circle pattern up to the specified radius in blocks.");
        this.setSenderType(SenderType.PLAYER);
    }

    @Override
    public void execute(CommandData commandData) {
        Location center;
        String centerArg = commandData.getStringArgument("center");
        String shape = commandData.getStringArgument("shape");
        int radius = commandData.getIntegerArgument("radius");
        String setWorldBorderArg = commandData.getStringArgument("setWorldBorder");
        if (radius < 0) {
            Logger.sendMessage(commandData.getCommandSender(), "&cRadius must be 0 or greater.");
            return;
        }
        World world = commandData.getPlayerSender().getWorld();
        switch (centerArg.toUpperCase()) {
            case "HERE": {
                center = commandData.getPlayerSender().getLocation();
                break;
            }
            case "WORLD_CENTER": {
                center = new Location(world, 0.0, (double)world.getHighestBlockYAt(0, 0), 0.0);
                break;
            }
            case "WORLD_SPAWN": {
                center = world.getSpawnLocation();
                break;
            }
            default: {
                Logger.sendMessage(commandData.getCommandSender(), "&cInvalid center argument. Use HERE, WORLD_CENTER, or WORLD_SPAWN.");
                return;
            }
        }
        boolean setWorldBorder = "TRUE".equalsIgnoreCase(setWorldBorderArg);
        if (!"SQUARE".equalsIgnoreCase(shape) && !"CIRCLE".equalsIgnoreCase(shape)) {
            Logger.sendMessage(commandData.getCommandSender(), "&cInvalid shape. Use SQUARE or CIRCLE.");
            return;
        }
        int radiusInBlocks = radius;
        int radiusInChunks = (int)Math.ceil((double)radiusInBlocks / 16.0);
        Logger.sendMessage(commandData.getCommandSender(), "&2Starting chunk pregeneration with shape: " + shape + ", center: " + centerArg + ", radius: " + radiusInBlocks + " blocks (" + radiusInChunks + " chunks)");
        if (setWorldBorder) {
            Logger.sendMessage(commandData.getCommandSender(), "&2World border will be set to match the generated area.");
        }
        Logger.sendMessage(commandData.getCommandSender(), "&7Progress will be reported in the console every 30 seconds.");
        Logger.sendMessage(commandData.getCommandSender(), "&7Use &2/betterstructures cancelPregenerate &7to cancel if needed.");
        ChunkPregenerator pregenerator = new ChunkPregenerator(world, center, shape, radiusInBlocks, radiusInChunks, setWorldBorder);
        pregenerator.start();
    }
}

