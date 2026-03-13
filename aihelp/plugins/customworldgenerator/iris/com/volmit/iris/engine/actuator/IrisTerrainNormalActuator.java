package com.volmit.iris.engine.actuator;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedActuator;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class IrisTerrainNormalActuator extends EngineAssignedActuator<BlockData> {
   private static final BlockData AIR;
   private static final BlockData BEDROCK;
   private static final BlockData LAVA;
   private static final BlockData GLASS;
   private static final BlockData CAVE_AIR;
   private final RNG rng;
   private int lastBedrock = -1;

   public IrisTerrainNormalActuator(Engine engine) {
      super(var1, "Terrain");
      this.rng = new RNG(var1.getSeedManager().getTerrain());
   }

   @BlockCoordinates
   public void onActuate(int x, int z, Hunk<BlockData> h, boolean multicore, ChunkContext context) {
      PrecisionStopwatch var6 = PrecisionStopwatch.start();

      for(int var7 = 0; var7 < var3.getWidth(); ++var7) {
         this.terrainSliver(var1, var2, var7, var3, var5);
      }

      this.getEngine().getMetrics().getTerrain().put(var6.getMilliseconds());
   }

   private int fluidOrHeight(int height) {
      return Math.max(this.getDimension().getFluidHeight(), var1);
   }

   @BlockCoordinates
   public void terrainSliver(int x, int z, int xf, Hunk<BlockData> h, ChunkContext context) {
      for(int var6 = 0; var6 < var4.getDepth(); ++var6) {
         int var7 = var3 + var1;
         int var8 = var6 + var2;
         IrisBiome var11 = (IrisBiome)var5.getBiome().get(var3, var6);
         IrisRegion var12 = (IrisRegion)var5.getRegion().get(var3, var6);
         int var10 = (int)Math.round(Math.min((double)var4.getHeight(), (Double)var5.getHeight().get(var3, var6)));
         int var9 = Math.round((float)Math.max(Math.min(var4.getHeight(), this.getDimension().getFluidHeight()), var10));
         if (var9 >= 0) {
            KList var13 = null;
            KList var14 = null;

            for(int var17 = var9; var17 >= 0; --var17) {
               if (var17 < var4.getHeight()) {
                  if (var17 == 0 && this.getDimension().isBedrock()) {
                     var4.set(var3, var17, var6, BEDROCK);
                     this.lastBedrock = var17;
                  } else {
                     BlockData var18 = var11.generateOres(var7, var17, var8, this.rng, this.getData(), true);
                     var18 = var18 == null ? var12.generateOres(var7, var17, var8, this.rng, this.getData(), true) : var18;
                     var18 = var18 == null ? this.getDimension().generateOres(var7, var17, var8, this.rng, this.getData(), true) : var18;
                     if (var18 != null) {
                        var4.set(var3, var17, var6, var18);
                     } else if (var17 > var10 && var17 <= var9) {
                        int var16 = var9 - var17;
                        if (var14 == null) {
                           var14 = var11.generateSeaLayers((double)var7, (double)var8, this.rng, var9 - var10, this.getData());
                        }

                        if (var14.hasIndex(var16)) {
                           var4.set(var3, var17, var6, (BlockData)var14.get(var16));
                        } else {
                           var4.set(var3, var17, var6, (BlockData)var5.getFluid().get(var3, var6));
                        }
                     } else if (var17 <= var10) {
                        int var15 = var10 - var17;
                        if (var13 == null) {
                           var13 = var11.generateLayers(this.getDimension(), (double)var7, (double)var8, this.rng, var10, var10, this.getData(), this.getComplex());
                        }

                        if (var13.hasIndex(var15)) {
                           var4.set(var3, var17, var6, (BlockData)var13.get(var15));
                        } else {
                           var18 = var11.generateOres(var7, var17, var8, this.rng, this.getData(), false);
                           var18 = var18 == null ? var12.generateOres(var7, var17, var8, this.rng, this.getData(), false) : var18;
                           var18 = var18 == null ? this.getDimension().generateOres(var7, var17, var8, this.rng, this.getData(), false) : var18;
                           if (var18 != null) {
                              var4.set(var3, var17, var6, var18);
                           } else {
                              var4.set(var3, var17, var6, (BlockData)var5.getRock().get(var3, var6));
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   @Generated
   public RNG getRng() {
      return this.rng;
   }

   @Generated
   public int getLastBedrock() {
      return this.lastBedrock;
   }

   static {
      AIR = Material.AIR.createBlockData();
      BEDROCK = Material.BEDROCK.createBlockData();
      LAVA = Material.LAVA.createBlockData();
      GLASS = Material.GLASS.createBlockData();
      CAVE_AIR = Material.CAVE_AIR.createBlockData();
   }
}
