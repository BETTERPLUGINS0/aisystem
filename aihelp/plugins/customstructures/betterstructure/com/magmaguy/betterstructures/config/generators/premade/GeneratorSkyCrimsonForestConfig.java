/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;

public class GeneratorSkyCrimsonForestConfig
extends GeneratorConfigFields {
    public GeneratorSkyCrimsonForestConfig() {
        super("generator_sky_crimson_forest", true, Arrays.asList(GeneratorConfigFields.StructureType.SKY));
        this.setValidBiomesStrings(Arrays.asList("minecraft:crimson_forest", "minecraft:custom"));
        this.setTreasureFilename("treasure_nether.yml");
    }
}

