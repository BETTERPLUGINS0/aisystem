package com.volmit.iris.engine.modifier;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedModifier;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisSlopeClip;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Slab;

public class IrisPostModifier extends EngineAssignedModifier<BlockData> {
   private static final BlockData AIR = B.get("AIR");
   private static final BlockData WATER = B.get("WATER");
   private final RNG rng = new RNG(this.getEngine().getSeedManager().getPost());

   public IrisPostModifier(Engine engine) {
      super(var1, "Post");
   }

   public void onModify(int x, int z, Hunk<BlockData> output, boolean multicore, ChunkContext context) {
      PrecisionStopwatch var6 = PrecisionStopwatch.start();
      AtomicInteger var7 = new AtomicInteger();
      AtomicInteger var8 = new AtomicInteger();
      Hunk var9 = var3.synchronize();
      var7.set(0);

      while(var7.get() < var3.getWidth()) {
         var8.set(0);

         while(var8.get() < var3.getDepth()) {
            int var10 = var7.get();
            int var11 = var8.get();
            this.post(var10, var11, var9, var10 + var1, var11 + var2, var5);
            var8.getAndIncrement();
         }

         var7.getAndIncrement();
      }

      this.getEngine().getMetrics().getPost().put(var6.getMilliseconds());
   }

   private void post(int currentPostX, int currentPostZ, Hunk<BlockData> currentData, int x, int z, ChunkContext context) {
      int var7 = this.getEngine().getMantle().trueHeight(var4, var5);
      int var8 = this.getEngine().getMantle().trueHeight(var4 + 1, var5);
      int var9 = this.getEngine().getMantle().trueHeight(var4, var5 + 1);
      int var10 = this.getEngine().getMantle().trueHeight(var4 - 1, var5);
      int var11 = this.getEngine().getMantle().trueHeight(var4, var5 - 1);
      byte var12 = 0;
      if (var7 >= 1) {
         int var18 = var12 + (var8 < var7 - 1 ? 1 : 0);
         var18 += var9 < var7 - 1 ? 1 : 0;
         var18 += var10 < var7 - 1 ? 1 : 0;
         var18 += var11 < var7 - 1 ? 1 : 0;
         if (var18 == 4 && this.isAir(var4, var7 - 1, var5, var1, var2, var3)) {
            this.setPostBlock(var4, var7, var5, AIR, var1, var2, var3);

            for(int var13 = var7 - 1; var13 > 0; --var13) {
               if (!this.isAir(var4, var13, var5, var1, var2, var3)) {
                  var7 = var13;
                  break;
               }
            }
         }

         var12 = 0;
         var18 = var12 + (var8 == var7 - 1 ? 1 : 0);
         var18 += var9 == var7 - 1 ? 1 : 0;
         var18 += var10 == var7 - 1 ? 1 : 0;
         var18 += var11 == var7 - 1 ? 1 : 0;
         BlockData var14;
         Material var15;
         BlockData var19;
         BlockData var22;
         if (var18 >= 4) {
            var19 = this.getPostBlock(var4, var7, var5, var1, var2, var3);
            var14 = this.getPostBlock(var4, var7 + 1, var5, var1, var2, var3);
            var15 = var19.getMaterial();
            if (var14.getMaterial().isOccluding() && var14.getMaterial().isSolid() && var15.isSolid()) {
               this.setPostBlock(var4, var7, var5, var14, var1, var2, var3);
               --var7;
            }
         } else {
            var12 = 0;
            var18 = var12 + (var8 == var7 + 1 ? 1 : 0);
            var18 += var9 == var7 + 1 ? 1 : 0;
            var18 += var10 == var7 + 1 ? 1 : 0;
            var18 += var11 == var7 + 1 ? 1 : 0;
            if (var18 >= 4) {
               var19 = this.getPostBlock(var4, var8, var5, var1, var2, var3);
               var14 = this.getPostBlock(var4, var9, var5, var1, var2, var3);
               var22 = this.getPostBlock(var4, var10, var5, var1, var2, var3);
               BlockData var16 = this.getPostBlock(var4, var11, var5, var1, var2, var3);
               var12 = 0;
               var18 = B.isSolid(var19) ? var12 + 1 : var12;
               var18 = B.isSolid(var14) ? var18 + 1 : var18;
               var18 = B.isSolid(var22) ? var18 + 1 : var18;
               var18 = B.isSolid(var16) ? var18 + 1 : var18;
               if (var18 >= 3) {
                  this.setPostBlock(var4, var7 + 1, var5, this.getPostBlock(var4, var7, var5, var1, var2, var3), var1, var2, var3);
                  ++var7;
               }
            }
         }

         IrisBiome var20 = (IrisBiome)var6.getBiome().get(var1, var2);
         int var25;
         if (this.getDimension().isPostProcessingWalls() && !var20.getWall().getPalette().isEmpty() && (var8 < var7 - 2 || var9 < var7 - 2 || var10 < var7 - 2 || var11 < var7 - 2)) {
            boolean var21 = false;
            int var24 = Math.abs(Math.max(var7 - var8, Math.max(var7 - var9, Math.max(var7 - var10, var7 - var11))));

            for(var25 = var7; var25 > var7 - var24; --var25) {
               BlockData var17 = var20.getWall().get(this.rng, (double)(var4 + var25), (double)(var25 + var7), (double)(var5 + var25), this.getData());
               if (var17 != null) {
                  if (this.isAirOrWater(var4, var25, var5, var1, var2, var3)) {
                     if (var21) {
                        break;
                     }
                  } else {
                     this.setPostBlock(var4, var25, var5, var17, var1, var2, var3);
                     var21 = true;
                  }
               }
            }
         }

         boolean var28;
         if (this.getDimension().isPostProcessingSlabs() && (var8 == var7 + 1 && this.isSolidNonSlab(var4 + 1, var8, var5, var1, var2, var3) || var9 == var7 + 1 && this.isSolidNonSlab(var4, var9, var5 + 1, var1, var2, var3) || var10 == var7 + 1 && this.isSolidNonSlab(var4 - 1, var10, var5, var1, var2, var3) || var11 == var7 + 1 && this.isSolidNonSlab(var4, var11, var5 - 1, var1, var2, var3))) {
            IrisSlopeClip var23 = var20.getSlab().getSlopeCondition();
            var22 = var23.isValid((Double)this.getComplex().getSlopeStream().get((double)var4, (double)var5)) ? var20.getSlab().get(this.rng, (double)var4, (double)var7, (double)var5, this.getData()) : null;
            if (var22 != null) {
               var28 = B.isAir(var22);
               if (var22.getMaterial().equals(Material.SNOW) && var7 + 1 <= this.getDimension().getFluidHeight()) {
                  var28 = true;
               }

               if (this.isSnowLayer(var4, var7, var5, var1, var2, var3)) {
                  var28 = true;
               }

               if (!var28 && this.isAirOrWater(var4, var7 + 1, var5, var1, var2, var3)) {
                  this.setPostBlock(var4, var7 + 1, var5, var22, var1, var2, var3);
                  ++var7;
               }
            }
         }

         var14 = this.getPostBlock(var4, var7, var5, var1, var2, var3);
         if (var14 instanceof Waterlogged) {
            Waterlogged var26 = (Waterlogged)var14.clone();
            var28 = false;
            if (var7 <= this.getDimension().getFluidHeight() + 1) {
               if (this.isWaterOrWaterlogged(var4, var7 + 1, var5, var1, var2, var3)) {
                  var28 = true;
               } else if (this.isWaterOrWaterlogged(var4 + 1, var7, var5, var1, var2, var3) || this.isWaterOrWaterlogged(var4 - 1, var7, var5, var1, var2, var3) || this.isWaterOrWaterlogged(var4, var7, var5 + 1, var1, var2, var3) || this.isWaterOrWaterlogged(var4, var7, var5 - 1, var1, var2, var3)) {
                  var28 = true;
               }
            }

            if (var28 != var26.isWaterlogged()) {
               var26.setWaterlogged(var28);
               this.setPostBlock(var4, var7, var5, var26, var1, var2, var3);
            }
         } else if (var14.getMaterial().equals(Material.AIR) && var7 <= this.getDimension().getFluidHeight() && (this.isWaterOrWaterlogged(var4 + 1, var7, var5, var1, var2, var3) || this.isWaterOrWaterlogged(var4 - 1, var7, var5, var1, var2, var3) || this.isWaterOrWaterlogged(var4, var7, var5 + 1, var1, var2, var3) || this.isWaterOrWaterlogged(var4, var7, var5 - 1, var1, var2, var3))) {
            this.setPostBlock(var4, var7, var5, WATER, var1, var2, var3);
         }

         var14 = this.getPostBlock(var4, var7 + 1, var5, var1, var2, var3);
         if (B.isVineBlock(var14) && var14 instanceof MultipleFacing) {
            MultipleFacing var27 = (MultipleFacing)var14;
            var25 = var7 + 1;
            var27.getAllowedFaces().forEach((var8x) -> {
               BlockData var9 = this.getPostBlock(var4 + var8x.getModX(), var25 + var8x.getModY(), var5 + var8x.getModZ(), var1, var2, var3);
               var27.setFace(var8x, !B.isAir(var9) && !B.isVineBlock(var9));
            });
            this.setPostBlock(var4, var7 + 1, var5, var14, var1, var2, var3);
         }

         if (B.isFoliage(var14) || var14.getMaterial().equals(Material.DEAD_BUSH)) {
            var15 = this.getPostBlock(var4, var7, var5, var1, var2, var3).getMaterial();
            if (!B.canPlaceOnto(var14.getMaterial(), var15) && !B.isDecorant(var14)) {
               this.setPostBlock(var4, var7 + 1, var5, AIR, var1, var2, var3);
            }
         }

      }
   }

