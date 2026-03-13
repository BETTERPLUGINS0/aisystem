/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;

public class GeneratorSurfaceSnowyConfig
extends GeneratorConfigFields {
    public GeneratorSurfaceSnowyConfig() {
        super("generator_surface_snowy", true, Arrays.asList(GeneratorConfigFields.StructureType.SURFACE));
        this.setValidBiomesStrings(Arrays.asList("minecraft:snowy_taiga", "minecraft:snowy_beach", "minecraft:snowy_plains", "minecraft:snowy_slopes", "minecraft:ice_spikes", "minecraft:custom"));
        this.setTreasureFilename("treasure_overworld_surface.yml");
    }
}

