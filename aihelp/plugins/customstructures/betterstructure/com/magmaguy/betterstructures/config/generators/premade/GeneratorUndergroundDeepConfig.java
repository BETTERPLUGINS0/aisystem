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

public class GeneratorUndergroundDeepConfig
extends GeneratorConfigFields {
    public GeneratorUndergroundDeepConfig() {
        super("generator_underground_deep", true, Arrays.asList(GeneratorConfigFields.StructureType.UNDERGROUND_DEEP));
        this.setValidWorldEnvironments(Arrays.asList(World.Environment.NORMAL, World.Environment.CUSTOM));
        this.setTreasureFilename("treasure_overworld_underground.yml");
    }
}

