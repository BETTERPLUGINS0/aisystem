package me.casperge.realisticseasons.paperlib.features.chunkisgenerated;

import org.bukkit.World;

public class ChunkIsGeneratedApiExists implements ChunkIsGenerated {
   public boolean isChunkGenerated(World var1, int var2, int var3) {
      return var1.isChunkGenerated(var2, var3);
   }
}
