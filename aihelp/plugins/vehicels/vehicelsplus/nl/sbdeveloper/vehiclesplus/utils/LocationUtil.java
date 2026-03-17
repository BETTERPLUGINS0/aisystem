/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.util.Vector
 */
package nl.sbdeveloper.vehiclesplus.utils;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class LocationUtil {
    public static Vector calculateOffset(Location location, Location location2) {
        double d = location.getX() - location2.getX();
        double d2 = location.getY() - location2.getY();
        double d3 = location.getZ() - location2.getZ();
        double d4 = Math.toRadians(location.getYaw());
        double d5 = -Math.sin(d4);
        double d6 = Math.cos(d4);
        double d7 = d * d6 + d3 * d5;
        double d8 = -d * d5 + d3 * d6;
        return new Vector(d7, d2, d8);
    }

    public static Location calculateOffset(Location location, double d, double d2, double d3) {
        Vector vector = location.getDirection().setY(0).normalize();
        double d4 = location.getX() + d * vector.getX() + d3 * vector.getZ();
        double d5 = location.getY() + d2;
        double d6 = location.getZ() + d * vector.getZ() - d3 * vector.getX();
        return new Location(location.getWorld(), d4, d5, d6, location.getYaw(), location.getPitch());
    }

    @Generated
    private LocationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

