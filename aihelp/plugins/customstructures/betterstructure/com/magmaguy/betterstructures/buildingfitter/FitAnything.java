/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.extent.clipboard.Clipboard
 *  net.md_5.bungee.api.chat.BaseComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Container
 *  org.bukkit.entity.EnderCrystal
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.buildingfitter;

import com.magmaguy.betterstructures.api.BuildPlaceEvent;
import com.magmaguy.betterstructures.api.ChestFillEvent;
import com.magmaguy.betterstructures.buildingfitter.FitAirBuilding;
import com.magmaguy.betterstructures.buildingfitter.FitLiquidBuilding;
import com.magmaguy.betterstructures.buildingfitter.FitSurfaceBuilding;
import com.magmaguy.betterstructures.buildingfitter.FitUndergroundShallowBuilding;
import com.magmaguy.betterstructures.buildingfitter.util.FitUndergroundDeepBuilding;
import com.magmaguy.betterstructures.buildingfitter.util.LocationProjector;
import com.magmaguy.betterstructures.buildingfitter.util.SchematicPicker;
import com.magmaguy.betterstructures.config.DefaultConfig;
import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import com.magmaguy.betterstructures.schematics.SchematicContainer;
import com.magmaguy.betterstructures.thirdparty.EliteMobs;
import com.magmaguy.betterstructures.thirdparty.MythicMobs;
import com.magmaguy.betterstructures.thirdparty.WorldGuard;
import com.magmaguy.betterstructures.util.SurfaceMaterials;
import com.magmaguy.betterstructures.util.WorldEditUtils;
import com.magmaguy.betterstructures.worldedit.Schematic;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.SpigotMessage;
import com.magmaguy.magmacore.util.VersionChecker;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FitAnything {
    public static boolean worldGuardWarn = false;
    protected final int searchRadius = 1;
    protected final int scanStep = 3;
    private final HashMap<Material, Integer> undergroundPedestalMaterials = new HashMap();
    private final HashMap<Material, Integer> surfacePedestalMaterials = new HashMap();
    protected SchematicContainer schematicContainer;
    protected double startingScore = 100.0;
    protected Clipboard schematicClipboard = null;
    protected Vector schematicOffset;
    protected int verticalOffset = 0;
    protected double highestScore = 10.0;
    protected Location location = null;
    protected GeneratorConfigFields.StructureType structureType;
    private Material pedestalMaterial = null;

    public FitAnything(SchematicContainer schematicContainer) {
        this.schematicContainer = schematicContainer;
        this.verticalOffset = schematicContainer.getClipboard().getMinimumPoint().y() - schematicContainer.getClipboard().getOrigin().y();
    }

    public FitAnything() {
    }

    public static void commandBasedCreation(Chunk chunk, GeneratorConfigFields.StructureType structureType, SchematicContainer container) {
        switch (structureType) {
            case SKY: {
                new FitAirBuilding(chunk, container);
                break;
            }
            case SURFACE: {
                new FitSurfaceBuilding(chunk, container);
                break;
            }
            case LIQUID_SURFACE: {
                new FitLiquidBuilding(chunk, container);
                break;
            }
            case UNDERGROUND_DEEP: {
                FitUndergroundDeepBuilding.fit(chunk, container);
                break;
            }
            case UNDERGROUND_SHALLOW: {
                FitUndergroundShallowBuilding.fit(chunk, container);
                break;
            }
        }
    }

    protected void randomizeSchematicContainer(Location location, GeneratorConfigFields.StructureType structureType) {
        if (this.schematicClipboard != null) {
            return;
        }
        this.schematicContainer = SchematicPicker.pick(location, structureType);
        if (this.schematicContainer != null) {
            this.schematicClipboard = this.schematicContainer.getClipboard();
            this.verticalOffset = this.schematicContainer.getClipboard().getMinimumPoint().y() - this.schematicContainer.getClipboard().getOrigin().y();
        }
    }

    protected void paste(Location location) {
        BuildPlaceEvent buildPlaceEvent = new BuildPlaceEvent(this);
        Bukkit.getServer().getPluginManager().callEvent((Event)buildPlaceEvent);
        if (buildPlaceEvent.isCancelled()) {
            return;
        }
        FitAnything fitAnything = this;
        this.assignPedestalMaterial(location);
        if (this.pedestalMaterial == null) {
            switch (location.getWorld().getEnvironment()) {
                case NETHER: {
                    this.pedestalMaterial = Material.NETHERRACK;
                    break;
                }
                case THE_END: {
                    this.pedestalMaterial = Material.END_STONE;
                    break;
                }
                default: {
                    this.pedestalMaterial = Material.STONE;
                }
            }
        }
        Function<Boolean, Material> pedestalMaterialProvider = this::getPedestalMaterial;
        Schematic.pasteSchematic(this.schematicClipboard, location, this.schematicOffset, pedestalMaterialProvider, (Runnable)this.onPasteComplete(fitAnything, location));
    }

    private BukkitRunnable onPasteComplete(final FitAnything fitAnything, final Location location) {
        return new BukkitRunnable(){

            public void run() {
                if (DefaultConfig.isNewBuildingWarn()) {
                    String structureTypeString = fitAnything.structureType.toString().toLowerCase(Locale.ROOT).replace("_", " ");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!player.hasPermission("betterstructures.warn")) continue;
                        player.spigot().sendMessage((BaseComponent)SpigotMessage.commandHoverMessage("[BetterStructures] New " + structureTypeString + " building generated! Click to teleport. Do \"/betterstructures silent\" to stop getting warnings!", "Click to teleport to " + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "\n Schem name: " + FitAnything.this.schematicContainer.getConfigFilename(), "/betterstructures teleport " + location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ()));
                    }
                }
                if (!(fitAnything instanceof FitAirBuilding)) {
                    try {
                        FitAnything.this.addPedestal(location);
                    } catch (Exception exception) {
                        Logger.warn("Failed to correctly assign pedestal material!");
                        exception.printStackTrace();
                    }
                    try {
                        if (fitAnything instanceof FitSurfaceBuilding) {
                            FitAnything.this.clearTrees(location);
                        }
                    } catch (Exception exception) {
                        Logger.warn("Failed to correctly clear trees!");
                        exception.printStackTrace();
                    }
                }
                try {
                    FitAnything.this.fillChests();
                } catch (Exception exception) {
                    Logger.warn("Failed to correctly fill chests!");
                    exception.printStackTrace();
                }
                try {
                    FitAnything.this.spawnEntities();
                } catch (Exception exception) {
                    Logger.warn("Failed to correctly spawn entities!");
                    exception.printStackTrace();
                }
                try {
                    FitAnything.this.spawnProps(fitAnything.schematicClipboard);
                } catch (Exception exception) {
                    Logger.warn("Failed to correctly spawn props!");
                    exception.printStackTrace();
                }
            }
        };
    }

    private void spawnProps(Clipboard clipboard) {
        WorldEditUtils.pasteArmorStandsOnlyFromTransformed(clipboard, this.location.clone().add(this.schematicOffset));
    }

    private void assignPedestalMaterial(Location location) {
        int z;
        int x;
        if (this instanceof FitAirBuilding) {
            return;
        }
        this.pedestalMaterial = this.schematicContainer.getSchematicConfigField().getPedestalMaterial();
        Location lowestCorner = location.clone().add(this.schematicOffset);
        int maxSurfaceHeightScan = 20;
        for (x = 0; x < this.schematicClipboard.getDimensions().x(); ++x) {
            for (z = 0; z < this.schematicClipboard.getDimensions().z(); ++z) {
                for (int y = 0; y < this.schematicClipboard.getDimensions().y(); ++y) {
                    Block groundBlock = lowestCorner.clone().add(new Vector(x, y, z)).getBlock();
                    Block aboveBlock = groundBlock.getRelative(BlockFace.UP);
                    if (!aboveBlock.getType().isSolid() || !groundBlock.getType().isSolid() || SurfaceMaterials.ignorable(groundBlock.getType())) continue;
                    this.undergroundPedestalMaterials.merge(groundBlock.getType(), 1, Integer::sum);
                }
            }
        }
        for (x = 0; x < this.schematicClipboard.getDimensions().x(); ++x) {
            block4: for (z = 0; z < this.schematicClipboard.getDimensions().z(); ++z) {
                boolean scanUp = lowestCorner.clone().add(new Vector(x, this.schematicClipboard.getDimensions().y(), z)).getBlock().getType().isSolid();
                for (int y = 0; y < maxSurfaceHeightScan; ++y) {
                    Block groundBlock = lowestCorner.clone().add(new Vector(x, scanUp ? y : -y, z)).getBlock();
                    Block aboveBlock = groundBlock.getRelative(BlockFace.UP);
                    if (aboveBlock.getType().isSolid() || !groundBlock.getType().isSolid()) continue;
                    this.surfacePedestalMaterials.merge(groundBlock.getType(), 1, Integer::sum);
                    continue block4;
                }
            }
        }
    }

    private Material getPedestalMaterial(boolean isPedestalSurface) {
        if (isPedestalSurface) {
            if (this.surfacePedestalMaterials.isEmpty()) {
                return this.pedestalMaterial;
            }
            return this.getRandomMaterialBasedOnWeight(this.surfacePedestalMaterials);
        }
        if (this.undergroundPedestalMaterials.isEmpty()) {
            return this.pedestalMaterial;
        }
        return this.getRandomMaterialBasedOnWeight(this.undergroundPedestalMaterials);
    }

    public Material getRandomMaterialBasedOnWeight(HashMap<Material, Integer> weightedMaterials) {
        int totalWeight = weightedMaterials.values().stream().mapToInt(Integer::intValue).sum();
        int randomNumber = ThreadLocalRandom.current().nextInt(totalWeight);
        int cumulativeWeight = 0;
        for (Map.Entry<Material, Integer> entry : weightedMaterials.entrySet()) {
            if (randomNumber >= (cumulativeWeight += entry.getValue().intValue())) continue;
            return entry.getKey();
        }
        throw new IllegalStateException("Weighted random selection failed.");
    }

    private void addPedestal(Location location) {
        if (this instanceof FitAirBuilding || this instanceof FitLiquidBuilding) {
            return;
        }
        Location lowestCorner = location.clone().add(this.schematicOffset);
        for (int x = 0; x < this.schematicClipboard.getDimensions().x(); ++x) {
            for (int z = 0; z < this.schematicClipboard.getDimensions().z(); ++z) {
                Block block;
                Block groundBlock = lowestCorner.clone().add(new Vector(x, 0, z)).getBlock();
                if (groundBlock.getType().isAir()) continue;
                for (int y = -1; y > -11 && SurfaceMaterials.ignorable((block = lowestCorner.clone().add(new Vector(x, y, z)).getBlock()).getType()); --y) {
                    block.setType(this.getPedestalMaterial(!block.getRelative(BlockFace.UP).getType().isSolid()));
                }
            }
        }
    }

    private void clearTrees(Location location) {
        Location highestCorner = location.clone().add(this.schematicOffset).add(new Vector(0, this.schematicClipboard.getDimensions().y() + 1, 0));
        boolean detectedTreeElement = true;
        for (int x = 0; x < this.schematicClipboard.getDimensions().x(); ++x) {
            for (int z = 0; z < this.schematicClipboard.getDimensions().z(); ++z) {
                for (int y = 0; y < 31 && detectedTreeElement; ++y) {
                    detectedTreeElement = false;
                    Block block = highestCorner.clone().add(new Vector(x, y, z)).getBlock();
                    if (!SurfaceMaterials.ignorable(block.getType()) || block.getType().isAir()) continue;
                    detectedTreeElement = true;
                    block.setType(Material.AIR);
                }
            }
        }
    }

    private void fillChests() {
        if (this.schematicContainer.getGeneratorConfigFields().getChestContents() != null) {
            for (Vector chestPosition : this.schematicContainer.getChestLocations()) {
                String treasureFilename;
                Location chestLocation = LocationProjector.project(this.location, this.schematicOffset, chestPosition);
                BlockState blockState = chestLocation.getBlock().getState();
                if (!(blockState instanceof Container)) {
                    Logger.warn("Expected a container for " + String.valueOf(chestLocation.getBlock().getType()) + " but didn't get it. Skipping this loot!");
                    continue;
                }
                Container container = (Container)blockState;
                if (this.schematicContainer.getChestContents() != null) {
                    this.schematicContainer.getChestContents().rollChestContents(container);
                    treasureFilename = this.schematicContainer.getSchematicConfigField().getTreasureFile();
                } else {
                    this.schematicContainer.getGeneratorConfigFields().getChestContents().rollChestContents(container);
                    treasureFilename = this.schematicContainer.getGeneratorConfigFields().getTreasureFilename();
                }
                ChestFillEvent chestFillEvent = new ChestFillEvent(container, treasureFilename);
                Bukkit.getServer().getPluginManager().callEvent((Event)chestFillEvent);
                if (chestFillEvent.isCancelled()) continue;
                container.update(true);
            }
        }
    }

    private void spawnEntities() {
        for (Vector vector : this.schematicContainer.getVanillaSpawns().keySet()) {
            Location signLocation = LocationProjector.project(this.location, this.schematicOffset, vector).clone();
            signLocation.getBlock().setType(Material.AIR);
            signLocation.add(new Vector(0.5, 0.0, 0.5));
            signLocation.getChunk().load();
            Entity entity = signLocation.getWorld().spawnEntity(signLocation, this.schematicContainer.getVanillaSpawns().get(vector));
            entity.setPersistent(true);
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).setRemoveWhenFarAway(false);
            }
            if (VersionChecker.serverVersionOlderThan(21, 0) || !entity.getType().equals((Object)EntityType.END_CRYSTAL)) continue;
            EnderCrystal enderCrystal = (EnderCrystal)entity;
            enderCrystal.setShowingBottom(false);
        }
        for (Vector vector : this.schematicContainer.getEliteMobsSpawns().keySet()) {
            Location eliteLocation = LocationProjector.project(this.location, this.schematicOffset, vector).clone();
            eliteLocation.getBlock().setType(Material.AIR);
            eliteLocation.add(new Vector(0.5, 0.0, 0.5));
            String bossFilename = this.schematicContainer.getEliteMobsSpawns().get(vector);
            if (!EliteMobs.Spawn(eliteLocation, bossFilename)) {
                return;
            }
            Location lowestCorner = this.location.clone().add(this.schematicOffset);
            Location highestCorner = lowestCorner.clone().add(new Vector(this.schematicClipboard.getRegion().getWidth() - 1, this.schematicClipboard.getRegion().getHeight(), this.schematicClipboard.getRegion().getLength() - 1));
            if (DefaultConfig.isProtectEliteMobsRegions() && Bukkit.getPluginManager().getPlugin("WorldGuard") != null && Bukkit.getPluginManager().getPlugin("EliteMobs") != null) {
                WorldGuard.Protect(lowestCorner, highestCorner, bossFilename, eliteLocation);
                continue;
            }
            if (worldGuardWarn) continue;
            worldGuardWarn = true;
            Logger.warn("You are not using WorldGuard, so BetterStructures could not protect a boss arena! Using WorldGuard is recommended to guarantee a fair combat experience.");
        }
        for (Map.Entry entry : this.schematicContainer.getMythicMobsSpawns().entrySet()) {
            Location mobLocation = LocationProjector.project(this.location, this.schematicOffset, (Vector)entry.getKey()).clone();
            mobLocation.getBlock().setType(Material.AIR);
            if (MythicMobs.Spawn(mobLocation, (String)entry.getValue())) continue;
            return;
        }
    }

    public SchematicContainer getSchematicContainer() {
        return this.schematicContainer;
    }

    public Clipboard getSchematicClipboard() {
        return this.schematicClipboard;
    }

    public Vector getSchematicOffset() {
        return this.schematicOffset;
    }

    public Location getLocation() {
        return this.location;
    }
}

