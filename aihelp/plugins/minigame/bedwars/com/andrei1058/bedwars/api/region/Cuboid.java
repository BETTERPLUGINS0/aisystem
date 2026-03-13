/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package com.andrei1058.bedwars.api.region;

import com.andrei1058.bedwars.api.region.Region;
import org.bukkit.Location;

public class Cuboid
implements Region {
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;
    private boolean protect;

    public Cuboid(Location loc, int radius, boolean protect) {
        Location l1 = loc.clone().subtract((double)radius, (double)radius, (double)radius);
        Location l2 = loc.clone().add((double)radius, (double)radius, (double)radius);
        this.minX = Math.min(l1.getBlockX(), l2.getBlockX());
        this.maxX = Math.max(l1.getBlockX(), l2.getBlockX());
        this.minY = Math.min(l1.getBlockY(), l2.getBlockY());
        this.maxY = Math.max(l1.getBlockY(), l2.getBlockY());
        this.minZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
        this.maxZ = Math.max(l1.getBlockZ(), l2.getBlockZ());
        this.protect = protect;
    }

    @Override
    public boolean isInRegion(Location l) {
        return l.getBlockX() <= this.maxX && l.getBlockX() >= this.minX && l.getY() <= (double)this.maxY && l.getY() >= (double)this.minY && l.getBlockZ() <= this.maxZ && l.getBlockZ() >= this.minZ;
    }

    @Override
    public boolean isProtected() {
        return this.protect;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public void setProtect(boolean protect) {
        this.protect = protect;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getMinY() {
        return this.minY;
    }
}

