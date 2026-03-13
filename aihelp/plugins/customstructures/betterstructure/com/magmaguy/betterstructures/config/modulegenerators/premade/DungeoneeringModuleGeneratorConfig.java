/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World$Environment
 */
package com.magmaguy.betterstructures.config.modulegenerators.premade;

import com.magmaguy.betterstructures.config.modulegenerators.ModuleGeneratorsConfigFields;
import java.util.List;
import org.bukkit.World;

public class DungeoneeringModuleGeneratorConfig
extends ModuleGeneratorsConfigFields {
    public DungeoneeringModuleGeneratorConfig() {
        super("dungeoneering_module_generator");
        this.radius = 5;
        this.edges = true;
        this.minChunkY = -2;
        this.maxChunkY = 2;
        this.moduleSizeXZ = 32;
        this.moduleSizeY = 16;
        this.debug = false;
        this.startModules = List.of((Object)"Betterstructures_ModularDungeon_Free_Center_1.schem", (Object)"Betterstructures_ModularDungeon_Free_Center_2.schem", (Object)"Betterstructures_ModularDungeon_Premium_Center_1.schem", (Object)"Betterstructures_ModularDungeon_Premium_Center_2.schem", (Object)"Betterstructures_ModularDungeon_Premium_Center_3.schem");
        this.isWorldGeneration = false;
        this.treasureFile = "treasure_overworld_underground.yml";
        this.centerModuleAltitude = -15;
        this.setValidWorldEnvironments(List.of((Object)World.Environment.NORMAL, (Object)World.Environment.CUSTOM));
    }
}

