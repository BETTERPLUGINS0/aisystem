package com.lenis0012.bukkit.loginsecurity.libs.paper.features.chunkisgenerated;

import org.bukkit.World;

public class ChunkIsGeneratedUnknown implements ChunkIsGenerated {
   public boolean isChunkGenerated(World world, int x, int z) {
      return true;
   }
}
