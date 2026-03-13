package com.volmit.iris.engine.data.chunk;

import com.volmit.iris.core.nms.BiomeBaseInjector;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public interface TerrainChunk extends BiomeGrid, ChunkData {
   static TerrainChunk create(World world) {
      return new LinkedTerrainChunk(world);
   }

   static TerrainChunk create(World world, BiomeGrid grid) {
      return new LinkedTerrainChunk(world, grid);
   }

   static TerrainChunk createUnsafe(World world, BiomeGrid grid) {
      LinkedTerrainChunk ltc = new LinkedTerrainChunk(world, grid);
      ltc.setUnsafe(true);
      return ltc;
   }

   static TerrainChunk create(ChunkData raw, BiomeGrid grid) {
      return new LinkedTerrainChunk(grid, raw);
   }

   BiomeBaseInjector getBiomeBaseInjector();

   /** @deprecated */
   @Deprecated
   Biome getBiome(int x, int z);

   Biome getBiome(int x, int y, int z);

   /** @deprecated */
   @Deprecated
   void setBiome(int x, int z, Biome bio);

   void setBiome(int x, int y, int z, Biome bio);

   int getMaxHeight();

   void setBlock(int x, int y, int z, BlockData blockData);

   BlockData getBlockData(int x, int y, int z);

   ChunkData getRaw();

   void setRaw(ChunkData data);

   void inject(BiomeGrid biome);
}
