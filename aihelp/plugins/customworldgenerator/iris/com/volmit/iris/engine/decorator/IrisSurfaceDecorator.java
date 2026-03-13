package com.volmit.iris.engine.decorator;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.InferredType;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisDecorationPart;
import com.volmit.iris.engine.object.IrisDecorator;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.math.RNG;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.PointedDripstone.Thickness;

public class IrisSurfaceDecorator extends IrisEngineDecorator {
   public IrisSurfaceDecorator(Engine engine) {
      super(var1, "Surface", IrisDecorationPart.NONE);
   }

   @BlockCoordinates
   public void decorate(int x, int z, int realX, int realX1, int realX_1, int realZ, int realZ1, int realZ_1, Hunk<BlockData> data, IrisBiome biome, int height, int max) {
      if (!var10.getInferredType().equals(InferredType.SHORE) || var11 >= this.getDimension().getFluidHeight()) {
         RNG var15 = this.getRNG(var3, var6);
         IrisDecorator var16 = this.getDecorator(var15, var10, (double)var3, (double)var6);
         BlockData var14 = (BlockData)var9.get(var1, var11, var2);
         boolean var17 = var11 < this.getDimension().getFluidHeight();
         if (var16 != null) {
            if (!var16.isForcePlace() && !var16.getSlopeCondition().isDefault() && !var16.getSlopeCondition().isValid((Double)this.getComplex().getSlopeStream().get((double)var3, (double)var6))) {
               return;
            }

            BlockData var13;
            if (!var16.isStacking()) {
               var13 = var16.getBlockData100(var10, var15, (double)var3, (double)var11, (double)var6, this.getData());
               if (!var17 && !this.canGoOn(var13, var14) && !var16.isForcePlace() && var16.getForceBlock() == null) {
                  return;
               }

               if (var16.getForceBlock() != null) {
                  var9.set(var1, var11, var2, this.fixFaces(var16.getForceBlock().getBlockData(this.getData()), var9, var1, var2, var3, var11, var6));
               } else if (!var16.isForcePlace()) {
                  if (var16.getWhitelist() != null && var16.getWhitelist().stream().noneMatch((var2x) -> {
                     return var2x.getBlockData(this.getData()).equals(var14);
                  })) {
                     return;
                  }

                  if (var16.getBlacklist() != null && var16.getBlacklist().stream().anyMatch((var2x) -> {
                     return var2x.getBlockData(this.getData()).equals(var14);
                  })) {
                     return;
                  }
               }

               if (var13 instanceof Bisected) {
                  var13 = var13.clone();
                  ((Bisected)var13).setHalf(Half.TOP);

                  try {
                     var9.set(var1, var11 + 2, var2, var13);
                  } catch (Throwable var24) {
                     Iris.reportError(var24);
                  }

                  var13 = var13.clone();
                  ((Bisected)var13).setHalf(Half.BOTTOM);
               }

               if (B.isAir((BlockData)var9.get(var1, var11 + 1, var2))) {
                  var9.set(var1, var11 + 1, var2, this.fixFaces(var13, var9, var1, var2, var3, var11 + 1, var6));
               }
            } else {
               if (var11 < this.getDimension().getFluidHeight()) {
                  var12 = this.getDimension().getFluidHeight();
               }

               int var18 = var16.getHeight(var15, (double)var3, (double)var6, this.getData());
               if (var16.isScaleStack()) {
                  var18 = Math.min((int)Math.ceil((double)var12 * ((double)var18 / 100.0D)), var16.getAbsoluteMaxStack());
               } else {
                  var18 = Math.min(var12, var18);
               }

               if (var18 == 1) {
                  var9.set(var1, var11, var2, var16.getBlockDataForTop(var10, var15, (double)var3, (double)var11, (double)var6, this.getData()));
                  return;
               }

               for(int var19 = 0; var19 < var18; ++var19) {
                  int var20 = var11 + var19;
                  double var21 = (double)var19 / (double)(var18 - 1);
                  var13 = var21 >= var16.getTopThreshold() ? var16.getBlockDataForTop(var10, var15, (double)var3, (double)var20, (double)var6, this.getData()) : var16.getBlockData100(var10, var15, (double)var3, (double)var20, (double)var6, this.getData());
                  if (var13 == null || var19 == 0 && !var17 && !this.canGoOn(var13, var14) || var17 && var11 + 1 + var19 > this.getDimension().getFluidHeight()) {
                     break;
                  }

                  if (var13 instanceof PointedDripstone) {
                     Thickness var23 = Thickness.BASE;
                     if (var18 == 2) {
                        var23 = Thickness.FRUSTUM;
                        if (var19 == var18 - 1) {
                           var23 = Thickness.TIP;
                        }
                     } else if (var19 == var18 - 1) {
                        var23 = Thickness.TIP;
                     } else if (var19 == var18 - 2) {
                        var23 = Thickness.FRUSTUM;
                     }

                     var13 = Material.POINTED_DRIPSTONE.createBlockData();
                     ((PointedDripstone)var13).setThickness(var23);
                     ((PointedDripstone)var13).setVerticalDirection(BlockFace.UP);
                  }

                  var9.set(var1, var11 + 1 + var19, var2, var13);
               }
            }
         }

      }
   }
}
