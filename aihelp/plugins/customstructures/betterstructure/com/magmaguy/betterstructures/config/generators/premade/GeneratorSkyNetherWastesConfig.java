/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;

public class GeneratorSkyNetherWastesConfig
extends GeneratorConfigFields {
    public GeneratorSkyNetherWastesConfig() {
        super("generator_sky_nether_wastes", true, Arrays.asList(GeneratorConfigFields.StructureType.SKY));
        this.setValidBiomesStrings(Arrays.asList("minecraft:nether_wastes", "minecraft:custom"));
        this.setTreasureFilename("treasure_nether.yml");
    }
}

