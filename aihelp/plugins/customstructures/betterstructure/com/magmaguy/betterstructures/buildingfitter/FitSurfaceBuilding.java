/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.World$Environment
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.buildingfitter;

import com.magmaguy.betterstructures.buildingfitter.FitAnything;
import com.magmaguy.betterstructures.buildingfitter.util.TerrainAdequacy;
import com.magmaguy.betterstructures.buildingfitter.util.Topology;
import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import com.magmaguy.betterstructures.schematics.SchematicContainer;
import com.magmaguy.betterstructures.util.WorldEditUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class FitSurfaceBuilding
extends FitAnything {
    public FitSurfaceBuilding(Chunk chunk, SchematicContainer schematicContainer) {
        super(schematicContainer);
        this.structureType = GeneratorConfigFields.StructureType.SURFACE;
        this.schematicContainer = schematicContainer;
        this.schematicClipboard = schematicContainer.getClipboard();
        this.scan(chunk);
    }

    public FitSurfaceBuilding(Chunk chunk) {
        this.structureType = GeneratorConfigFields.StructureType.SURFACE;
        this.scan(chunk);
    }

    private void scan(Chunk chunk) {
        Location originalLocation = new Location(chunk.getWorld(), (double)chunk.getX() * 16.0, 0.0, (double)chunk.getZ() * 16.0).add(new Vector(8, 0, 8));
        originalLocation.setY((double)originalLocation.getWorld().getHighestBlockYAt(originalLocation));
        this.randomizeSchematicContainer(originalLocation, GeneratorConfigFields.StructureType.SURFACE);
        if (this.schematicClipboard == null) {
            return;
        }
        this.schematicOffset = WorldEditUtils.getSchematicOffset(this.schematicClipboard);
        this.chunkScan(originalLocation, 0, 0);
        if (this.highestScore < 50.0) {
            for (int chunkX = -1; chunkX < 2; ++chunkX) {
                for (int chunkZ = -1; chunkZ < 2; ++chunkZ) {
                    if (chunkX == -1 && chunkZ == -1 || chunkX == 1 && chunkZ == 1 || chunkX == -1 && chunkZ == 1 || chunkX == 1 && chunkZ == -1) continue;
                    this.chunkScan(originalLocation, chunkX, chunkZ);
                    if (this.highestScore > 50.0) break;
                }
                if (this.highestScore > 50.0) break;
            }
        }
        if (this.location == null) {
            return;
        }
        super.paste(this.location);
    }

    private void chunkScan(Location originalLocation, int chunkX, int chunkZ) {
        double score;
        Location iteratedLocation = originalLocation.clone().add(new Vector(chunkX * 16, 0, chunkZ * 16));
        if (originalLocation.getWorld().getEnvironment().equals((Object)World.Environment.NETHER)) {
            this.startingScore = 200.0;
        }
        if ((score = Topology.scan(this.startingScore, 3, this.schematicClipboard, iteratedLocation, this.schematicOffset)) == 0.0) {
            return;
        }
        double adequacyScore = TerrainAdequacy.scan(3, this.schematicClipboard, iteratedLocation, this.schematicOffset, TerrainAdequacy.ScanType.SURFACE);
        if ((score += 0.5 * adequacyScore) == 0.0) {
            return;
        }
        if (score > this.highestScore) {
            this.highestScore = score;
            this.location = iteratedLocation;
        }
    }
}

