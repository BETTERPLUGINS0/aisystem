/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.generator.ChunkGenerator
 *  org.bukkit.generator.ChunkGenerator$BiomeGrid
 *  org.bukkit.generator.ChunkGenerator$ChunkData
 */
package com.andrei1058.bedwars.arena;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class VoidChunkGenerator
extends ChunkGenerator {
    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid b) {
        return this.createChunkData(world);
    }

    public final Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0.0, 64.0, 0.0);
    }
}

