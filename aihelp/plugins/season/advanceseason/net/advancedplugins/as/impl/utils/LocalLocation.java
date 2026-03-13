/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 */
package net.advancedplugins.as.impl.utils;

import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LocalLocation
extends Location {
    private String locationName = null;

    public LocalLocation(World world, double d, double d2, double d3) {
        super(world, d, d2, d3);
    }

    public LocalLocation(World world, double d, double d2, double d3, float f, float f2) {
        super(world, d, d2, d3, f, f2);
    }

    public LocalLocation(Location location) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public static String locToEncode(Location location) {
        return new LocalLocation(location).getEncode();
    }

    public String getEncode() {
        if (this.getWorld() == null) {
            return null;
        }
        return this.getWorld().getName() + ";" + this.getX() + ";" + this.getY() + ";" + this.getZ();
    }

    public static LocalLocation getFromEncode(String string) {
        String[] stringArray = string.split(";");
        World world = Bukkit.getWorld((String)stringArray[0]);
        double d = Double.parseDouble(stringArray[1]);
        double d2 = Double.parseDouble(stringArray[2]);
        double d3 = Double.parseDouble(stringArray[3]);
        return new LocalLocation(world, d, d2, d3);
    }

    public boolean isInDistance(Location location, int n) {
        World world = location.getWorld();
        if (!world.equals((Object)this.getWorld())) {
            return false;
        }
        n ^= 2;
        return !(this.distanceSquared(location) > (double)n);
    }

    public void setName(String string) {
        this.locationName = string;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public void playParticles(Effect effect, int n, float f) {
    }

    public LocalLocation clone() {
        return new LocalLocation(super.clone());
    }

    public Optional<Block> getOptionalBlock() {
        return Optional.of(this.getBlock());
    }

    public String getChunkEncode() {
        return LocalLocation.getChunkEncode(this.getChunk());
    }

    public static String getChunkEncode(Chunk chunk) {
        return chunk.getWorld().getName() + ";" + chunk.getX() + ";" + chunk.getZ();
    }

    public static LocalLocation fromBlock(Block block) {
        return new LocalLocation(block.getLocation());
    }
}

