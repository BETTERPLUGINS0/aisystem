/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.magmaguy.elitemobs.config.custombosses.CustomBossesConfig
 *  com.magmaguy.elitemobs.config.custombosses.CustomBossesConfigFields
 *  com.magmaguy.elitemobs.mobconstructor.custombosses.CustomBossEntity
 *  com.magmaguy.elitemobs.mobconstructor.custombosses.InstancedBossEntity
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.event.Event
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.magmaguy.betterstructures.modules;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.api.WorldGenerationFinishEvent;
import com.magmaguy.betterstructures.config.spawnpools.SpawnPoolsConfig;
import com.magmaguy.betterstructures.config.spawnpools.SpawnPoolsConfigFields;
import com.magmaguy.betterstructures.modules.ModulePasting;
import com.magmaguy.betterstructures.worldedit.Schematic;
import com.magmaguy.elitemobs.config.custombosses.CustomBossesConfig;
import com.magmaguy.elitemobs.config.custombosses.CustomBossesConfigFields;
import com.magmaguy.elitemobs.mobconstructor.custombosses.CustomBossEntity;
import com.magmaguy.elitemobs.mobconstructor.custombosses.InstancedBossEntity;
import com.magmaguy.magmacore.instance.MatchInstance;
import com.magmaguy.magmacore.util.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Vector2i;

public class ModularWorld {
    private final List<Location> spawnLocations = new ArrayList<Location>();
    private final List<ExitLocation> exitLocations = new ArrayList<ExitLocation>();
    private final List<Location> chestLocations = new ArrayList<Location>();
    private final List<Location> barrelLocations = new ArrayList<Location>();
    private final HashSet<ModulePasting.InterpretedSign> otherLocations = new HashSet();
    private final List<ScheduledInstancedEntity> scheduledInstancedEntities = new ArrayList<ScheduledInstancedEntity>();
    private final File worldFolder;
    private World world = null;

    public ModularWorld(World world, File worldFolder, List<ModulePasting.InterpretedSign> interpretedSigns) {
        this.world = world;
        this.worldFolder = worldFolder;
        for (ModulePasting.InterpretedSign interpretedSign : interpretedSigns) {
            for (String signText : interpretedSign.text()) {
                if (signText.contains("[spawn]")) {
                    this.spawnLocations.add(new Location(world, (double)((int)interpretedSign.location().getX()), (double)((int)interpretedSign.location().getY()), (double)((int)interpretedSign.location().getZ())));
                    continue;
                }
                if (signText.contains("[exit]")) {
                    this.processExitLocations(interpretedSign);
                    continue;
                }
                if (signText.contains("[chest]")) {
                    this.chestLocations.add(new Location(world, (double)((int)interpretedSign.location().getX()), (double)((int)interpretedSign.location().getY()), (double)((int)interpretedSign.location().getZ())));
                    continue;
                }
                if (signText.contains("[barrel]")) {
                    this.barrelLocations.add(new Location(world, (double)((int)interpretedSign.location().getX()), (double)((int)interpretedSign.location().getY()), (double)((int)interpretedSign.location().getZ())));
                    continue;
                }
                this.otherLocations.add(interpretedSign);
            }
        }
    }

    public static String extractPoolText(String input) {
        Pattern pattern = Pattern.compile("\\[pool:\\s*([^\\]]+)\\]");
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group(1) : null;
    }

    private void processExitLocations(ModulePasting.InterpretedSign interpretedSign) {
        Object exitClipboardFilename = "";
        for (int i = 1; i < interpretedSign.text().size(); ++i) {
            exitClipboardFilename = (String)exitClipboardFilename + interpretedSign.text().get(i);
        }
        if (((String)exitClipboardFilename).isEmpty()) {
            Logger.warn("Failed to get exit clipboard filename from sign " + String.valueOf(interpretedSign.location()));
            this.exitLocations.add(new ExitLocation(new Location(this.world, (double)((int)interpretedSign.location().getX()), (double)((int)interpretedSign.location().getY()), (double)((int)interpretedSign.location().getZ())), "genericelevator_up", "genericelevator_down"));
            return;
        }
        this.exitLocations.add(new ExitLocation(new Location(this.world, (double)((int)interpretedSign.location().getX()), (double)((int)interpretedSign.location().getY()), (double)((int)interpretedSign.location().getZ())), interpretedSign.text().get(1), interpretedSign.text().get(2)));
    }

    public List<Block> spawnChests() {
        ArrayList<Block> chests = new ArrayList<Block>();
        for (Location chestLocation : this.chestLocations) {
            chestLocation.getBlock().setType(Material.CHEST);
            chests.add(chestLocation.getBlock());
        }
        return chests;
    }

    public List<Block> spawnBarrels() {
        ArrayList<Block> barrels = new ArrayList<Block>();
        for (Location barrelLocation : this.barrelLocations) {
            barrelLocation.getBlock().setType(Material.BARREL);
            barrels.add(barrelLocation.getBlock());
        }
        return barrels;
    }

