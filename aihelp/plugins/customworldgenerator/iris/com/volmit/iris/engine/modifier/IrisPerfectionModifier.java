package com.volmit.iris.engine.modifier;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedModifier;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;

public class IrisPerfectionModifier extends EngineAssignedModifier<BlockData> {
   private static final BlockData AIR = B.get("AIR");
   private static final BlockData WATER = B.get("WATER");

   public IrisPerfectionModifier(Engine engine) {
      super(var1, "Perfection");
   }

   public void onModify(int x, int z, Hunk<BlockData> output, boolean multicore, ChunkContext context) {
      PrecisionStopwatch var6 = PrecisionStopwatch.start();
      AtomicBoolean var7 = new AtomicBoolean(true);
      int var8 = 0;
      AtomicInteger var9 = new AtomicInteger();
      ArrayList var10 = new ArrayList();
      ArrayList var11 = new ArrayList();
      BurstExecutor var12 = this.burst().burst(var4);

      while(var7.get()) {
         ++var8;
         var7.set(false);

         for(int var13 = 0; var13 < 16; ++var13) {
            var12.queue(() -> {
               label81:
               for(int var7x = 0; var7x < 16; ++var7x) {
                  var10.clear();
                  var11.clear();
                  int var8 = this.getHeight(var3, var13, var7x);
                  boolean var9x = true;
                  var10.add(var8);

                  for(int var10x = var8; var10x >= 0; --var10x) {
                     BlockData var11x = (BlockData)var3.get(var13, var10x, var7x);
                     boolean var12 = var11x != null && !B.isAir(var11x) && !B.isFluid(var11x);
                     if (var12 != var9x) {
                        var9x = var12;
                        if (var12) {
                           var10.add(var10x);
                        } else {
                           var11.add(var10x + 1);
                        }
                     }
                  }

                  Iterator var17 = var10.iterator();

                  while(true) {
                     boolean var13x;
                     boolean var14;
                     int var18;
                     BlockData var19;
                     do {
                        do {
                           if (!var17.hasNext()) {
                              continue label81;
                           }

                           var18 = (Integer)var17.next();
                           var19 = (BlockData)var3.get(var13, var18, var7x);
                        } while(var19 == null);

                        var13x = false;
                        var14 = false;
                     } while(!B.isDecorant(var19));

                     BlockData var15 = (BlockData)var3.get(var13, var18 - 1, var7x);
                     if (var15 == null) {
                        var13x = true;
                     } else if (!B.canPlaceOnto(var19.getMaterial(), var15.getMaterial())) {
                        var13x = true;
                     } else if (var15 instanceof Bisected) {
                        BlockData var16 = (BlockData)var3.get(var13, var18 - 2, var7x);
                        if (var16 == null || !B.canPlaceOnto(var15.getMaterial(), var16.getMaterial())) {
                           var13x = true;
                           var14 = true;
                        }
                     }

                     if (var13x) {
                        var7.set(true);
                        var9.getAndIncrement();
                        var3.set(var13, var18, var7x, AIR);
                        if (var14) {
                           var9.getAndIncrement();
                           var3.set(var13, var18 - 1, var7x, AIR);
                        }
                     }
                  }
               }

            });
         }
      }

      this.getEngine().getMetrics().getPerfection().put(var6.getMilliseconds());
   }

   private int getHeight(Hunk<BlockData> output, int x, int z) {
      for(int var4 = var1.getHeight() - 1; var4 >= 0; --var4) {
         BlockData var5 = (BlockData)var1.get(var2, var4, var3);
         if (var5 != null && !B.isAir(var5) && !B.isFluid(var5)) {
            return var4;
         }
      }

      return 0;
   }
}
