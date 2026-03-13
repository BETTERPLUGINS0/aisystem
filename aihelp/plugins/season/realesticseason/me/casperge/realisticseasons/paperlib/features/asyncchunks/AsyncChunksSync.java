package me.casperge.realisticseasons.paperlib.features.asyncchunks;

import java.util.concurrent.CompletableFuture;
import me.casperge.realisticseasons.paperlib.PaperLib;
import org.bukkit.Chunk;
import org.bukkit.World;

public class AsyncChunksSync implements AsyncChunks {
   public CompletableFuture<Chunk> getChunkAtAsync(World var1, int var2, int var3, boolean var4, boolean var5) {
      return !var4 && !PaperLib.isChunkGenerated(var1, var2, var3) ? CompletableFuture.completedFuture((Object)null) : CompletableFuture.completedFuture(var1.getChunkAt(var2, var3));
   }
}
