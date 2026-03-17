/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.util.Vector
 */
package me.zombie_striker.qav;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class BoundingBox {
    private final Location location;
    private final Vector centerOffset = new Vector(0, 1, 0);
    private double width;
    private double height;

    public BoundingBox(Location location, double d, double d2) {
        this.location = location;
        this.width = d;
        this.height = d2;
    }

    public void setHeight(double d) {
        this.height = d;
    }

    public boolean intersects(Location location) {
        return this.intersects(location, this.centerOffset);
    }

    public boolean intersects(Location location, Vector vector) {
        Location location2 = this.location.clone().add(vector);
        double d = location.getX() - location2.getX();
        double d2 = location.getY() - location2.getY();
        double d3 = location.getZ() - location2.getZ();
        if (d * d + d3 * d3 <= this.width * this.width) {
            return d2 <= this.height && d2 >= 0.0;
        }
        return false;
    }

    public boolean intersects(Location location, Vector vector, int n) {
        if (this.location.getWorld() != null && !this.location.getWorld().equals((Object)location.getWorld())) {
            return false;
        }
        Location location2 = this.location.clone().add(this.centerOffset);
        double d = location.distance(location2);
        if (d > (double)n + this.width) {
            return false;
        }
        Vector vector2 = vector.clone();
        vector2.normalize().multiply(d);
        return this.intersects(location, vector2);
    }

    public Location getLocation() {
        return this.location;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double d) {
        this.width = d;
    }
}

