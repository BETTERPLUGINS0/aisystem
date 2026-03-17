/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.util.Vector
 */
package nl.sbdeveloper.vehiclesplus.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class VectorUtils {
    private VectorUtils() {
    }

    public static Vector rotateAroundAxisX(Vector vector, double d) {
        double d2 = Math.cos(d);
        double d3 = Math.sin(d);
        double d4 = vector.getY() * d2 - vector.getZ() * d3;
        double d5 = vector.getY() * d3 + vector.getZ() * d2;
        return vector.setY(d4).setZ(d5);
    }

    public static Vector rotateAroundAxisY(Vector vector, double d) {
        double d2 = Math.cos(d);
        double d3 = Math.sin(d);
        double d4 = vector.getX() * d2 + vector.getZ() * d3;
        double d5 = vector.getX() * -d3 + vector.getZ() * d2;
        return vector.setX(d4).setZ(d5);
    }

    public static Vector rotateAroundAxisZ(Vector vector, double d) {
        double d2 = Math.cos(d);
        double d3 = Math.sin(d);
        double d4 = vector.getX() * d2 - vector.getY() * d3;
        double d5 = vector.getX() * d3 + vector.getY() * d2;
        return vector.setX(d4).setY(d5);
    }

    public static Vector rotateVector(Vector vector, double d, double d2, double d3) {
        VectorUtils.rotateAroundAxisX(vector, d);
        VectorUtils.rotateAroundAxisY(vector, d2);
        VectorUtils.rotateAroundAxisZ(vector, d3);
        return vector;
    }

    public static Vector rotateVector(Vector vector, Location location) {
        return VectorUtils.rotateVector(vector, location.getYaw(), location.getPitch());
    }

    public static Vector rotateVector(Vector vector, float f, float f2) {
        double d = Math.toRadians(-1.0f * (f + 90.0f));
        double d2 = Math.toRadians(-f2);
        double d3 = Math.cos(d);
        double d4 = Math.cos(d2);
        double d5 = Math.sin(d);
        double d6 = Math.sin(d2);
        double d7 = vector.getX();
        double d8 = vector.getY();
        double d9 = d7 * d4 - d8 * d6;
        double d10 = d7 * d6 + d8 * d4;
        double d11 = vector.getZ();
        d7 = d9;
        double d12 = d11 * d3 - d7 * d5;
        d9 = d11 * d5 + d7 * d3;
        return new Vector(d9, d10, d12);
    }

    public static double angleToXAxis(Vector vector) {
        return Math.atan2(vector.getX(), vector.getY());
    }
}

