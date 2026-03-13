/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 */
package net.advancedplugins.as.impl.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BreakWholeTree {
    private int leavesScanned = 0;
    private final Set<Block> foundBlocks;
    private final Set<Block> scannedBlocks = new HashSet<Block>();
    private final List<Location> logs = new ArrayList<Location>();
    private final List<Location> leaves = new ArrayList<Location>();
    private final int maxLogs;
    private final int maxLeaves;
    private final List<BlockFace> dirs = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST);

    public BreakWholeTree(Block block, int n, int n2) {
        this.foundBlocks = new HashSet<Block>();
        this.maxLogs = n > 0 ? n : 1000;
        this.maxLeaves = n2 > 0 ? n2 : 300;
        Location location = block.getLocation();
        this.findLog(block, true, true);
        this.findLog(block, false, true);
        for (Location location2 : this.logs) {
            if (!(location2.getY() < location.getY())) continue;
            location = location2;
        }
        this.foundBlocks.addAll(this.logs.stream().map(Location::getBlock).collect(Collectors.toSet()));
        this.foundBlocks.remove(block);
    }

    private boolean findLog(Block block, boolean bl, boolean bl2) {
        Block block2;
        if (block == null || this.foundBlocks.contains(block)) {
            return false;
        }
        if (this.foundBlocks.size() >= this.maxLogs || this.leavesScanned >= this.maxLeaves) {
            return false;
        }
        if (this.scannedBlocks.contains(block)) {
            return false;
        }
        boolean bl3 = false;
        if (ASManager.isLog(block.getType()) && this.foundBlocks.add(block)) {
            this.logs.add(block.getLocation());
            bl3 = true;
        }
        if (block.getType().name().endsWith("LEAVES") && !this.leaves.contains(block.getLocation())) {
            this.leaves.add(block.getLocation());
            ++this.leavesScanned;
        }
        this.scannedBlocks.add(block);
        if (bl3) {
            block2 = this.dirs.iterator();
            while (block2.hasNext()) {
                BlockFace blockFace = (BlockFace)block2.next();
                this.findLog(block.getRelative(blockFace), bl, true);
            }
        }
        if ((bl3 || bl2) && !this.foundBlocks.contains(block2 = block.getRelative(bl ? BlockFace.UP : BlockFace.DOWN))) {
            this.findLog(block2, bl, bl3);
        }
        return bl3;
    }

    public Set<Block> get() {
        return this.foundBlocks;
    }
}

