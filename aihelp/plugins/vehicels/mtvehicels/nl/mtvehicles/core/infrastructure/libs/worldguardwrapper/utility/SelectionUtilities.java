/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.utility;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ICuboidSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.IPolygonalSelection;
import org.bukkit.Location;

public final class SelectionUtilities {
    public static ICuboidSelection createCuboidSelection(Location first, Location second) {
        Location minimum;
        Location maximum;
        if (first.getBlockY() > second.getBlockY()) {
            maximum = first;
            minimum = second;
        } else {
            maximum = second;
            minimum = first;
        }
        return new ICuboidSelection(){

            @Override
            public Location getMinimumPoint() {
                return minimum;
            }

            @Override
            public Location getMaximumPoint() {
                return maximum;
            }
        };
    }

    public static IPolygonalSelection createPolygonalSelection(final Collection<Location> points, final int minY, final int maxY) {
        return new IPolygonalSelection(){

            @Override
            public Set<Location> getPoints() {
                return new HashSet<Location>(points);
            }

            @Override
            public int getMinimumY() {
                return minY;
            }

            @Override
            public int getMaximumY() {
                return maxY;
            }
        };
    }

    private SelectionUtilities() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

