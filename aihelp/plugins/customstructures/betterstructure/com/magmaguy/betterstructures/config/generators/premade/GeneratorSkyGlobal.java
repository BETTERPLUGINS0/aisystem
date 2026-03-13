/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;

public class GeneratorSkyGlobal
extends GeneratorConfigFields {
    public GeneratorSkyGlobal() {
        super("generator_sky_global", true, Arrays.asList(GeneratorConfigFields.StructureType.SKY));
        this.setTreasureFilename("treasure_overworld_surface.yml");
    }
}

