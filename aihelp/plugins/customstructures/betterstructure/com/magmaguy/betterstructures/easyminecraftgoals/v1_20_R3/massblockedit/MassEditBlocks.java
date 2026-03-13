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
 *  org.bukkit.craftbukkit.v1_20_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_20_R3.block.data.CraftBlockData
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R3.massblockedit;

import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkSection;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.block.data.CraftBlockData;

public class MassEditBlocks {
    public static void setBlockInNativeDataPalette(World world, int x, int y, int z, BlockData blockData, boolean applyPhysics) {
        WorldServer nmsWorld = ((CraftWorld)world).getHandle();
        BlockPosition blockPos = new BlockPosition(x, y, z);
        Chunk nmsChunk = nmsWorld.m(blockPos);
        IBlockData blockState = ((CraftBlockData)blockData).getState();
        int sectionY = Math.floorDiv(y, 16);
        ChunkSection chunkSection = nmsChunk.d()[sectionY - nmsWorld.an()];
        if (applyPhysics) {
            chunkSection.a(x & 0xF, y & 0xF, z & 0xF, blockState);
        } else {
            chunkSection.a(x & 0xF, y & 0xF, z & 0xF, blockState, false);
        }
    }
}

