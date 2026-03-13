package com.volmit.iris.util.hunk.view;

import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.engine.data.chunk.LinkedTerrainChunk;
import com.volmit.iris.util.hunk.Hunk;
import lombok.Generated;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

public class BiomeGridHunkView implements Hunk<Biome> {
   private final BiomeGrid chunk;
   private final int minHeight;
   private final int maxHeight;
   private int highest = -1000;

   public BiomeGridHunkView(BiomeGrid chunk, int minHeight, int maxHeight) {
      this.chunk = var1;
      this.minHeight = var2;
      this.maxHeight = var3;
   }

   public int getWidth() {
      return 16;
   }

   public int getDepth() {
      return 16;
   }

   public int getHeight() {
      return this.maxHeight - this.minHeight;
   }

   public void setRaw(int x, int y, int z, Biome t) {
      this.chunk.setBiome(var1, var2 + this.minHeight, var3, var4);
      if (var2 > this.highest) {
         this.highest = var2;
      }

   }

   public Biome getRaw(int x, int y, int z) {
      return this.chunk.getBiome(var1, var2 + this.minHeight, var3);
   }

   public void forceBiomeBaseInto(int x, int y, int z, Object somethingVeryDirty) {
      if (this.chunk instanceof LinkedTerrainChunk) {
         INMS.get().forceBiomeInto(var1, var2 + this.minHeight, var3, var4, ((LinkedTerrainChunk)this.chunk).getRawBiome());
      } else {
         INMS.get().forceBiomeInto(var1, var2 + this.minHeight, var3, var4, this.chunk);
      }
   }

   @Generated
   public BiomeGrid getChunk() {
      return this.chunk;
   }
}
