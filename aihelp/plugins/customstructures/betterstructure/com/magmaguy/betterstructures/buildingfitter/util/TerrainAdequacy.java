/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldedit.extent.clipboard.Clipboard
 *  com.sk89q.worldedit.math.BlockVector3
 *  com.sk89q.worldedit.world.block.BlockType
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.buildingfitter.util;

import com.magmaguy.betterstructures.buildingfitter.util.LocationProjector;
import com.magmaguy.betterstructures.util.SurfaceMaterials;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class TerrainAdequacy {
    public static double scan(int scanStep, Clipboard schematicClipboard, Location iteratedLocation, Vector schematicOffset, ScanType scanType) {
        int width = schematicClipboard.getDimensions().x();
        int depth = schematicClipboard.getDimensions().z();
        int height = schematicClipboard.getDimensions().y();
        int totalCount = 0;
        int negativeCount = 0;
        for (int x = 0; x < width; x += scanStep) {
            for (int y = 0; y < height; y += scanStep) {
                for (int z = 0; z < depth; z += scanStep) {
                    Material schematicMaterialAtPosition = BukkitAdapter.adapt((BlockType)schematicClipboard.getBlock(BlockVector3.at((int)x, (int)y, (int)z)).getBlockType());
                    Location projectedLocation = LocationProjector.project(iteratedLocation, new Vector(x, y, z), schematicOffset);
                    if (!TerrainAdequacy.isBlockAdequate(projectedLocation, schematicMaterialAtPosition, iteratedLocation.getBlockY() - 1, scanType)) {
                        ++negativeCount;
                    }
                    ++totalCount;
                }
            }
        }
        double score = 100.0 - (double)negativeCount * 100.0 / (double)totalCount;
        return score;
    }

    private static boolean isBlockAdequate(Location projectedWorldLocation, Material schematicBlockMaterial, int floorHeight, ScanType scanType) {
        int floorYValue = projectedWorldLocation.getBlockY();
        if (projectedWorldLocation.getBlock().getType().equals((Object)Material.VOID_AIR)) {
            return false;
        }
        switch (scanType.ordinal()) {
            case 0: {
                if (floorYValue > floorHeight) {
                    return SurfaceMaterials.ignorable(projectedWorldLocation.getBlock().getType()) || !schematicBlockMaterial.isAir();
                }
                return !projectedWorldLocation.getBlock().getType().isAir();
            }
            case 2: {
                return projectedWorldLocation.getBlock().getType().isAir();
            }
            case 1: {
                return projectedWorldLocation.getBlock().getType().isSolid();
            }
            case 3: {
                if (floorYValue > floorHeight) {
                    return projectedWorldLocation.getBlock().getType().isAir();
                }
                if (schematicBlockMaterial == Material.WATER || schematicBlockMaterial == Material.LAVA) {
                    return projectedWorldLocation.getBlock().isLiquid();
                }
                return true;
            }
        }
        return false;
    }

    public static enum ScanType {
        SURFACE,
        UNDERGROUND,
        AIR,
        LIQUID;

    }
}

