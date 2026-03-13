/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData
 *  net.minecraft.world.level.chunk.ChunkSection
 *  org.bukkit.World
 */
package net.advancedplugins.seasons.objects;

import net.advancedplugins.seasons.enums.Season;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.chunk.ChunkSection;
import org.bukkit.World;

public interface IProtocolLib {
    public Integer getBiome(int var1, Season var2, boolean var3);

    public ChunkSection[] getChunkSections(ClientboundLevelChunkPacketData var1, World var2);

    public byte[] convertToBytes(ChunkSection[] var1);
}

