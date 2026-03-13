/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World$Environment
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;
import org.bukkit.World;

public class GeneratorUndergroundShallowConfig
extends GeneratorConfigFields {
    public GeneratorUndergroundShallowConfig() {
        super("generator_underground_shallow", true, Arrays.asList(GeneratorConfigFields.StructureType.UNDERGROUND_SHALLOW));
        this.setValidWorldEnvironments(Arrays.asList(World.Environment.NORMAL, World.Environment.CUSTOM));
        this.setTreasureFilename("treasure_overworld_underground.yml");
    }
}

