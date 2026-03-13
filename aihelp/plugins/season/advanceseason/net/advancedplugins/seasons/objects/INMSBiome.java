/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.world.level.biome.BiomeBase
 *  net.minecraft.world.level.chunk.ChunkSection
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.seasons.objects;

import java.util.Collection;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.chunk.ChunkSection;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface INMSBiome {
    public Holder<BiomeBase> getBiome();

    public Holder<BiomeBase> getBiome(int var1);

    public int getBiomeId(BiomeBase var1);

    public int getBiomeId(Holder<BiomeBase> var1);

    public String getDefaultBiome(int var1);

    public Integer getVanillaBiomeId(String var1);

    public int getBiomeID();

    public void sendChunkUpdate(Chunk var1, Player var2);

    public String getType(ChunkSection var1, int var2, int var3, int var4);

    public Collection<String> getBiomes();

    public String getBiome(Location var1);
}