   public boolean isAir(int x, int y, int z, int currentPostX, int currentPostZ, Hunk<BlockData> currentData) {
      BlockData var7 = this.getPostBlock(var1, var2, var3, var4, var5, var6);
      return var7.getMaterial().equals(Material.AIR) || var7.getMaterial().equals(Material.CAVE_AIR);
   }

   public boolean hasGravity(int x, int y, int z, int currentPostX, int currentPostZ, Hunk<BlockData> currentData) {
      BlockData var7 = this.getPostBlock(var1, var2, var3, var4, var5, var6);
      return var7.getMaterial().equals(Material.SAND) || var7.getMaterial().equals(Material.RED_SAND) || var7.getMaterial().equals(Material.BLACK_CONCRETE_POWDER) || var7.getMaterial().equals(Material.BLUE_CONCRETE_POWDER) || var7.getMaterial().equals(Material.BROWN_CONCRETE_POWDER) || var7.getMaterial().equals(Material.CYAN_CONCRETE_POWDER) || var7.getMaterial().equals(Material.GRAY_CONCRETE_POWDER) || var7.getMaterial().equals(Material.GREEN_CONCRETE_POWDER) || var7.getMaterial().equals(Material.LIGHT_BLUE_CONCRETE_POWDER) || var7.getMaterial().equals(Material.LIGHT_GRAY_CONCRETE_POWDER) || var7.getMaterial().equals(Material.LIME_CONCRETE_POWDER) || var7.getMaterial().equals(Material.MAGENTA_CONCRETE_POWDER) || var7.getMaterial().equals(Material.ORANGE_CONCRETE_POWDER) || var7.getMaterial().equals(Material.PINK_CONCRETE_POWDER) || var7.getMaterial().equals(Material.PURPLE_CONCRETE_POWDER) || var7.getMaterial().equals(Material.RED_CONCRETE_POWDER) || var7.getMaterial().equals(Material.WHITE_CONCRETE_POWDER) || var7.getMaterial().equals(Material.YELLOW_CONCRETE_POWDER);
   }

