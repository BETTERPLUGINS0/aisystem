package me.SuperRonanCraft.BetterRTP.lib.paperlib.features.asyncchunks;

import java.util.concurrent.CompletableFuture;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.PaperLib;
import org.bukkit.Chunk;
import org.bukkit.World;

public class AsyncChunksSync implements AsyncChunks {
   public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen, boolean isUrgent) {
      return !gen && !PaperLib.isChunkGenerated(world, x, z) ? CompletableFuture.completedFuture((Object)null) : CompletableFuture.completedFuture(world.getChunkAt(x, z));
   }
}
