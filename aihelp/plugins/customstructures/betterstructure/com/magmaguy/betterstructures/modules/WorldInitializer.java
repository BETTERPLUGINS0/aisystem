/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.WorldCreator
 *  org.bukkit.entity.Player
 *  org.bukkit.generator.ChunkGenerator
 */
package com.magmaguy.betterstructures.modules;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

public class WorldInitializer {
    public static World generateWorld(String worldName, Player player) {
        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.keepSpawnInMemory(false);
        worldCreator.generator((ChunkGenerator)new VoidGenerator());
        World world = worldCreator.createWorld();
        world.setAutoSave(false);
        player.setGameMode(GameMode.SPECTATOR);
        return world;
    }

    private static class VoidGenerator
    extends ChunkGenerator {
        private VoidGenerator() {
        }
    }
}

