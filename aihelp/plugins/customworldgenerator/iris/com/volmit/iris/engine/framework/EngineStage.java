package com.volmit.iris.engine.framework;

import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

public interface EngineStage {
   @BlockCoordinates
   void generate(int x, int z, Hunk<BlockData> blocks, Hunk<Biome> biomes, boolean multicore, ChunkContext context);

   default void close() {
      if (this instanceof EngineComponent) {
         EngineComponent c = (EngineComponent)this;
         c.close();
      }

   }
}
