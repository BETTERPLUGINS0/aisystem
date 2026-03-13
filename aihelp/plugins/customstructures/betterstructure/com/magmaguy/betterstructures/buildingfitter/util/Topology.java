/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.extent.clipboard.Clipboard
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World$Environment
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.buildingfitter.util;

import com.magmaguy.betterstructures.buildingfitter.util.LocationProjector;
import com.magmaguy.betterstructures.util.SurfaceMaterials;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Topology {
    public static double scan(double startingScore, int scanStep, Clipboard schematicClipboard, Location iteratedLocation, Vector schematicOffset) {
        ArrayList<Integer> heights;
        int depth;
        double score = startingScore;
        int width = schematicClipboard.getDimensions().x();
        score = Topology.scanHighestLocations(width, depth = schematicClipboard.getDimensions().z(), scanStep, iteratedLocation, schematicOffset, heights = new ArrayList<Integer>(), score);
        if (score == 0.0) {
            return 0.0;
        }
        if ((score = Topology.scanExtremeHeightDifferences(heights, score)) == 0.0) {
            return 0.0;
        }
        int averageFloorLevel = Topology.setToAverageHeight(heights, iteratedLocation);
        score = Topology.scoreTerrainHeightVariation(heights, averageFloorLevel, score);
        return score;
    }

    private static double scanHighestLocations(int width, int depth, int scanStep, Location iteratedLocation, Vector schematicOffset, ArrayList<Integer> heights, double score) {
        int totalPointAmount = (int)Math.floor(Math.floor((double)width / (double)scanStep) * Math.floor((double)depth / (double)scanStep));
        for (int x = 0; x < width; x += scanStep) {
            for (int z = 0; z < depth; z += scanStep) {
                Location projectedLocation = LocationProjector.project(iteratedLocation, new Vector(x, 0, z), schematicOffset);
                if ((projectedLocation = Topology.getHighestBlockAt(projectedLocation)) == null) {
                    return 0.0;
                }
                int safeGuard = 0;
                while (SurfaceMaterials.ignorable(projectedLocation.getBlock().getType())) {
                    if (projectedLocation.getBlock().getType().equals((Object)Material.VOID_AIR)) {
                        return 0.0;
                    }
                    projectedLocation.setY(projectedLocation.getY() - 1.0);
                    if (++safeGuard <= 50) continue;
                    Bukkit.getLogger().warning("Busted the 50 block cap for the tree scanner!");
                    break;
                }
                switch (projectedLocation.getBlock().getType()) {
                    case WATER: 
                    case LAVA: {
                        score -= 50.0 / (double)totalPointAmount;
                    }
                }
                if (score < 75.0) {
                    return 0.0;
                }
                heights.add(projectedLocation.getBlockY());
            }
        }
        return score;
    }

    private static Location getHighestBlockAt(Location location) {
        if (!location.getWorld().getEnvironment().equals((Object)World.Environment.NETHER)) {
            return location.getWorld().getHighestBlockAt(location).getLocation();
        }
        location.setY(63.0);
        if (SurfaceMaterials.ignorable(location.getBlock().getType())) {
            for (int y = (int)location.getY(); y > 30; --y) {
                location.setY((double)y);
                if (!Topology.validNetherSurface(location)) continue;
                return location;
            }
        } else {
            for (int y = (int)location.getY(); y < 100; ++y) {
                location.setY((double)y);
                if (!Topology.validNetherSurface(location)) continue;
                return location;
            }
        }
        return null;
    }

    private static boolean validNetherSurface(Location location) {
        if (SurfaceMaterials.ignorable(location.getBlock().getType()) || !SurfaceMaterials.ignorable(location.getBlock().getLocation().add(new Vector(0, 1, 0)).getBlock().getType())) {
            return false;
        }
        for (int i = 1; i < 11; ++i) {
            if (SurfaceMaterials.ignorable(location.clone().add(new Vector(0, i, 0)).getBlock().getType())) continue;
            return false;
        }
        return true;
    }

    private static double scanExtremeHeightDifferences(ArrayList<Integer> heights, double score) {
        Collections.sort(heights);
        if (Math.abs(heights.get(0) - heights.get(heights.size() - 1)) >= 20) {
            return 0.0;
        }
        return score;
    }

    private static int setToAverageHeight(ArrayList<Integer> heights, Location iteratedLocation) {
        int averageFloorLevel = 0;
        for (Integer integer : heights) {
            averageFloorLevel += integer.intValue();
        }
        iteratedLocation.setY((double)(averageFloorLevel /= heights.size()) + 1.0);
        return averageFloorLevel;
    }

    private static double scoreTerrainHeightVariation(ArrayList<Integer> heights, int averageFloorLevel, double score) {
        for (Integer integer : heights) {
            int difference = Math.abs(averageFloorLevel - integer);
            if (difference < 3) continue;
            double maxImpact = score / 2.0 / (double)heights.size();
            double currentHeightScore = (1.0 - Math.pow(difference, 2.0) * 4.0 / 100.0) * maxImpact;
            if (!((score -= currentHeightScore) < 85.0)) continue;
            return 0.0;
        }
        return score;
    }
}

