/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.util.Vector
 */
package nl.sbdeveloper.vehiclesplus.utils.math;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Predicate;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class BoundingBox
implements Serializable {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public BoundingBox(double d, double d2, double d3, double d4, double d5, double d6) {
        this.minX = Math.min(d, d4);
        this.minY = Math.min(d2, d5);
        this.minZ = Math.min(d3, d6);
        this.maxX = Math.max(d, d4);
        this.maxY = Math.max(d2, d5);
        this.maxZ = Math.max(d3, d6);
    }

    public BoundingBox(Vector vector, Vector vector2) {
        this(vector.getX(), vector.getY(), vector.getZ(), vector2.getX(), vector2.getY(), vector2.getZ());
    }

    public BoundingBox(Location location, Location location2) {
        this(location.getX(), location.getY(), location.getZ(), location2.getX(), location2.getY(), location2.getZ());
    }

    public BoundingBox expand(double d, double d2, double d3) {
        double d4 = this.minX - d;
        double d5 = this.minY - d2;
        double d6 = this.minZ - d3;
        double d7 = this.maxX + d;
        double d8 = this.maxY + d2;
        double d9 = this.maxZ + d3;
        return new BoundingBox(d4, d5, d6, d7, d8, d9);
    }

    public BoundingBox expand(double d) {
        return this.expand(d, d, d);
    }

    public BoundingBox shrink(double d, double d2, double d3) {
        return this.expand(-d, -d2, -d3);
    }

    public BoundingBox shrink(double d) {
        return this.shrink(d, d, d);
    }

    public BoundingBox add(double d, double d2, double d3) {
        double d4 = this.minX;
        double d5 = this.minY;
        double d6 = this.minZ;
        double d7 = this.maxX;
        double d8 = this.maxY;
        double d9 = this.maxZ;
        if (d < 0.0) {
            d4 += d;
        } else if (d > 0.0) {
            d7 += d;
        }
        if (d2 < 0.0) {
            d5 += d2;
        } else if (d2 > 0.0) {
            d8 += d2;
        }
        if (d3 < 0.0) {
            d6 += d3;
        } else if (d3 > 0.0) {
            d9 += d3;
        }
        return new BoundingBox(d4, d5, d6, d7, d8, d9);
    }

    public BoundingBox translate(double d, double d2, double d3) {
        return new BoundingBox(this.minX + d, this.minY + d2, this.minZ + d3, this.maxX + d, this.maxY + d2, this.maxZ + d3);
    }

    public boolean contains(double d, double d2, double d3) {
        return d > this.minX && d < this.maxX && d2 > this.minY && d2 < this.maxY && d3 > this.minZ && d3 < this.maxZ;
    }

    public boolean contains(double d, double d2, double d3, double d4, double d5, double d6) {
        return this.minX < d && this.maxX > d4 && this.minY < d2 && this.maxY > d5 && this.minZ < d3 && this.maxZ > d6;
    }

    public boolean contains(BoundingBox boundingBox) {
        return this.contains(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
    }

    public boolean intersects(double d, double d2, double d3, double d4, double d5, double d6) {
        return this.minX < d4 && this.maxX > d && this.minY < d5 && this.maxY > d2 && this.minZ < d6 && this.maxZ > d3;
    }

    public boolean intersects(BoundingBox boundingBox) {
        return this.intersects(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
    }

    public BoundingBox combine(BoundingBox boundingBox) {
        double d = Math.min(this.minX, boundingBox.minX);
        double d2 = Math.min(this.minY, boundingBox.minY);
        double d3 = Math.min(this.minZ, boundingBox.minZ);
        double d4 = Math.max(this.maxX, boundingBox.maxX);
        double d5 = Math.max(this.maxY, boundingBox.maxY);
        double d6 = Math.max(this.maxZ, boundingBox.maxZ);
        return new BoundingBox(d, d2, d3, d4, d5, d6);
    }

    public Vector getMinBukkitVector() {
        return new Vector(this.minX, this.minY, this.minZ);
    }

    public Vector getMaxBukkitVector() {
        return new Vector(this.maxX, this.maxY, this.maxZ);
    }

    public Collection<Entity> getNearbyEntities(World world, Predicate<? super Entity> predicate) {
        return world.getNearbyEntities(this.calculateCenter(world), this.calculateRadiusX(), this.calculateRadiusY(), this.calculateRadiusY(), predicate);
    }

    private Location calculateCenter(World world) {
        double d = (this.maxX + this.minX) / 2.0;
        double d2 = (this.maxY + this.minY) / 2.0;
        double d3 = (this.maxZ + this.minZ) / 2.0;
        return new Location(world, d, d2, d3);
    }

    private double calculateRadiusX() {
        return (this.maxX - this.minX) / 2.0;
    }

    private double calculateRadiusY() {
        return (this.maxY - this.minY) / 2.0;
    }

    private double calculateRadiusZ() {
        return (this.maxZ - this.minZ) / 2.0;
    }

    @Generated
    public double getMinX() {
        return this.minX;
    }

    @Generated
    public double getMinY() {
        return this.minY;
    }

    @Generated
    public double getMinZ() {
        return this.minZ;
    }

    @Generated
    public double getMaxX() {
        return this.maxX;
    }

    @Generated
    public double getMaxY() {
        return this.maxY;
    }

    @Generated
    public double getMaxZ() {
        return this.maxZ;
    }

    @Generated
    public String toString() {
        return "BoundingBox(minX=" + this.getMinX() + ", minY=" + this.getMinY() + ", minZ=" + this.getMinZ() + ", maxX=" + this.getMaxX() + ", maxY=" + this.getMaxY() + ", maxZ=" + this.getMaxZ() + ")";
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof BoundingBox)) {
            return false;
        }
        BoundingBox boundingBox = (BoundingBox)object;
        if (!boundingBox.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getMinX(), boundingBox.getMinX()) != 0) {
            return false;
        }
        if (Double.compare(this.getMinY(), boundingBox.getMinY()) != 0) {
            return false;
        }
        if (Double.compare(this.getMinZ(), boundingBox.getMinZ()) != 0) {
            return false;
        }
        if (Double.compare(this.getMaxX(), boundingBox.getMaxX()) != 0) {
            return false;
        }
        if (Double.compare(this.getMaxY(), boundingBox.getMaxY()) != 0) {
            return false;
        }
        return Double.compare(this.getMaxZ(), boundingBox.getMaxZ()) == 0;
    }

    @Generated
    protected boolean canEqual(Object object) {
        return object instanceof BoundingBox;
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = Double.doubleToLongBits(this.getMinX());
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        long l2 = Double.doubleToLongBits(this.getMinY());
        n2 = n2 * 59 + (int)(l2 >>> 32 ^ l2);
        long l3 = Double.doubleToLongBits(this.getMinZ());
        n2 = n2 * 59 + (int)(l3 >>> 32 ^ l3);
        long l4 = Double.doubleToLongBits(this.getMaxX());
        n2 = n2 * 59 + (int)(l4 >>> 32 ^ l4);
        long l5 = Double.doubleToLongBits(this.getMaxY());
        n2 = n2 * 59 + (int)(l5 >>> 32 ^ l5);
        long l6 = Double.doubleToLongBits(this.getMaxZ());
        n2 = n2 * 59 + (int)(l6 >>> 32 ^ l6);
        return n2;
    }
}

