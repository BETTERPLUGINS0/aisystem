/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;

public class GeneratorSkyDesertConfig
extends GeneratorConfigFields {
    public GeneratorSkyDesertConfig() {
        super("generator_sky_desert", true, Arrays.asList(GeneratorConfigFields.StructureType.SKY));
        this.setValidBiomesStrings(Arrays.asList("minecraft:desert", "minecraft:custom"));
        this.setTreasureFilename("treasure_overworld_surface.yml");
    }
}

