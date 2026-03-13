/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;

public class GeneratorUndergroundLushConfig
extends GeneratorConfigFields {
    public GeneratorUndergroundLushConfig() {
        super("generator_underground_lush", true, Arrays.asList(GeneratorConfigFields.StructureType.UNDERGROUND_SHALLOW, GeneratorConfigFields.StructureType.UNDERGROUND_DEEP));
        this.setValidBiomesStrings(Arrays.asList("minecraft:lush_caves", "minecraft:custom"));
        this.setTreasureFilename("treasure_overworld_underground.yml");
    }
}

