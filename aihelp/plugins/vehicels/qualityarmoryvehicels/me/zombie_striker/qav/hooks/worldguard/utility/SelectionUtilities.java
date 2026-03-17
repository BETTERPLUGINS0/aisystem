/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package me.zombie_striker.qav.hooks.worldguard.utility;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import me.zombie_striker.qav.hooks.worldguard.selection.ICuboidSelection;
import me.zombie_striker.qav.hooks.worldguard.selection.IPolygonalSelection;
import org.bukkit.Location;

public final class SelectionUtilities {
    public static ICuboidSelection createCuboidSelection(Location location, Location location2) {
        Location location3;
        Location location4;
        if (location.getBlockY() > location2.getBlockY()) {
            location4 = location;
            location3 = location2;
        } else {
            location4 = location2;
            location3 = location;
        }
        return new ICuboidSelection(){

            @Override
            public Location getMinimumPoint() {
                return location3;
            }

            @Override
            public Location getMaximumPoint() {
                return location4;
            }
        };
    }

    public static IPolygonalSelection createPolygonalSelection(final Collection<Location> collection, final int n, final int n2) {
        return new IPolygonalSelection(){

            @Override
            public Set<Location> getPoints() {
                return new HashSet<Location>(collection);
            }

            @Override
            public int getMinimumY() {
                return n;
            }

            @Override
            public int getMaximumY() {
                return n2;
            }
        };
    }

    private SelectionUtilities() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

