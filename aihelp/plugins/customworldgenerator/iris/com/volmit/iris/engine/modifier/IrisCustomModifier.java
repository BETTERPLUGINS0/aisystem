package com.volmit.iris.engine.modifier;

import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedModifier;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.data.IrisCustomData;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.mantle.MantleChunk;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import org.bukkit.block.data.BlockData;

public class IrisCustomModifier extends EngineAssignedModifier<BlockData> {
   public IrisCustomModifier(Engine engine) {
      super(var1, "Custom");
   }

   public void onModify(int x, int z, Hunk<BlockData> output, boolean multicore, ChunkContext context) {
      MantleChunk var6 = this.getEngine().getMantle().getMantle().getChunk(var1 >> 4, var2 >> 4);
      if (var6.isFlagged(MantleFlag.CUSTOM_ACTIVE)) {
         var6.use();
         BurstExecutor var7 = MultiBurst.burst.burst(var3.getHeight());
         var7.setMulticore(var4);

         for(int var8 = 0; var8 < var3.getHeight(); ++var8) {
            var7.queue(() -> {
               for(int var3x = 0; var3x < var3.getWidth(); ++var3x) {
                  for(int var4 = 0; var4 < var3.getDepth(); ++var4) {
                     BlockData var5 = (BlockData)var3.get(var3x, var8, var4);
                     if (var5 instanceof IrisCustomData) {
                        IrisCustomData var6x = (IrisCustomData)var5;
                        var6.getOrCreate(var8 >> 4).slice(Identifier.class).set(var3x, var8 & 15, var4, var6x.getCustom());
                        var3.set(var3x, var8, var4, var6x.getBase());
                     }
                  }
               }

            });
         }

         var7.complete();
         var6.release();
      }
   }
}
