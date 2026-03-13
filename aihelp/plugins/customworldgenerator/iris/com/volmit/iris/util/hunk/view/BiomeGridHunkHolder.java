package com.volmit.iris.util.hunk.view;

import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.engine.data.chunk.LinkedTerrainChunk;
import com.volmit.iris.util.hunk.storage.AtomicHunk;
import lombok.Generated;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

public class BiomeGridHunkHolder extends AtomicHunk<Biome> {
   private final BiomeGrid chunk;
   private final int minHeight;
   private final int maxHeight;

   public BiomeGridHunkHolder(BiomeGrid chunk, int minHeight, int maxHeight) {
      super(16, var3 - var2, 16);
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

   public void apply() {
      for(int var1 = 0; var1 < this.getHeight(); ++var1) {
         for(int var2 = 0; var2 < this.getWidth(); ++var2) {
            for(int var3 = 0; var3 < this.getDepth(); ++var3) {
               Biome var4 = (Biome)super.getRaw(var2, var1, var3);
               if (var4 != null) {
                  this.chunk.setBiome(var2, var1 + this.minHeight, var3, var4);
               }
            }
         }
      }

   }

   public Biome getRaw(int x, int y, int z) {
      Biome var4 = (Biome)super.getRaw(var1, var2, var3);
      return var4 != null ? var4 : Biome.PLAINS;
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
