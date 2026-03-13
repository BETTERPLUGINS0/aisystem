/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;

public class GeneratorSkySoulSandConfig
extends GeneratorConfigFields {
    public GeneratorSkySoulSandConfig() {
        super("generator_sky_soul_sand", true, Arrays.asList(GeneratorConfigFields.StructureType.SKY));
        this.setValidBiomesStrings(Arrays.asList("minecraft:soul_sand_valley", "minecraft:custom"));
        this.setTreasureFilename("treasure_nether.yml");
    }
}

