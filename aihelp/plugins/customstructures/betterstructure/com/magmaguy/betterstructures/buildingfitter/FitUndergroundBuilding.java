/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World$Environment
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
import org.bukkit.World;
import org.bukkit.util.Vector;

public class FitUndergroundBuilding
extends FitAnything {
    private int lowestY;
    private int highestY;

    public FitUndergroundBuilding(Chunk chunk, SchematicContainer schematicContainer, int lowestY, int highestY, GeneratorConfigFields.StructureType structureType) {
        super(schematicContainer);
        this.structureType = structureType;
        this.lowestY = lowestY;
        this.highestY = highestY;
        this.schematicContainer = schematicContainer;
        this.schematicClipboard = schematicContainer.getClipboard();
        this.scan(chunk);
    }

    public FitUndergroundBuilding(Chunk chunk, int lowestY, int highestY, GeneratorConfigFields.StructureType structureType) {
        this.structureType = structureType;
        this.lowestY = lowestY;
        this.highestY = highestY;
        this.scan(chunk);
    }

    private void scan(Chunk chunk) {
        Location originalLocation = new Location(chunk.getWorld(), (double)chunk.getX() * 16.0, 0.0, (double)chunk.getZ() * 16.0).add(new Vector(8, 0, 8));
        switch (chunk.getWorld().getEnvironment()) {
            case NORMAL: 
            case CUSTOM: {
                originalLocation.setY((double)ThreadLocalRandom.current().nextInt(this.lowestY, this.highestY));
                break;
            }
            case NETHER: {
                Location currentLocation;
                int y;
                int tolerance;
                int highPoint;
                int lowPoint;
                boolean streak;
                if (this.structureType == GeneratorConfigFields.StructureType.UNDERGROUND_SHALLOW) {
                    streak = false;
                    lowPoint = 0;
                    highPoint = 0;
                    tolerance = 3;
                    for (y = this.lowestY; y < this.highestY; ++y) {
                        currentLocation = originalLocation.clone();
                        currentLocation.setY((double)y);
                        if (currentLocation.getBlock().getType().isSolid()) {
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
                            if (highPoint - lowPoint >= 20) break;
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
                    break;
                }
                streak = false;
                lowPoint = 0;
                highPoint = 0;
                tolerance = 3;
                for (y = this.highestY; y > this.lowestY; --y) {
                    currentLocation = originalLocation.clone();
                    currentLocation.setY((double)y);
                    if (currentLocation.getBlock().getType().isSolid()) {
                        if (streak) {
                            lowPoint = y;
                            continue;
                        }
                        highPoint = y;
                        streak = true;
                        continue;
                    }
                    if (currentLocation.getBlock().getType() == Material.VOID_AIR || currentLocation.getBlock().getType() == Material.BEDROCK || tolerance == 0) {
                        if (!streak) continue;
                        streak = false;
                        if (highPoint - lowPoint >= 20) break;
                        if (currentLocation.getBlock().getType() == Material.VOID_AIR || currentLocation.getBlock().getType() == Material.BEDROCK) {
                            return;
                        }
                        tolerance = 3;
                        continue;
                    }
                    if (!streak) continue;
                    --tolerance;
                    lowPoint = y;
                }
                if (highPoint - lowPoint < 20) {
                    return;
                }
                if (highPoint - lowPoint > 30) {
                    originalLocation.setY((double)ThreadLocalRandom.current().nextInt(lowPoint, highPoint - 20));
                    break;
                }
                originalLocation.setY((double)lowPoint + 1.0);
                break;
            }
            case THE_END: {
                Location currentLocation;
                int y;
                if (this.structureType != GeneratorConfigFields.StructureType.UNDERGROUND_SHALLOW) break;
                boolean streak = false;
                int lowPoint = 0;
                int highPoint = 0;
                int tolerance = 3;
                for (y = this.lowestY; y < this.highestY; ++y) {
                    currentLocation = originalLocation.clone();
                    currentLocation.setY((double)y);
                    if (currentLocation.getBlock().getType().isSolid()) {
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
                        if (highPoint - lowPoint >= 20) break;
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
        this.randomizeSchematicContainer(originalLocation, this.structureType);
        if (this.schematicClipboard == null) {
            return;
        }
        this.schematicOffset = WorldEditUtils.getSchematicOffset(this.schematicClipboard);
        switch (originalLocation.getWorld().getEnvironment()) {
            case NORMAL: 
            case CUSTOM: {
                if (originalLocation.getY() - Math.abs(this.schematicOffset.getY()) < (double)DefaultConfig.getLowestYNormalCustom()) {
                    originalLocation.setY((double)(DefaultConfig.getLowestYNormalCustom() + 1) + Math.abs(this.schematicOffset.getY()));
                    break;
                }
                if (!(originalLocation.getY() + Math.abs(this.schematicOffset.getY()) - (double)this.schematicClipboard.getRegion().getHeight() > (double)DefaultConfig.getHighestYNormalCustom())) break;
                originalLocation.setY((double)(DefaultConfig.getHighestYNormalCustom() - this.schematicClipboard.getRegion().getHeight()) + Math.abs(this.schematicOffset.getY()));
                break;
            }
            case NETHER: {
                if (originalLocation.getY() - Math.abs(this.schematicOffset.getY()) < (double)DefaultConfig.getLowestYNether()) {
                    originalLocation.setY((double)(DefaultConfig.getLowestYNether() + 1) + Math.abs(this.schematicOffset.getY()));
                    break;
                }
                if (!(originalLocation.getY() + Math.abs(this.schematicOffset.getY()) - (double)this.schematicClipboard.getRegion().getHeight() > (double)DefaultConfig.getHighestYNether())) break;
                originalLocation.setY((double)(DefaultConfig.getHighestYNether() - this.schematicClipboard.getRegion().getHeight()) + Math.abs(this.schematicOffset.getY()));
                break;
            }
            case THE_END: {
                if (originalLocation.getY() - Math.abs(this.schematicOffset.getY()) < (double)DefaultConfig.getLowestYEnd()) {
                    originalLocation.setY((double)(DefaultConfig.getLowestYEnd() + 1) + Math.abs(this.schematicOffset.getY()));
                    break;
                }
                if (!(originalLocation.getY() + Math.abs(this.schematicOffset.getY()) - (double)this.schematicClipboard.getRegion().getHeight() > (double)DefaultConfig.getLowestYEnd())) break;
                originalLocation.setY((double)(DefaultConfig.getLowestYEnd() - this.schematicClipboard.getRegion().getHeight()) + Math.abs(this.schematicOffset.getY()));
            }
        }
        this.chunkScan(originalLocation, 0, 0);
        if (this.highestScore < 90.0) {
            for (int chunkX = -1; chunkX < 2; ++chunkX) {
                for (int chunkZ = -1; chunkZ < 2; ++chunkZ) {
                    if (chunkX == 0 && chunkZ == 0) continue;
                    this.chunkScan(originalLocation, chunkX, chunkZ);
                    if (this.highestScore > 90.0) break;
                }
                if (this.highestScore > 90.0) break;
            }
        }
        if (this.location == null) {
            return;
        }
        this.paste(this.location);
    }

    private void chunkScan(Location originalLocation, int chunkX, int chunkZ) {
        Location iteratedLocation = originalLocation.clone().add(new Vector(chunkX * 16, 0, chunkZ * 16));
        double score = TerrainAdequacy.scan(3, this.schematicClipboard, iteratedLocation, this.schematicOffset, TerrainAdequacy.ScanType.UNDERGROUND);
        if (!originalLocation.getWorld().getEnvironment().equals((Object)World.Environment.NETHER) ? score < 70.0 : score < 50.0) {
            return;
        }
        if (score > this.highestScore) {
            this.highestScore = score;
            this.location = iteratedLocation;
        }
    }
}

