/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.BlockVector
 *  com.sk89q.worldedit.BlockVector2D
 *  org.bukkit.Location
 *  org.bukkit.World
 */
package me.zombie_striker.qav.hooks.worldguard.implementation.v6.utility;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.World;

public final class WorldGuardVectorUtilities {
    public static BlockVector toBlockVector(Location location) {
        return new BlockVector(location.getX(), location.getY(), location.getZ());
    }

    public static Location fromBlockVector(World world, BlockVector blockVector) {
        return new Location(world, blockVector.getX(), blockVector.getY(), blockVector.getZ());
    }

    public static List<BlockVector2D> toBlockVector2DList(List<Location> list) {
        return list.stream().map(location -> new BlockVector2D(location.getX(), location.getZ())).collect(Collectors.toList());
    }

    private WorldGuardVectorUtilities() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

