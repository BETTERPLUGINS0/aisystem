/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FileUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.Difficulty
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.WorldCreator
 *  org.bukkit.generator.ChunkGenerator
 *  org.bukkit.generator.ChunkGenerator$ChunkData
 *  org.bukkit.generator.WorldInfo
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.magmaguy.magmacore.util;

import com.magmaguy.magmacore.MagmaCore;
import com.magmaguy.magmacore.util.Logger;
import java.io.File;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TemporaryWorldManager {
    public static World loadVoidTemporaryWorld(String worldName, World.Environment environment) {
        if (Bukkit.getWorld((String)worldName) != null) {
            return Bukkit.getWorld((String)worldName);
        }
        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        if (!worldFolder.exists()) {
            Logger.warn("File " + worldFolder.getAbsolutePath() + " does not exist!");
            return null;
        }
        Logger.info("Loading world " + worldName + " !");
        try {
            Logger.info("Creating world " + worldName + " !");
            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator.environment(environment);
            worldCreator.generator((ChunkGenerator)new VoidGenerator());
            World world = Bukkit.createWorld((WorldCreator)worldCreator);
            if (world != null) {
                world.setKeepSpawnInMemory(false);
            }
            world.setDifficulty(Difficulty.HARD);
            Logger.info("Successfully loaded world " + worldName + " !");
            return world;
        } catch (Exception exception) {
            Logger.warn("Failed to load world " + worldName + " !");
            exception.printStackTrace();
            return null;
        }
    }

    public static World createVoidTemporaryWorld(String worldName, World.Environment environment) {
        int identifier = 1;
        while (Bukkit.getWorld((String)((String)worldName + "_" + identifier)) != null) {
            ++identifier;
        }
        worldName = (String)worldName + "_" + identifier;
        try {
            Logger.info("Creating world " + (String)worldName + " !");
            WorldCreator worldCreator = new WorldCreator((String)worldName);
            worldCreator.environment(environment);
            worldCreator.generator((ChunkGenerator)new VoidGenerator());
            World world = Bukkit.createWorld((WorldCreator)worldCreator);
            world.setKeepSpawnInMemory(false);
            world.setDifficulty(Difficulty.HARD);
            world.setAutoSave(false);
            Logger.info("Successfully loaded world " + (String)worldName + " !");
            return world;
        } catch (Exception exception) {
            Logger.warn("Failed to load world " + (String)worldName + " !");
            exception.printStackTrace();
            return null;
        }
    }

    public static void asyncPermanentlyDeleteWorld(final World world) {
        world.setAutoSave(false);
        if (!Bukkit.unloadWorld((World)world, (boolean)false)) {
            Logger.warn("Failed to unload world " + world.getName() + " ! This is bad, report this to the developer!");
        }
        new BukkitRunnable(){

            public void run() {
                TemporaryWorldManager.deleteWorldDirectory(world.getName());
            }
        }.runTaskAsynchronously((Plugin)MagmaCore.getInstance().getRequestingPlugin());
    }

    public static void permanentlyDeleteWorld(World world) {
        if (MagmaCore.getInstance().getRequestingPlugin().isEnabled()) {
            TemporaryWorldManager.asyncPermanentlyDeleteWorld(world);
        } else {
            TemporaryWorldManager.syncPermanentlyDeleteWorld(world);
        }
    }

    public static void syncPermanentlyDeleteWorld(World world) {
        world.setAutoSave(false);
        if (!Bukkit.unloadWorld((World)world, (boolean)false)) {
            Logger.warn("Failed to unload world " + world.getName() + " ! This is bad, report this to the developer!");
        }
        TemporaryWorldManager.deleteWorldDirectory(world.getName());
    }

    private static void deleteWorldDirectory(String worldName) {
        try {
            FileUtils.deleteDirectory((File)new File(Bukkit.getWorldContainer(), worldName));
            Logger.info("Successfully deleted temporary world " + worldName);
        } catch (Exception e) {
            Logger.warn("Failed to delete " + worldName + " ! This is bad, report this to the developer!");
        }
    }

    private static class VoidGenerator
    extends ChunkGenerator {
        private VoidGenerator() {
        }

        public void generateSurface(WorldInfo info, Random random, int x, int z, ChunkGenerator.ChunkData data) {
        }

        public boolean shouldGenerateNoise() {
            return false;
        }

        public boolean shouldGenerateBedrock() {
            return false;
        }

        public boolean shouldGenerateCaves() {
            return false;
        }
    }
}

