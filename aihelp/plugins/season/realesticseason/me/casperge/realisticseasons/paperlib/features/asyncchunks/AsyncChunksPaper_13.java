package me.casperge.realisticseasons.paperlib.features.asyncchunks;

import java.util.concurrent.CompletableFuture;
import org.bukkit.Chunk;
import org.bukkit.World;

public class AsyncChunksPaper_13 implements AsyncChunks {
   public CompletableFuture<Chunk> getChunkAtAsync(World var1, int var2, int var3, boolean var4, boolean var5) {
      return var1.getChunkAtAsync(var2, var3, var4);
   }
}
