/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World$Environment
 */
package com.magmaguy.betterstructures.config.generators.premade;

import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import java.util.Arrays;
import org.bukkit.World;

public class GeneratorSurfaceEndConfig
extends GeneratorConfigFields {
    public GeneratorSurfaceEndConfig() {
        super("generator_surface_end", true, Arrays.asList(GeneratorConfigFields.StructureType.SURFACE));
        this.setValidWorldEnvironments(Arrays.asList(World.Environment.THE_END));
        this.setTreasureFilename("treasure_end.yml");
    }
}

