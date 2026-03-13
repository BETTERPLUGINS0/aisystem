/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPosition
 *  net.minecraft.server.level.WorldServer
 *  net.minecraft.world.level.block.state.IBlockData
 *  net.minecraft.world.level.chunk.Chunk
 *  net.minecraft.world.level.chunk.ChunkSection
 *  org.bukkit.World
 *  org.bukkit.block.data.BlockData
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.massblockedit;

import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.CraftBukkitBridge;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkSection;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

public class MassEditBlocks {
    public static void setBlockInNativeDataPalette(World world, int x, int y, int z, BlockData blockData, boolean applyPhysics) {
        WorldServer nmsWorld = CraftBukkitBridge.getServerLevel(world);
        BlockPosition blockPos = new BlockPosition(x, y, z);
        Chunk nmsChunk = nmsWorld.q(blockPos);
        IBlockData blockState = CraftBukkitBridge.getBlockState(blockData);
        int sectionY = Math.floorDiv(y, 16);
        ChunkSection chunkSection = nmsChunk.d()[sectionY - nmsWorld.ay()];
        if (applyPhysics) {
            chunkSection.a(x & 0xF, y & 0xF, z & 0xF, blockState);
        } else {
            chunkSection.a(x & 0xF, y & 0xF, z & 0xF, blockState, false);
        }
    }
}

