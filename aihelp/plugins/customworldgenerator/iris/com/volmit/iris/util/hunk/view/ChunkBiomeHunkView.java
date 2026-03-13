package com.volmit.iris.util.hunk.view;

import com.volmit.iris.Iris;
import com.volmit.iris.core.service.EditSVC;
import com.volmit.iris.util.hunk.Hunk;
import org.bukkit.Chunk;
import org.bukkit.block.Biome;

public class ChunkBiomeHunkView implements Hunk<Biome> {
   private final Chunk chunk;

   public ChunkBiomeHunkView(Chunk chunk) {
      this.chunk = var1;
   }

   public int getWidth() {
      return 16;
   }

   public int getDepth() {
      return 16;
   }

   public int getHeight() {
      return this.chunk.getWorld().getMaxHeight();
   }

   public void setRaw(int x, int y, int z, Biome t) {
      if (var4 != null) {
         ((EditSVC)Iris.service(EditSVC.class)).setBiome(this.chunk.getWorld(), var1 + this.chunk.getX() * 16, var2, var3 + this.chunk.getZ() * 16, var4);
      }
   }

   public Biome getRaw(int x, int y, int z) {
      return ((EditSVC)Iris.service(EditSVC.class)).getBiome(this.chunk.getWorld(), var1 + this.chunk.getX() * 16, var2, var3 + this.chunk.getZ() * 16);
   }
}
