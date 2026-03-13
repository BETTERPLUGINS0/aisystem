/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;

public class GeneratorSurfaceDesertConfig
extends GeneratorConfigFields {
    public GeneratorSurfaceDesertConfig() {
        super("generator_surface_desert", true, Arrays.asList(GeneratorConfigFields.StructureType.SURFACE));
        this.setValidBiomesStrings(Arrays.asList("minecraft:desert", "minecraft:custom"));
        this.setTreasureFilename("treasure_overworld_surface.yml");
    }
}

