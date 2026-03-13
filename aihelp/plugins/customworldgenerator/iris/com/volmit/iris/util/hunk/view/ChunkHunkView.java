package com.volmit.iris.util.hunk.view;

import com.volmit.iris.Iris;
import com.volmit.iris.core.service.EditSVC;
import com.volmit.iris.util.hunk.Hunk;
import org.bukkit.Chunk;
import org.bukkit.block.data.BlockData;

public class ChunkHunkView implements Hunk<BlockData> {
   private final Chunk chunk;

   public ChunkHunkView(Chunk chunk) {
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

   public void setRaw(int x, int y, int z, BlockData t) {
      if (var4 != null) {
         ((EditSVC)Iris.service(EditSVC.class)).set(this.chunk.getWorld(), var1 + this.chunk.getX() * 16, var2, var3 + this.chunk.getZ() * 16, var4);
      }
   }

   public BlockData getRaw(int x, int y, int z) {
      return ((EditSVC)Iris.service(EditSVC.class)).get(this.chunk.getWorld(), var1 + this.chunk.getX() * 16, var2, var3 + this.chunk.getZ() * 16);
   }
}
