/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.betterstructures.buildingfitter.FitAnything;
import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import com.magmaguy.betterstructures.schematics.SchematicContainer;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import com.magmaguy.magmacore.command.arguments.ListStringCommandArgument;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class PlaceCommand
extends AdvancedCommand {
    public PlaceCommand() {
        super(List.of((Object)"place"));
        ArrayList<String> loadedSchematics = new ArrayList<String>();
        SchematicContainer.getSchematics().values().forEach(schematicContainer -> loadedSchematics.add(schematicContainer.getClipboardFilename()));
        this.addArgument("schematic", new ListStringCommandArgument(loadedSchematics, "<schematic>"));
        this.addArgument("type", new ListStringCommandArgument(List.of((Object)GeneratorConfigFields.StructureType.SURFACE.toString(), (Object)GeneratorConfigFields.StructureType.UNDERGROUND_SHALLOW.toString(), (Object)GeneratorConfigFields.StructureType.UNDERGROUND_DEEP.toString(), (Object)GeneratorConfigFields.StructureType.SKY.toString(), (Object)GeneratorConfigFields.StructureType.LIQUID_SURFACE.toString()), "<type>"));
        this.setPermission("betterstructures.*");
        this.setDescription("Allows players to place structures.");
        this.setSenderType(SenderType.PLAYER);
        this.setUsage("/betterstructures place <schematic> <SURFACE/SKY/LIQUID_SURFACE/UNDERGROUND_DEEP/UNDERGROUND_SHALLOW>");
    }

    @Override
    public void execute(CommandData commandData) {
        this.placeSchematic(commandData.getStringArgument("schematic"), commandData.getStringArgument("type"), commandData.getPlayerSender());
    }

    private void placeSchematic(String schematicFile, String schematicType, Player player) {
        try {
            GeneratorConfigFields.StructureType structureType;
            SchematicContainer commandSchematicContainer = null;
            for (SchematicContainer schematicContainer : SchematicContainer.getSchematics().values()) {
                if (!schematicContainer.getClipboardFilename().equals(schematicFile)) continue;
                commandSchematicContainer = schematicContainer;
                break;
            }
            if (commandSchematicContainer == null) {
                player.sendMessage("[BetterStructures] Invalid schematic!");
                return;
            }
            try {
                structureType = GeneratorConfigFields.StructureType.valueOf(schematicType);
            } catch (Exception exception) {
                player.sendMessage("[BetterStructures] Failed to get valid schematic type!");
                return;
            }
            FitAnything.commandBasedCreation(player.getLocation().getChunk(), structureType, commandSchematicContainer);
            player.sendMessage("[BetterStructures] Attempted to place " + schematicFile + " !");
        } catch (Exception ex) {
            player.sendMessage("[BetterStructures] Invalid schematic!");
        }
    }
}

