/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;

public class GeneratorSurfaceBarrenConfig
extends GeneratorConfigFields {
    public GeneratorSurfaceBarrenConfig() {
        super("generator_surface_barren", true, Arrays.asList(GeneratorConfigFields.StructureType.SURFACE));
        this.setValidBiomesStrings(Arrays.asList("minecraft:badlands", "minecraft:savanna", "minecraft:savanna_plateau", "minecraft:windswept_savanna", "minecraft:mushroom_fields", "minecraft:custom"));
        this.setTreasureFilename("treasure_overworld_surface.yml");
    }
}

