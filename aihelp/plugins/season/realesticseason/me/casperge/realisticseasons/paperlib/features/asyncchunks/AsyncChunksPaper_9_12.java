package me.casperge.realisticseasons.paperlib.features.asyncchunks;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import me.casperge.realisticseasons.paperlib.PaperLib;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.World.ChunkLoadCallback;

public class AsyncChunksPaper_9_12 implements AsyncChunks {
   public CompletableFuture<Chunk> getChunkAtAsync(World var1, int var2, int var3, boolean var4, boolean var5) {
      CompletableFuture var6 = new CompletableFuture();
      if (!var4 && PaperLib.getMinecraftVersion() >= 12 && !var1.isChunkGenerated(var2, var3)) {
         var6.complete((Object)null);
      } else {
         Objects.requireNonNull(var6);
         ChunkLoadCallback var7 = var6::complete;
         var1.getChunkAtAsync(var2, var3, var7);
      }

      return var6;
   }
}
