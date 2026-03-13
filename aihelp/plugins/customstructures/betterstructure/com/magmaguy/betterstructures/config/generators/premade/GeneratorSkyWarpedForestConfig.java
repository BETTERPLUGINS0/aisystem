/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;

public class GeneratorSkyWarpedForestConfig
extends GeneratorConfigFields {
    public GeneratorSkyWarpedForestConfig() {
        super("generator_sky_warped_forest", true, Arrays.asList(GeneratorConfigFields.StructureType.SKY));
        this.setValidBiomesStrings(Arrays.asList("minecraft:warped_forest", "minecraft:custom"));
        this.setTreasureFilename("treasure_nether.yml");
    }
}

