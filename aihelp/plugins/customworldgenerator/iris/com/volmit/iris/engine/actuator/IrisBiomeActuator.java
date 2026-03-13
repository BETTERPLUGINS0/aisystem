package com.volmit.iris.engine.actuator;

import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.nms.INMSBinding;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedActuator;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisBiomeCustom;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.MatterBiomeInject;
import com.volmit.iris.util.matter.slices.BiomeInjectMatter;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import org.bukkit.block.Biome;

public class IrisBiomeActuator extends EngineAssignedActuator<Biome> {
   private final RNG rng;
   private final ChronoLatch cl = new ChronoLatch(5000L);

   public IrisBiomeActuator(Engine engine) {
      super(var1, "Biome");
      this.rng = new RNG(var1.getSeedManager().getBiome());
   }

   @BlockCoordinates
   public void onActuate(int x, int z, Hunk<Biome> h, boolean multicore, ChunkContext context) {
      try {
         PrecisionStopwatch var6 = PrecisionStopwatch.start();

         for(int var7 = 0; var7 < var3.getWidth(); ++var7) {
            for(int var9 = 0; var9 < var3.getDepth(); ++var9) {
               IrisBiome var8 = (IrisBiome)var5.getBiome().get(var7, var9);
               MatterBiomeInject var10;
               if (var8.isCustom()) {
                  IrisBiomeCustom var11 = var8.getCustomBiome(this.rng, (double)var1, 0.0D, (double)var2);
                  INMSBinding var10000 = INMS.get();
                  String var10001 = this.getDimension().getLoadKey();
                  var10 = BiomeInjectMatter.get(var10000.getBiomeBaseIdForKey(var10001 + ":" + var11.getId()));
               } else {
                  Biome var13 = var8.getSkyBiome(this.rng, (double)var1, 0.0D, (double)var2);
                  var10 = BiomeInjectMatter.get(var13);
               }

               this.getEngine().getMantle().getMantle().set(var1 + var7, 0, var2 + var9, (Object)var10);
            }
         }

         this.getEngine().getMetrics().getBiome().put(var6.getMilliseconds());
      } catch (Throwable var12) {
         var12.printStackTrace();
      }

   }
}
