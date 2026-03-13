/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.buildingfitter;

import com.magmaguy.betterstructures.buildingfitter.FitAnything;
import com.magmaguy.betterstructures.buildingfitter.util.TerrainAdequacy;
import com.magmaguy.betterstructures.config.DefaultConfig;
import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import com.magmaguy.betterstructures.schematics.SchematicContainer;
import com.magmaguy.betterstructures.util.WorldEditUtils;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class FitAirBuilding
extends FitAnything {
    public FitAirBuilding(Chunk chunk, SchematicContainer schematicContainer) {
        super(schematicContainer);
        this.structureType = GeneratorConfigFields.StructureType.SKY;
        this.schematicContainer = schematicContainer;
        this.schematicClipboard = schematicContainer.getClipboard();
        this.scan(chunk);
    }

    public FitAirBuilding(Chunk chunk) {
        this.structureType = GeneratorConfigFields.StructureType.SKY;
        this.scan(chunk);
    }

    private void scan(Chunk chunk) {
        int altitude = 0;
        switch (chunk.getWorld().getEnvironment()) {
            case NORMAL: 
            case CUSTOM: {
                altitude = ThreadLocalRandom.current().nextInt(DefaultConfig.getNormalCustomAirBuildingMinAltitude(), DefaultConfig.getNormalCustomAirBuildingMaxAltitude() + 1);
                break;
            }
            case NETHER: {
                altitude = 0;
                break;
            }
            case THE_END: {
                altitude = ThreadLocalRandom.current().nextInt(DefaultConfig.getEndAirBuildMinAltitude(), DefaultConfig.getEndAirBuildMinAltitude() + 1);
            }
        }
        Location originalLocation = chunk.getWorld().getHighestBlockAt(chunk.getX() * 16 + 8, chunk.getZ() * 16 + 8).getLocation().add(new Vector(0, altitude, 0));
        switch (chunk.getWorld().getEnvironment()) {
            case NORMAL: 
            case CUSTOM: {
                break;
            }
            case NETHER: {
                boolean streak = false;
                int lowestY = 45;
                int highestY = 100;
                int lowPoint = 0;
                int highPoint = 0;
                int tolerance = 3;
                for (int y = lowestY; y < highestY; ++y) {
                    Location currentLocation = originalLocation.clone();
                    currentLocation.setY((double)y);
                    if (currentLocation.getBlock().getType().isAir()) {
                        if (streak) {
                            highPoint = y;
                            continue;
                        }
                        lowPoint = y;
                        streak = true;
                        continue;
                    }
                    if (currentLocation.getBlock().getType() == Material.VOID_AIR || currentLocation.getBlock().getType() == Material.BEDROCK || tolerance == 0) {
                        if (!streak) continue;
                        streak = false;
                        if (highPoint - lowPoint >= 40) break;
                        if (currentLocation.getBlock().getType() == Material.VOID_AIR || currentLocation.getBlock().getType() == Material.BEDROCK) {
                            return;
                        }
                        tolerance = 3;
                        continue;
                    }
                    if (!streak) continue;
                    --tolerance;
                    highPoint = y;
                }
                if (highPoint - lowPoint < 20) {
                    return;
                }
                if (highPoint - lowPoint > 30) {
                    originalLocation.setY((double)ThreadLocalRandom.current().nextInt(lowPoint + 1, highPoint - 20));
                    break;
                }
                originalLocation.setY((double)lowPoint + 1.0);
            }
        }
        this.randomizeSchematicContainer(originalLocation, GeneratorConfigFields.StructureType.SKY);
        if (this.schematicClipboard == null) {
            return;
        }
        this.schematicOffset = WorldEditUtils.getSchematicOffset(this.schematicClipboard);
        this.chunkScan(originalLocation, 0, 0);
        if (this.location == null) {
            for (int chunkX = -1; chunkX < 2; ++chunkX) {
                for (int chunkZ = -1; chunkZ < 2; ++chunkZ) {
                    if (chunkX == 0 && chunkZ == 0) continue;
                    this.chunkScan(originalLocation, chunkX, chunkZ);
                    if (this.location != null) break;
                }
                if (this.location != null) break;
            }
        }
        if (this.location == null) {
            return;
        }
        this.paste(this.location);
    }

    private void chunkScan(Location originalLocation, int chunkX, int chunkZ) {
        Location iteratedLocation = originalLocation.clone().add(new Vector(chunkX * 16, 0, chunkZ * 16));
        double newScore = TerrainAdequacy.scan(3, this.schematicClipboard, iteratedLocation, this.schematicOffset, TerrainAdequacy.ScanType.AIR);
        if (newScore == this.startingScore) {
            this.location = iteratedLocation;
        }
    }
}

