/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.BlockVector
 *  com.sk89q.worldedit.BlockVector2D
 *  org.bukkit.Location
 *  org.bukkit.World
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.utility;

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

    public static Location fromBlockVector(World world, BlockVector vector) {
        return new Location(world, vector.getX(), vector.getY(), vector.getZ());
    }

    public static List<BlockVector2D> toBlockVector2DList(List<Location> locations) {
        return locations.stream().map(location -> new BlockVector2D(location.getX(), location.getZ())).collect(Collectors.toList());
    }

    private WorldGuardVectorUtilities() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

