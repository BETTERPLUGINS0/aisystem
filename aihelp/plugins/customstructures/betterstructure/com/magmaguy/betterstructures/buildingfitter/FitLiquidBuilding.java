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
import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import com.magmaguy.betterstructures.schematics.SchematicContainer;
import com.magmaguy.betterstructures.util.WorldEditUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class FitLiquidBuilding
extends FitAnything {
    public FitLiquidBuilding(Chunk chunk, SchematicContainer schematicContainer) {
        super(schematicContainer);
        this.structureType = GeneratorConfigFields.StructureType.LIQUID_SURFACE;
        this.schematicContainer = schematicContainer;
        this.schematicClipboard = schematicContainer.getClipboard();
        this.scan(chunk);
    }

    public FitLiquidBuilding(Chunk chunk) {
        this.structureType = GeneratorConfigFields.StructureType.LIQUID_SURFACE;
        this.scan(chunk);
    }

    private void scan(Chunk chunk) {
        Location originalLocation = new Location(chunk.getWorld(), (double)chunk.getX() * 16.0, 0.0, (double)chunk.getZ() * 16.0).add(new Vector(8, 0, 8));
        originalLocation.setY((double)originalLocation.getWorld().getHighestBlockYAt(originalLocation));
        switch (chunk.getWorld().getEnvironment()) {
            case CUSTOM: 
            case NORMAL: {
                if (originalLocation.getBlock().isLiquid()) break;
                return;
            }
            case NETHER: {
                int netherLavaOceanHeight = 31;
                originalLocation.setY((double)netherLavaOceanHeight);
                if (originalLocation.getBlock().getType() != Material.LAVA) {
                    return;
                }
                for (int i = 1; i < 20; ++i) {
                    if (originalLocation.clone().add(new Vector(0, i, 0)).getBlock().getType().isAir()) continue;
                    return;
                }
                break;
            }
        }
        this.randomizeSchematicContainer(originalLocation, GeneratorConfigFields.StructureType.LIQUID_SURFACE);
        if (this.schematicClipboard == null) {
            return;
        }
        this.schematicOffset = WorldEditUtils.getSchematicOffset(this.schematicClipboard);
        this.chunkScan(originalLocation, 0, 0);
        if (this.highestScore < 90.0) {
            for (int chunkX = -1; chunkX < 2; ++chunkX) {
                for (int chunkZ = -1; chunkZ < 2; ++chunkZ) {
                    if (chunkX == 0 && chunkZ == 0) continue;
                    this.chunkScan(originalLocation, chunkX, chunkZ);
                    if (this.highestScore >= 90.0) break;
                }
                if (this.highestScore >= 90.0) break;
            }
        }
        if (this.location == null) {
            return;
        }
        super.paste(this.location);
    }

    private void chunkScan(Location originalLocation, int chunkX, int chunkZ) {
        Location iteratedLocation = originalLocation.clone().add(new Vector(chunkX * 16, 1, chunkZ * 16));
        double newScore = TerrainAdequacy.scan(3, this.schematicClipboard, iteratedLocation, this.schematicOffset, TerrainAdequacy.ScanType.LIQUID);
        if (newScore < 90.0) {
            return;
        }
        if (newScore == this.startingScore) {
            this.highestScore = newScore;
            this.location = iteratedLocation;
        }
    }
}