   public boolean isSolid(int x, int y, int z, int currentPostX, int currentPostZ, Hunk<BlockData> currentData) {
      BlockData var7 = this.getPostBlock(var1, var2, var3, var4, var5, var6);
      return var7.getMaterial().isSolid() && !B.isVineBlock(var7);
   }

   public boolean isSolidNonSlab(int x, int y, int z, int currentPostX, int currentPostZ, Hunk<BlockData> currentData) {
      BlockData var7 = this.getPostBlock(var1, var2, var3, var4, var5, var6);
      return var7.getMaterial().isSolid() && !(var7 instanceof Slab);
   }

   public boolean isAirOrWater(int x, int y, int z, int currentPostX, int currentPostZ, Hunk<BlockData> currentData) {
      BlockData var7 = this.getPostBlock(var1, var2, var3, var4, var5, var6);
      return var7.getMaterial().equals(Material.WATER) || var7.getMaterial().equals(Material.AIR) || var7.getMaterial().equals(Material.CAVE_AIR);
   }

   public boolean isSlab(int x, int y, int z, int currentPostX, int currentPostZ, Hunk<BlockData> currentData) {
      BlockData var7 = this.getPostBlock(var1, var2, var3, var4, var5, var6);
      return var7 instanceof Slab;
   }

   public boolean isSnowLayer(int x, int y, int z, int currentPostX, int currentPostZ, Hunk<BlockData> currentData) {
      BlockData var7 = this.getPostBlock(var1, var2, var3, var4, var5, var6);
      return var7.getMaterial().equals(Material.SNOW);
   }

   public boolean isWater(int x, int y, int z, int currentPostX, int currentPostZ, Hunk<BlockData> currentData) {
      BlockData var7 = this.getPostBlock(var1, var2, var3, var4, var5, var6);
      return var7.getMaterial().equals(Material.WATER);
   }

   public boolean isWaterOrWaterlogged(int x, int y, int z, int currentPostX, int currentPostZ, Hunk<BlockData> currentData) {
      BlockData var7 = this.getPostBlock(var1, var2, var3, var4, var5, var6);
      return var7.getMaterial().equals(Material.WATER) || var7 instanceof Waterlogged && ((Waterlogged)var7).isWaterlogged();
   }

   public boolean isLiquid(int x, int y, int z, int currentPostX, int currentPostZ, Hunk<BlockData> currentData) {
      BlockData var7 = this.getPostBlock(var1, var2, var3, var4, var5, var6);
      return var7 instanceof Levelled;
   }

   public void setPostBlock(int x, int y, int z, BlockData d, int currentPostX, int currentPostZ, Hunk<BlockData> currentData) {
      if (var2 < var7.getHeight()) {
         var7.set(var1 & 15, var2, var3 & 15, var4);
      }

   }

   public BlockData getPostBlock(int x, int y, int z, int cpx, int cpz, Hunk<BlockData> h) {
      BlockData var7 = (BlockData)var6.getClosest(var1 & 15, var2, var3 & 15);
      return var7 == null ? AIR : var7;
   }
}
