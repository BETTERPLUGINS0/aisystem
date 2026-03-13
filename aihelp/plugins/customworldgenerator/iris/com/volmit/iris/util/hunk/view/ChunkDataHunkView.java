package com.volmit.iris.util.hunk.view;

import com.volmit.iris.util.data.B;
import com.volmit.iris.util.data.IrisCustomData;
import com.volmit.iris.util.hunk.Hunk;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class ChunkDataHunkView implements Hunk<BlockData> {
   private static final BlockData AIR = B.getAir();
   private final ChunkData chunk;

   public ChunkDataHunkView(ChunkData chunk) {
      this.chunk = var1;
   }

   public int getWidth() {
      return 16;
   }

   public int getDepth() {
      return 16;
   }

   public int getHeight() {
      return this.chunk.getMaxHeight() - this.chunk.getMinHeight();
   }

   public void set(int x1, int y1, int z1, int x2, int y2, int z2, BlockData t) {
      if (var7 != null) {
         this.chunk.setRegion(var1, var2 + this.chunk.getMinHeight(), var3, var4, var5 + this.chunk.getMinHeight(), var6, var7);
      }
   }

   public BlockData get(int x, int y, int z) {
      return this.getRaw(var1, var2, var3);
   }

   public void set(int x, int y, int z, BlockData t) {
      this.setRaw(var1, var2, var3, var4);
   }

   public void setRaw(int x, int y, int z, BlockData t) {
      if (var4 != null) {
         try {
            if (var4 instanceof IrisCustomData) {
               IrisCustomData var5 = (IrisCustomData)var4;
               var4 = var5.getBase();
            }

            this.chunk.setBlock(var1, var2 + this.chunk.getMinHeight(), var3, var4);
         } catch (Throwable var6) {
         }

      }
   }

   public BlockData getRaw(int x, int y, int z) {
      try {
         return this.chunk.getBlockData(var1, var2 + this.chunk.getMinHeight(), var3);
      } catch (Throwable var5) {
         return AIR;
      }
   }
}
