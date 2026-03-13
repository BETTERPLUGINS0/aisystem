/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.buildingfitter.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class LocationProjector {
    public static Location project(Location worldAnchorPoint, Vector schematicOffset) {
        return worldAnchorPoint.clone().add(schematicOffset);
    }

    public static Location project(Location worldAnchorPoint, Vector schematicOffset, Vector relativeBlockLocation) {
        return LocationProjector.project(worldAnchorPoint, schematicOffset).add(relativeBlockLocation);
    }
}

