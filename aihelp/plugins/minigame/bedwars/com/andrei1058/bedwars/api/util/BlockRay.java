/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.util.NumberConversions
 *  org.bukkit.util.Vector
 */
package com.andrei1058.bedwars.api.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class BlockRay
implements Iterator<Block> {
    private final World world;
    private final Vector delta;
    private final double multiple;
    private final int parts;
    private int consumed;
    private final double xOffset;
    private final double yOffset;
    private final double zOffset;
    private double lcx;
    private double lcy;
    private double lcz;
    private int currentBlock;
    private final Block[] blockQueue;

    public BlockRay(World world, Vector src, Vector dst, double step) {
        this.delta = new Vector(dst.getBlockX() - src.getBlockX(), dst.getBlockY() - src.getBlockY(), dst.getBlockZ() - src.getBlockZ());
        if (this.delta.lengthSquared() == 0.0) {
            throw new IllegalArgumentException("The source vector is the same as the destination vector");
        }
        this.world = world;
        this.xOffset = (double)src.getBlockX() + 0.5;
        this.yOffset = (double)src.getBlockY() + 0.5;
        this.zOffset = (double)src.getBlockZ() + 0.5;
        this.lcx = 0.0;
        this.lcy = 0.0;
        this.lcz = 0.0;
        this.multiple = 1.0 / (this.delta.length() / step);
        this.parts = NumberConversions.ceil((double)(this.delta.length() / step));
        this.blockQueue = new Block[7];
        this.currentBlock = 6;
        this.scan();
    }

    @Override
    public boolean hasNext() {
        return this.consumed <= this.parts || this.currentBlock >= 0;
    }

    @Override
    public Block next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException("No more blocks");
        }
        if (this.currentBlock < 0) {
            this.scan();
            this.currentBlock = 6;
        }
        return this.blockQueue[this.currentBlock--];
    }

    private void scan() {
        double cx = this.multiple * this.delta.getX() * (double)this.consumed;
        double cy = this.multiple * this.delta.getY() * (double)this.consumed;
        double cz = this.multiple * this.delta.getZ() * (double)this.consumed;
        int lastXFloor = NumberConversions.floor((double)(this.xOffset + this.lcx));
        int lastYFloor = NumberConversions.floor((double)(this.yOffset + this.lcy));
        int lastZFloor = NumberConversions.floor((double)(this.zOffset + this.lcz));
        int currentXFloor = NumberConversions.floor((double)(this.xOffset + cx));
        int currentYFloor = NumberConversions.floor((double)(this.yOffset + cy));
        int currentZFloor = NumberConversions.floor((double)(this.zOffset + cz));
        this.blockQueue[0] = this.world.getBlockAt(currentXFloor, lastYFloor, lastZFloor);
        this.blockQueue[1] = this.world.getBlockAt(lastXFloor, currentYFloor, lastZFloor);
        this.blockQueue[2] = this.world.getBlockAt(lastXFloor, lastYFloor, currentZFloor);
        this.blockQueue[3] = this.world.getBlockAt(currentXFloor, currentYFloor, lastZFloor);
        this.blockQueue[4] = this.world.getBlockAt(currentXFloor, lastYFloor, currentZFloor);
        this.blockQueue[5] = this.world.getBlockAt(lastXFloor, currentYFloor, currentZFloor);
        this.blockQueue[6] = this.world.getBlockAt(currentXFloor, currentYFloor, currentZFloor);
        this.lcx = cx;
        this.lcy = cy;
        this.lcz = cz;
        ++this.consumed;
    }
}

