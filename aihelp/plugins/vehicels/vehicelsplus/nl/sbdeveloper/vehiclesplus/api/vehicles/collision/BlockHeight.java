/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.block.data.Bisected$Half
 *  org.bukkit.block.data.type.Slab
 *  org.bukkit.block.data.type.Slab$Type
 *  org.bukkit.block.data.type.Snow
 *  org.bukkit.block.data.type.Stairs
 *  org.bukkit.block.data.type.TrapDoor
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.collision;

import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.TrapDoor;

public enum BlockHeight {
    AIR(0.0),
    FARMLAND(15.0),
    PATH(15.0),
    SLAB(16.0),
    STAIRS(16.0),
    TRAPDOOR(3.0),
    SNOW(16.0),
    BED(9.0);

    private final double blockHeight;

    private BlockHeight(double d) {
        this.blockHeight = d;
    }

    private static BlockHeight getBlockHeight(String string) {
        for (BlockHeight blockHeight : BlockHeight.values()) {
            if (!string.contains(blockHeight.name())) continue;
            return blockHeight;
        }
        return null;
    }

    public static double getHeight(Block block) {
        BlockHeight blockHeight = BlockHeight.getBlockHeight(block.getType().name());
        if (block.isPassable()) {
            return 0.0;
        }
        if (blockHeight == null) {
            return 1.0;
        }
        double d = blockHeight.blockHeight;
        if (blockHeight == SNOW) {
            if (XMaterial.supports(13)) {
                Snow snow = (Snow)block.getBlockData();
                d = 16.0 / (double)snow.getMaximumLayers() * (double)snow.getLayers();
            } else {
                d = 2.0 * (double)block.getData() + 2.0;
            }
        } else if (blockHeight == SLAB) {
            Slab slab = (Slab)block.getBlockData();
            if (slab.getType() == Slab.Type.BOTTOM) {
                d = 8.0;
            }
        } else if (blockHeight == STAIRS) {
            Stairs stairs = (Stairs)block.getBlockData();
            if (stairs.getHalf() == Bisected.Half.BOTTOM) {
                d = 8.0;
            }
        } else if (blockHeight == TRAPDOOR) {
            TrapDoor trapDoor = (TrapDoor)block.getBlockData();
            d = !trapDoor.isOpen() && trapDoor.getHalf() == Bisected.Half.BOTTOM ? 3.0 : 0.0;
        }
        return d / 16.0;
    }
}

