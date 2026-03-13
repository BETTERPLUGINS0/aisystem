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

public class GeneratorUndergroundNetherShallow
extends GeneratorConfigFields {
    public GeneratorUndergroundNetherShallow() {
        super("generator_underground_nether_shallow", true, Arrays.asList(GeneratorConfigFields.StructureType.UNDERGROUND_SHALLOW));
        this.setValidWorldEnvironments(Arrays.asList(World.Environment.NETHER));
        this.setTreasureFilename("treasure_nether.yml");
    }
}