    public List<Location> spawnInaccessibleExitLocations() {
        ArrayList<Location> randomizedLocations = new ArrayList<Location>();
        for (ExitLocation exitLocation : this.exitLocations) {
            File exitLocationsFile = new File(MetadataHandler.PLUGIN.getDataFolder().getAbsolutePath() + File.separatorChar + "components" + File.separatorChar + exitLocation.clipboardFilenameUp + ".schem");
            if (!exitLocationsFile.exists()) {
                Logger.warn("Failed to find elevator file");
                continue;
            }
            randomizedLocations.add(exitLocation.location);
            Schematic.paste(Schematic.load(exitLocationsFile), exitLocation.location);
        }
        return randomizedLocations;
    }

    public List<Location> spawnAccessibleExitLocations() {
        ArrayList<Location> randomizedLocations = new ArrayList<Location>();
        for (ExitLocation exitLocation : this.exitLocations) {
            File exitLocationsFile = new File(MetadataHandler.PLUGIN.getDataFolder().getAbsolutePath() + File.separatorChar + "components" + File.separatorChar + exitLocation.clipboardFilenameDown + ".schem");
            if (!exitLocationsFile.exists()) {
                Logger.warn("Failed to find elevator file");
                continue;
            }
            randomizedLocations.add(exitLocation.location);
            Schematic.paste(Schematic.load(exitLocationsFile), exitLocation.location);
        }
        return randomizedLocations;
    }

    public void spawnOtherEntities() {
        new BukkitRunnable(){

            public void run() {
                for (ModulePasting.InterpretedSign otherLocation : ModularWorld.this.otherLocations) {
                    for (String string : otherLocation.text()) {
                        if (!string.contains("pool")) continue;
                        String parsedString = ModularWorld.extractPoolText(string) + ".yml";
                        SpawnPoolsConfigFields spawnPoolsConfigFields = SpawnPoolsConfig.getConfigFields(parsedString);
                        if (spawnPoolsConfigFields == null) {
                            Logger.warn("Could not find spawn pool " + parsedString);
                            continue;
                        }
                        CustomBossesConfigFields customBossesConfigFields = CustomBossesConfig.getCustomBoss((String)spawnPoolsConfigFields.getPoolStrings().get(ThreadLocalRandom.current().nextInt(0, spawnPoolsConfigFields.getPoolStrings().size())));
                        if (!customBossesConfigFields.isInstanced()) {
                            CustomBossEntity customBossEntity = new CustomBossEntity(customBossesConfigFields);
                            customBossEntity.spawn(otherLocation.location(), true);
                            continue;
                        }
                        ModularWorld.this.scheduledInstancedEntities.add(new ScheduledInstancedEntity(otherLocation.location(), customBossesConfigFields, parsedString, spawnPoolsConfigFields.getMinLevel(), spawnPoolsConfigFields.getMaxLevel()));
                    }
                }
                ModularWorld.this.otherLocations.clear();
                ModularWorld.this.generationFinished();
            }
        }.runTask(MetadataHandler.PLUGIN);
    }

    public List<InstancedBossEntity> spawnInstancedEntities(MatchInstance matchInstance) {
        ArrayList<InstancedBossEntity> instancedBossEntities = new ArrayList<InstancedBossEntity>();
        for (ScheduledInstancedEntity scheduledInstancedEntity : this.scheduledInstancedEntities) {
            int totalRadius = 320;
            Vector2i center = new Vector2i(64, 64);
            Vector2i entityLocation = new Vector2i(scheduledInstancedEntity.location.getBlockX(), scheduledInstancedEntity.location.getBlockZ());
            double distance = center.distance(entityLocation);
            double percentageDistance = distance / (double)totalRadius;
            int level = (int)Math.round((1.0 - percentageDistance) * (double)scheduledInstancedEntity.maxLevel + percentageDistance * (double)scheduledInstancedEntity.minLevel);
            InstancedBossEntity instancedBossEntity = new InstancedBossEntity(scheduledInstancedEntity.configFields, scheduledInstancedEntity.location, matchInstance, level);
            instancedBossEntity.spawn(true);
            instancedBossEntity.addCustomData(new NamespacedKey("betterstructures", "spawnpool"), (Object)scheduledInstancedEntity.originalSpawnPool);
            instancedBossEntities.add(instancedBossEntity);
        }
        return instancedBossEntities;
    }

    public void generationFinished() {
        Bukkit.getServer().getPluginManager().callEvent((Event)new WorldGenerationFinishEvent(this));
    }

    public List<Location> getSpawnLocations() {
        return this.spawnLocations;
    }

    public List<ExitLocation> getExitLocations() {
        return this.exitLocations;
    }

    public List<Location> getChestLocations() {
        return this.chestLocations;
    }

    public List<Location> getBarrelLocations() {
        return this.barrelLocations;
    }

    public File getWorldFolder() {
        return this.worldFolder;
    }

    public World getWorld() {
        return this.world;
    }

    private record ExitLocation(Location location, String clipboardFilenameUp, String clipboardFilenameDown) {
    }

    private record ScheduledInstancedEntity(Location location, CustomBossesConfigFields configFields, String originalSpawnPool, int minLevel, int maxLevel) {
    }
}

