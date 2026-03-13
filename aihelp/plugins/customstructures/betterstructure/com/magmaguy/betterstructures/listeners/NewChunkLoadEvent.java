/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.world.ChunkLoadEvent
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.magmaguy.betterstructures.listeners;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.buildingfitter.FitAirBuilding;
import com.magmaguy.betterstructures.buildingfitter.FitLiquidBuilding;
import com.magmaguy.betterstructures.buildingfitter.FitSurfaceBuilding;
import com.magmaguy.betterstructures.buildingfitter.FitUndergroundShallowBuilding;
import com.magmaguy.betterstructures.buildingfitter.util.FitUndergroundDeepBuilding;
import com.magmaguy.betterstructures.config.DefaultConfig;
import com.magmaguy.betterstructures.config.ValidWorldsConfig;
import com.magmaguy.betterstructures.config.generators.GeneratorConfigFields;
import com.magmaguy.betterstructures.config.modulegenerators.ModuleGeneratorsConfig;
import com.magmaguy.betterstructures.config.modulegenerators.ModuleGeneratorsConfigFields;
import com.magmaguy.betterstructures.modules.WFCGenerator;
import com.magmaguy.betterstructures.schematics.SchematicContainer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NewChunkLoadEvent
implements Listener {
    private static HashSet<Chunk> loadingChunks = new HashSet();

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onChunkLoad(final ChunkLoadEvent event) {
        if (!event.isNewChunk()) {
            return;
        }
        if (loadingChunks.contains(event.getChunk())) {
            return;
        }
        loadingChunks.add(event.getChunk());
        new BukkitRunnable(this){

            public void run() {
                loadingChunks.remove(event.getChunk());
            }
        }.runTaskLater(MetadataHandler.PLUGIN, 20L);
        if (!ValidWorldsConfig.isValidWorld(event.getWorld())) {
            return;
        }
        this.surfaceScanner(event.getChunk());
        this.shallowUndergroundScanner(event.getChunk());
        this.deepUndergroundScanner(event.getChunk());
        this.skyScanner(event.getChunk());
        this.liquidSurfaceScanner(event.getChunk());
        this.dungeonScanner(event.getChunk());
    }

    private boolean isValidStructurePosition(Chunk chunk, GeneratorConfigFields.StructureType structureType, int gridDistance, int maxOffset) {
        int x = chunk.getX();
        int z = chunk.getZ();
        long worldSeed = chunk.getWorld().getSeed();
        long typeSeed = worldSeed + (long)(structureType.name().hashCode() * 7919);
        for (int gridX = (x - maxOffset) / gridDistance - 1; gridX <= (x + maxOffset) / gridDistance + 1; ++gridX) {
            for (int gridZ = (z - maxOffset) / gridDistance - 1; gridZ <= (z + maxOffset) / gridDistance + 1; ++gridZ) {
                int baseX = gridX * gridDistance;
                int baseZ = gridZ * gridDistance;
                if (gridZ % 2 != 0) {
                    baseX += gridDistance / 2;
                }
                Random cellRandom = new Random(typeSeed ^ ((long)baseX << 32 | (long)baseZ & 0xFFFFFFFFL));
                int offsetX = maxOffset > 0 ? cellRandom.nextInt(maxOffset * 2 + 1) - maxOffset : 0;
                int offsetZ = maxOffset > 0 ? cellRandom.nextInt(maxOffset * 2 + 1) - maxOffset : 0;
                int structureX = baseX + offsetX;
                int structureZ = baseZ + offsetZ;
                if (x != structureX || z != structureZ) continue;
                return true;
            }
        }
        return false;
    }

    private void surfaceScanner(Chunk chunk) {
        if (SchematicContainer.getSchematics().get((Object)GeneratorConfigFields.StructureType.SURFACE).isEmpty()) {
            return;
        }
        if (!this.isValidStructurePosition(chunk, GeneratorConfigFields.StructureType.SURFACE, DefaultConfig.getDistanceSurface(), DefaultConfig.getMaxOffsetSurface())) {
            return;
        }
        new FitSurfaceBuilding(chunk);
    }

    private void shallowUndergroundScanner(Chunk chunk) {
        if (SchematicContainer.getSchematics().get((Object)GeneratorConfigFields.StructureType.UNDERGROUND_SHALLOW).isEmpty()) {
            return;
        }
        if (!this.isValidStructurePosition(chunk, GeneratorConfigFields.StructureType.UNDERGROUND_SHALLOW, DefaultConfig.getDistanceShallow(), DefaultConfig.getMaxOffsetShallow())) {
            return;
        }
        FitUndergroundShallowBuilding.fit(chunk);
    }

    private void deepUndergroundScanner(Chunk chunk) {
        if (SchematicContainer.getSchematics().get((Object)GeneratorConfigFields.StructureType.UNDERGROUND_DEEP).isEmpty()) {
            return;
        }
        if (!this.isValidStructurePosition(chunk, GeneratorConfigFields.StructureType.UNDERGROUND_DEEP, DefaultConfig.getDistanceDeep(), DefaultConfig.getMaxOffsetDeep())) {
            return;
        }
        FitUndergroundDeepBuilding.fit(chunk);
    }

    private void skyScanner(Chunk chunk) {
        if (SchematicContainer.getSchematics().get((Object)GeneratorConfigFields.StructureType.SKY).isEmpty()) {
            return;
        }
        if (!this.isValidStructurePosition(chunk, GeneratorConfigFields.StructureType.SKY, DefaultConfig.getDistanceSky(), DefaultConfig.getMaxOffsetSky())) {
            return;
        }
        new FitAirBuilding(chunk);
    }

    private void liquidSurfaceScanner(Chunk chunk) {
        if (SchematicContainer.getSchematics().get((Object)GeneratorConfigFields.StructureType.LIQUID_SURFACE).isEmpty()) {
            return;
        }
        if (!this.isValidStructurePosition(chunk, GeneratorConfigFields.StructureType.LIQUID_SURFACE, DefaultConfig.getDistanceLiquid(), DefaultConfig.getMaxOffsetLiquid())) {
            return;
        }
        new FitLiquidBuilding(chunk);
    }

    private void dungeonScanner(Chunk chunk) {
        if (ModuleGeneratorsConfig.getModuleGenerators().isEmpty()) {
            return;
        }
        if (!this.isValidStructurePosition(chunk, GeneratorConfigFields.StructureType.DUNGEON, DefaultConfig.getDistanceDungeon(), DefaultConfig.getMaxOffsetDungeon())) {
            return;
        }
        ArrayList<ModuleGeneratorsConfigFields> validatedGenerators = new ArrayList<ModuleGeneratorsConfigFields>();
        for (ModuleGeneratorsConfigFields moduleGeneratorsConfigFields : ModuleGeneratorsConfig.getModuleGenerators().values()) {
            if (moduleGeneratorsConfigFields.getValidWorlds() != null && !moduleGeneratorsConfigFields.getValidWorlds().isEmpty() && !moduleGeneratorsConfigFields.getValidWorlds().contains(chunk.getWorld().getName()) || moduleGeneratorsConfigFields.getValidWorldEnvironments() != null && !moduleGeneratorsConfigFields.getValidWorldEnvironments().isEmpty() && !moduleGeneratorsConfigFields.getValidWorldEnvironments().contains(chunk.getWorld().getEnvironment())) continue;
            validatedGenerators.add(moduleGeneratorsConfigFields);
        }
        if (validatedGenerators.isEmpty()) {
            return;
        }
        ModuleGeneratorsConfigFields moduleGeneratorsConfigFields = (ModuleGeneratorsConfigFields)validatedGenerators.get(ThreadLocalRandom.current().nextInt(0, ModuleGeneratorsConfig.getModuleGenerators().size()));
        new WFCGenerator(moduleGeneratorsConfigFields, chunk.getBlock(8, moduleGeneratorsConfigFields.getCenterModuleAltitude(), 8).getLocation());
    }
}

