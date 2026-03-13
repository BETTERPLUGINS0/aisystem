package com.volmit.iris.engine.framework;

import com.volmit.iris.engine.data.chunk.TerrainChunk;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.hunk.Hunk;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Generated;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class PregeneratedData {
   private final Hunk<BlockData> blocks;
   private final Hunk<BlockData> post;
   private final Hunk<Biome> biomes;
   private final AtomicBoolean postMod = new AtomicBoolean(false);

   public PregeneratedData(int height) {
      this.blocks = Hunk.newAtomicHunk(16, var1, 16);
      this.biomes = Hunk.newAtomicHunk(16, var1, 16);
      Hunk var2 = Hunk.newMappedHunkSynced(16, var1, 16);
      this.post = var2.trackWrite(this.postMod);
   }

   public Runnable inject(TerrainChunk tc) {
      this.blocks.iterateSync((var2, var3, var4, var5) -> {
         if (var5 != null) {
            var1.setBlock(var2, var3, var4, var5);
         }

         Biome var6 = (Biome)this.biomes.get(var2, var3, var4);
         if (var6 != null) {
            var1.setBiome(var2, var3, var4, var6);
         }

      });
      return this.postMod.get() ? () -> {
         Hunk.view((ChunkData)var1).insertSoftly(0, 0, 0, this.post, (var0) -> {
            return var0 == null || B.isAirOrFluid(var0);
         });
      } : () -> {
      };
   }

   @Generated
   public Hunk<BlockData> getBlocks() {
      return this.blocks;
   }

   @Generated
   public Hunk<BlockData> getPost() {
      return this.post;
   }

   @Generated
   public Hunk<Biome> getBiomes() {
      return this.biomes;
   }

   @Generated
   public AtomicBoolean getPostMod() {
      return this.postMod;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof PregeneratedData)) {
         return false;
      } else {
         PregeneratedData var2 = (PregeneratedData)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label59: {
               Hunk var3 = this.getBlocks();
               Hunk var4 = var2.getBlocks();
               if (var3 == null) {
                  if (var4 == null) {
                     break label59;
                  }
               } else if (var3.equals(var4)) {
                  break label59;
               }

               return false;
            }

            Hunk var5 = this.getPost();
            Hunk var6 = var2.getPost();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            Hunk var7 = this.getBiomes();
            Hunk var8 = var2.getBiomes();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            AtomicBoolean var9 = this.getPostMod();
            AtomicBoolean var10 = var2.getPostMod();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof PregeneratedData;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      Hunk var3 = this.getBlocks();
      int var7 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      Hunk var4 = this.getPost();
      var7 = var7 * 59 + (var4 == null ? 43 : var4.hashCode());
      Hunk var5 = this.getBiomes();
      var7 = var7 * 59 + (var5 == null ? 43 : var5.hashCode());
      AtomicBoolean var6 = this.getPostMod();
      var7 = var7 * 59 + (var6 == null ? 43 : var6.hashCode());
      return var7;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getBlocks());
      return "PregeneratedData(blocks=" + var10000 + ", post=" + String.valueOf(this.getPost()) + ", biomes=" + String.valueOf(this.getBiomes()) + ", postMod=" + String.valueOf(this.getPostMod()) + ")";
   }
}
