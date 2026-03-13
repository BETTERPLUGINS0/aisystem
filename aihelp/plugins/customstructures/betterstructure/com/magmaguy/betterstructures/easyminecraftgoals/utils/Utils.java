/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.easyminecraftgoals.utils;

import org.bukkit.util.Vector;

public class Utils {
    private Utils() {
    }

    public static boolean distanceShorterThan(Vector source, Vector destination, double distance) {
        return source.distanceSquared(destination) < distance * distance;
    }

    public static boolean distanceGreaterThan(Vector source, Vector destination, double distance) {
        return source.distanceSquared(destination) > distance * distance;
    }
}

