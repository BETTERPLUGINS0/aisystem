package com.volmit.iris.util.hunk.view;

import com.volmit.iris.util.hunk.storage.AtomicHunk;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class ChunkDataHunkHolder extends AtomicHunk<BlockData> {
   private static final BlockData AIR;
   private final ChunkData chunk;

   public ChunkDataHunkHolder(ChunkData chunk) {
      super(16, var1.getMaxHeight() - var1.getMinHeight(), 16);
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

   public BlockData getRaw(int x, int y, int z) {
      BlockData var4 = (BlockData)super.getRaw(var1, var2, var3);
      return var4 != null ? var4 : AIR;
   }

   public void apply() {
      for(int var1 = 0; var1 < this.getHeight(); ++var1) {
         for(int var2 = 0; var2 < this.getWidth(); ++var2) {
            for(int var3 = 0; var3 < this.getDepth(); ++var3) {
               BlockData var4 = (BlockData)super.getRaw(var2, var1, var3);
               if (var4 != null) {
                  this.chunk.setBlock(var2, var1 + this.chunk.getMinHeight(), var3, var4);
               }
            }
         }
      }

   }

   static {
      AIR = Material.AIR.createBlockData();
   }
}
