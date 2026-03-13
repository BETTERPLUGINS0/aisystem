package com.volmit.iris.engine.modifier;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedModifier;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisDepositGenerator;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.data.HeightMap;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.mantle.MantleChunk;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.MatterCavern;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BlockVector;

public class IrisDepositModifier extends EngineAssignedModifier<BlockData> {
   private final RNG rng = new RNG(this.getEngine().getSeedManager().getDeposit());

   public IrisDepositModifier(Engine engine) {
      super(var1, "Deposit");
   }

   public void onModify(int x, int z, Hunk<BlockData> output, boolean multicore, ChunkContext context) {
      PrecisionStopwatch var6 = PrecisionStopwatch.start();
      this.generateDeposits(var3, Math.floorDiv(var1, 16), Math.floorDiv(var2, 16), var4, var5);
      this.getEngine().getMetrics().getDeposit().put(var6.getMilliseconds());
   }

   public void generateDeposits(Hunk<BlockData> terrain, int x, int z, boolean multicore, ChunkContext context) {
      IrisRegion var6 = (IrisRegion)var5.getRegion().get(7, 7);
      IrisBiome var7 = (IrisBiome)var5.getBiome().get(7, 7);
      BurstExecutor var8 = this.burst().burst(var4);
      long var9 = (long)var2 * 341873128712L + (long)var3 * 132897987541L;
      long var11 = 0L;
      MantleChunk var13 = this.getEngine().getMantle().getMantle().getChunk(var2, var3).use();
      Iterator var14 = this.getDimension().getDeposits().iterator();

      IrisDepositGenerator var15;
      long var16;
      while(var14.hasNext()) {
         var15 = (IrisDepositGenerator)var14.next();
         var16 = var9 * ++var11;
         var8.queue(() -> {
            this.generate(var15, var13, var1, this.rng.nextParallelRNG(var16), var2, var3, false, var5);
         });
      }

      var14 = var6.getDeposits().iterator();

      while(var14.hasNext()) {
         var15 = (IrisDepositGenerator)var14.next();
         var16 = var9 * ++var11;
         var8.queue(() -> {
            this.generate(var15, var13, var1, this.rng.nextParallelRNG(var16), var2, var3, false, var5);
         });
      }

      var14 = var7.getDeposits().iterator();

      while(var14.hasNext()) {
         var15 = (IrisDepositGenerator)var14.next();
         var16 = var9 * ++var11;
         var8.queue(() -> {
            this.generate(var15, var13, var1, this.rng.nextParallelRNG(var16), var2, var3, false, var5);
         });
      }

      var8.complete();
      var13.release();
   }

   public void generate(IrisDepositGenerator k, MantleChunk chunk, Hunk<BlockData> data, RNG rng, int cx, int cz, boolean safe, ChunkContext context) {
      this.generate(var1, var2, var3, var4, var5, var6, var7, (HeightMap)null, var8);
   }

   public void generate(IrisDepositGenerator k, MantleChunk chunk, Hunk<BlockData> data, RNG rng, int cx, int cz, boolean safe, HeightMap he, ChunkContext context) {
      if (!(var1.getSpawnChance() < var4.d())) {
         label108:
         for(int var10 = 0; var10 < var4.i(var1.getMinPerChunk(), var1.getMaxPerChunk() + 1); ++var10) {
            if (!(var1.getPerClumpSpawnChance() < var4.d())) {
               IrisObject var11 = var1.getClump(this.getEngine(), var4, this.getData());
               int var12 = var11.getW();
               int var13 = var12 / 2;
               int var14 = (int)(16.0D - (double)var12 / 2.0D);
               if (var13 > var14 || var13 < 0 || var14 > 15) {
                  var13 = 6;
                  var14 = 9;
               }

               int var15 = var4.i(var13, var14 + 1);
               int var16 = var4.i(var13, var14 + 1);
               int var17 = (var8 != null ? var8.getHeight((var5 << 4) + var15, (var6 << 4) + var16) : (int)Math.round((Double)var9.getHeight().get(var15, var16))) - 7;
               if (var17 > 0) {
                  int var18 = Math.max(0, var1.getMinHeight());
                  int var19 = Math.min(var17, Math.min(this.getEngine().getHeight(), var1.getMaxHeight()));
                  if (var18 < var19) {
                     int var20 = var4.i(var18, var19 + 1);
                     if (var20 <= var1.getMaxHeight() && var20 >= var1.getMinHeight() && var20 <= var17 - 2) {
                        Iterator var21 = var11.getBlocks().keys().iterator();

                        while(true) {
                           BlockVector var22;
                           int var23;
                           int var24;
                           int var25;
                           do {
                              do {
                                 do {
                                    do {
                                       do {
                                          do {
                                             do {
                                                do {
                                                   if (!var21.hasNext()) {
                                                      continue label108;
                                                   }

                                                   var22 = (BlockVector)var21.next();
                                                   var23 = var22.getBlockX() + var15;
                                                   var24 = var22.getBlockY() + var20;
                                                   var25 = var22.getBlockZ() + var16;
                                                } while(var24 > var17);
                                             } while(var23 > 15);
                                          } while(var23 < 0);
                                       } while(var24 > this.getEngine().getHeight());
                                    } while(var24 < 0);
                                 } while(var25 < 0);
                              } while(var25 > 15);
                           } while(!var1.isReplaceBedrock() && ((BlockData)var3.get(var23, var24, var25)).getMaterial() == Material.BEDROCK);

                           if (var2.get(var23, var24, var25, MatterCavern.class) == null) {
                              var3.set(var23, var24, var25, B.toDeepSlateOre((BlockData)var3.get(var23, var24, var25), (BlockData)var11.getBlocks().get(var22)));
                           }
                        }
                     }
                  }
               }
            }
         }

      }
   }
}
