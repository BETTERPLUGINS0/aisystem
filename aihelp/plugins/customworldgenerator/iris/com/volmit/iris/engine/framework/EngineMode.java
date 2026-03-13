package com.volmit.iris.engine.framework;

import com.volmit.iris.engine.IrisComplex;
import com.volmit.iris.engine.mantle.EngineMantle;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.context.IrisContext;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.math.RollingSequence;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import java.util.Iterator;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

public interface EngineMode extends Staged {
   RollingSequence r = new RollingSequence(64);
   RollingSequence r2 = new RollingSequence(256);

   void close();

   Engine getEngine();

   default MultiBurst burst() {
      return this.getEngine().burst();
   }

   default EngineStage burst(EngineStage... stages) {
      return (x, z, blocks, biomes, multicore, ctx) -> {
         BurstExecutor e = this.burst().burst(stages.length);
         e.setMulticore(multicore);
         EngineStage[] var9 = stages;
         int var10 = stages.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            EngineStage i = var9[var11];
            e.queue(() -> {
               i.generate(x, z, blocks, biomes, multicore, ctx);
            });
         }

         e.complete();
      };
   }

   default IrisComplex getComplex() {
      return this.getEngine().getComplex();
   }

   default EngineMantle getMantle() {
      return this.getEngine().getMantle();
   }

   default void generateMatter(int x, int z, boolean multicore, ChunkContext context) {
      this.getMantle().generateMatter(x, z, multicore, context);
   }

   @BlockCoordinates
   default void generate(int x, int z, Hunk<BlockData> blocks, Hunk<Biome> biomes, boolean multicore) {
      ChunkContext ctx = new ChunkContext(x, z, this.getComplex());
      IrisContext.getOr(this.getEngine()).setChunkContext(ctx);
      Iterator var7 = this.getStages().iterator();

      while(var7.hasNext()) {
         EngineStage i = (EngineStage)var7.next();
         i.generate(x, z, blocks, biomes, multicore, ctx);
      }

   }
}
