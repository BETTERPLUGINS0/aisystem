/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 */
package me.zombie_striker.qav.util;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import me.zombie_striker.qav.util.expiringmap.ExpirationPolicy;
import me.zombie_striker.qav.util.expiringmap.ExpiringMap;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockCollisionUtil {
    private static final ExpiringMap<Location, Material> CACHE;
    private static final HashMap<Material, Double> customBlockHeights;

    public static double getHeight(Block block) {
        Material material = BlockCollisionUtil.getMaterial(block.getLocation());
        if (material == null) {
            return 0.0;
        }
        if (material.name().contains("SLAB") || material.name().contains("STEP")) {
            boolean bl = XReflection.supports(13);
            if (bl) {
                try {
                    if (block.getBlockData().getAsString().toLowerCase().contains("type=double")) {
                        return 1.0;
                    }
                    return 0.5;
                } catch (Error | Exception throwable) {
                    // empty catch block
                }
            }
            if (block.getData() == 0) {
                return 0.5;
            }
            if (block.getData() == 1) {
                return 1.0;
            }
        }
        if (customBlockHeights.containsKey(material)) {
            return customBlockHeights.get(material);
        }
        return material.isSolid() ? 1.0 : 0.0;
    }

    public static boolean isSolidAt(Location location) {
        Block block = location.getBlock();
        if (block.getLocation().getY() + BlockCollisionUtil.getHeight(block) > location.getY()) {
            return true;
        }
        Block block2 = block.getRelative(0, -1, 0);
        return block2.getLocation().getY() + BlockCollisionUtil.getHeight(block2) > location.getY();
    }

    public static Material getMaterial(Location location) {
        if (CACHE.containsKey(location)) {
            return CACHE.get(location);
        }
        Material material = location.getBlock().getType();
        CACHE.put(location, material);
        return material;
    }

    public static boolean isSolid(Location location) {
        return BlockCollisionUtil.isSolid(BlockCollisionUtil.getMaterial(location));
    }

    public static boolean isSolid(Material material) {
        return material.isSolid();
    }

    static {
        customBlockHeights = new HashMap();
        CACHE = ExpiringMap.builder().expiration(5L, TimeUnit.MINUTES).expirationPolicy(ExpirationPolicy.CREATED).build();
        for (Material material : Material.values()) {
            if (material.name().endsWith("_WALL")) {
                customBlockHeights.put(material, 1.5);
            }
            if (material.name().endsWith("_FENCE_GATE") || material.name().endsWith("_FENCE")) {
                customBlockHeights.put(material, 1.5);
            }
            if (material.name().endsWith("_BED")) {
                customBlockHeights.put(material, 0.5);
            }
            if (material.name().endsWith("_SLAB") || material.name().endsWith("_FENCE")) {
                customBlockHeights.put(material, 0.5);
            }
            if (material.name().endsWith("DAYLIGHT_DETECTOR")) {
                customBlockHeights.put(material, 0.4);
            }
            if (material.name().endsWith("CARPET")) {
                customBlockHeights.put(material, 0.1);
            }
            if (material.name().endsWith("TRAPDOOR")) {
                customBlockHeights.put(material, 0.2);
            }
            if (!material.name().endsWith("RAIL")) continue;
            customBlockHeights.put(material, 0.0);
        }
    }
}

